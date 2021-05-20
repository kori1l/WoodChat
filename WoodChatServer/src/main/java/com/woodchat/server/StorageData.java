package com.woodchat.server;

import com.woodchat.server.dataBase.MessagesTable;
import com.woodchat.server.dataBase.UsersTable;
import com.woodchat.connection.message.Message;
import com.woodchat.connection.message.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StorageData {
    public static Logger loggerDB = Logger.getLogger(StorageData.class.getName());
    public static final String DB_URL = "jdbc:h2:/c:/JavaH2DB/StorageDate";
    public static final String DB_Driver = "org.h2.Driver";
    MessagesTable messagesTable;
    UsersTable usersTable;

    public StorageData() {
        try {
            Class.forName(DB_Driver);
            loggerDB.log(Level.INFO,"Драйвер найден");
            usersTable = new UsersTable();
            messagesTable = new MessagesTable();
            createAllTables();
        } catch (ClassNotFoundException e) {
            loggerDB.log(Level.WARNING,"JDBC драйвер для СУБД не найден!"+e);
        } catch (SQLException e) {
            loggerDB.log(Level.WARNING,"Ошибка SQL !"+e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void createAllTables() {
        try {
            usersTable.createTable();
            messagesTable.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void saveUser(User newUser) {
        try {
            usersTable.setData(newUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedHashSet<User> getAllUsers() throws SQLException {
        return usersTable.getAllUsers();
    }

    public void saveMessage(Message message) {
        try {
            messagesTable.setData(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public LinkedList<Message> getAllMessages() throws SQLException {
        return messagesTable.getAllMessages();
    }
}
