package com.woodchat.server;

import com.woodchat.server.dataBase.MessagesTable;
import com.woodchat.server.dataBase.UsersTable;
import com.woodchat.server.message.Message;
import com.woodchat.server.message.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.LinkedHashSet;
import java.util.LinkedList;

public class StorageData {
    public static final String DB_URL = "jdbc:h2:/c:/JavaH2DB/StorageDate";
    public static final String DB_Driver = "org.h2.Driver";
    MessagesTable messagesTable;
    UsersTable usersTable;

    public StorageData() {
        try {
            Class.forName(DB_Driver);
            System.out.println("Драйвер найден");
            usersTable = new UsersTable();
            messagesTable = new MessagesTable();
            createAllTables();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL !");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void createAllTables() {
        try {
            usersTable.createTable();
            messagesTable.createTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void saveUser(User newUser) {
        try {
            usersTable.setData(newUser);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public LinkedHashSet<User> getAllUsers() throws SQLException {
        return usersTable.getAllUsers();
    }

    public void saveMessage(Message message) {
        try {
            messagesTable.setData(message);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public LinkedList<Message> getAllMessages() throws SQLException {
        return messagesTable.getAllMessages();
    }
}
