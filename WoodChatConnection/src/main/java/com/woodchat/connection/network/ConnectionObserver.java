package com.woodchat.connection.network;

import com.woodchat.connection.message.Message;

public interface ConnectionObserver {

    void onConnectionReady(SocketConnection socketConnection);

    void onReceiveMessage(SocketConnection socketConnection, Message message);

    void onDisconnect(SocketConnection socketConnection);

    void onExpression(SocketConnection socketConnection, Exception e);

}
