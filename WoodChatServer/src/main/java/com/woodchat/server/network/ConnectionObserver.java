package com.woodchat.server.network;

import com.woodchat.server.message.Message;

public interface ConnectionObserver {

    void onConnectionReady(SocketConnection socketConnection);

    void onReceiveMessage(SocketConnection socketConnection, Message message);

    void onDisconnect(SocketConnection socketConnection);

    void onExpression(SocketConnection socketConnection, Exception e);

}
