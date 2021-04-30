package com.woodchat.connection.network;


import com.woodchat.connection.message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class SocketConnection {
    private final Socket socket;
    private final Thread rxTread;
    private final ConnectionObserver eventObserver;

    public SocketConnection(ConnectionObserver eventObserver, String ipAddress, int port) throws IOException {
        this(eventObserver, new Socket(ipAddress, port));
    }

    public SocketConnection(ConnectionObserver eventObserver, Socket socket) throws IOException {
        this.eventObserver = eventObserver;
        this.socket = socket;

        rxTread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    eventObserver.onConnectionReady(SocketConnection.this);
                    while (!rxTread.isInterrupted()) {
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                        Message message = (Message) in.readObject();
                        eventObserver.onReceiveMessage(SocketConnection.this, message);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    eventObserver.onExpression(SocketConnection.this, e);
                } finally {
                    eventObserver.onDisconnect(SocketConnection.this);
                }
            }
        });
        rxTread.start();
    }

    public synchronized void sendMessage(Message value) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(value);
            out.flush();
        } catch (IOException e) {
            eventObserver.onExpression(SocketConnection.this, e);
            disconnect();
        }

    }

    public synchronized void disconnect() {
        rxTread.interrupt();

        try {
            socket.close();
        } catch (IOException e) {
            eventObserver.onExpression(SocketConnection.this, e);
        }


    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocketConnection that = (SocketConnection) o;
        return socket.equals(that.socket);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket);
    }

    @Override
    public String toString() {
        return "SocketConnection:" + socket.getInetAddress() + " " + socket.getPort();
    }
}
