package com.woodchat.server;

import com.woodchat.connection.message.Message;
import com.woodchat.connection.message.User;
import com.woodchat.connection.network.SocketConnection;
import com.woodchat.connection.network.ConnectionObserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.*;

public class ChatServer implements ConnectionObserver {
    private final StorageData storageData = new StorageData();
    private final LinkedHashMap<SocketConnection, User> activeUserSet = new LinkedHashMap<>();
    private LinkedHashSet<User> userSetDB = new LinkedHashSet<>();
    private LinkedList<Message> messagesList = new LinkedList<>();

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        new ChatServer();
    }

    private ChatServer() throws SQLException, ClassNotFoundException {
        System.out.println("Server running...");
        getAllUsers();
        getAllMessages();
        addUserDB(new User());
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                try {
                    new SocketConnection(this, serverSocket.accept());
                } catch (IOException e) {
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(SocketConnection socketConnection) {
        activeUserSet.put(socketConnection, new User());
        sendAllDBMessages(socketConnection);

    }

    @Override
    public synchronized void onReceiveMessage(SocketConnection socketConnection, Message message) {
        if (message.getUser().getUserName().equals("System")) {
            if (message.getText().substring(0, 5).equals("User:")) {
                User user = new User(message.getText().substring(5), message.getUser().getColor());
                boolean userExist = false;
                Iterator<User> itr1 = activeUserSet.values().iterator();
                while (itr1.hasNext()) {
                    if (itr1.next().getUserName().equals(user.getUserName())) {
                        userExist = true;
                    }
                }

                if (userExist) {
                    socketConnection.sendMessage(new Message("User exists" + user.getUserName(), new User("System")));
                } else {
                    activeUserSet.replace(socketConnection, user);
                    addUserDB(user);
                    Message hiMessage = new Message("" + message.getText().substring(5) + " joined the server!", new User());
                    addMassageDB(hiMessage);
                    sendAllConnections(hiMessage);
                }

            } else {
                sendAllConnections(message);
                addMassageDB(message);
            }

        } else {
            sendAllConnections(message);
            addMassageDB(message);
        }
    }

    @Override
    public synchronized void onDisconnect(SocketConnection socketConnection) {
        Message message = new Message("" + activeUserSet.get(socketConnection).getUserName() + " disconnected!", new User());
        addMassageDB(message);
        sendAllConnections(message);
        activeUserSet.remove(socketConnection);

    }

    @Override
    public synchronized void onExpression(SocketConnection socketConnection, Exception e) {
        System.out.println("TCP SocketConnection exception: " + e);
    }

    private void sendAllConnections(Message message) {
        Iterator<SocketConnection> itr1 = activeUserSet.keySet().iterator();
        while (itr1.hasNext())
            itr1.next().sendMessage(message);
    }

    private synchronized void addMassageDB(Message message) {
        getAllUsers();
        Iterator<User> iterator = userSetDB.iterator();
        long id = 0;
        if (userSetDB.size() != 0) {
            while (iterator.hasNext()) {
                User iterUser = iterator.next();
                if (iterUser.getUserName().equals(message.getUser().getUserName())) {
                    id = iterUser.getUserId();
                }
            }
        }
        if (id != 0) {
            message.getUser().setUserId(id);
            storageData.saveMessage(message);
        } else {
            System.out.println("нет пользователя в базе");
        }

    }

    private synchronized void addUserDB(User user) {
        storageData.saveUser(user);
    }

    private synchronized void getAllUsers() {
        try {
            Iterator<User> iterator = storageData.getAllUsers().iterator();
            while (iterator.hasNext()) {
                userSetDB.add(iterator.next());
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private synchronized void getAllMessages() {
        try {
            messagesList = storageData.getAllMessages();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void sendAllDBMessages(SocketConnection socketConnection) {
        getAllMessages();
        Iterator<Message> iterator = messagesList.iterator();
        if (messagesList.size() > 0) {
            while (iterator.hasNext()) {
                socketConnection.sendMessage(iterator.next());
            }
        }
    }

}
