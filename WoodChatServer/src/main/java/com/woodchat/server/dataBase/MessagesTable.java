package com.woodchat.server.dataBase;

import com.woodchat.server.message.Message;
import com.woodchat.server.message.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Date;
import java.util.LinkedList;

public class MessagesTable extends DataTable implements Operations {
    public MessagesTable() throws SQLException {
        super("messages");
    }

    @Override
    public void createTable() throws SQLException {
        super.executeStatement("CREATE TABLE IF NOT EXISTS messages(" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "time BIGINT NOT NULL," +
                "text VARCHAR(2000) NOT NULL," +
                "user BIGINT NOT NULL," +
                "is_changed BIT)");
        System.out.println("таблица messages создана");
        super.executeStatement("ALTER TABLE messages ADD FOREIGN KEY (user) REFERENCES users(uid)");
        System.out.println("созданы связи");
    }


    public LinkedList<Message> getData(String sql) throws SQLException {
        super.reopenConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        LinkedList<Message> messageList = new LinkedList<>();
        while (resultSet.next()) {
            User user = new UsersTable().getOneUser(resultSet.getLong(4));
            Date date = new Date(resultSet.getLong(2));
            messageList.add(new Message(resultSet.getString(3), resultSet.getLong(1), date, user));
        }
        return messageList;
    }


    public void setData(Message message) throws SQLException {
        super.reopenConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO MESSAGES VALUES(NULL," + (Long) message.getTimeMessage().getTime() + ",'" + message.getText() + "'," + message.getUser().getUserId() + "," + false + ")");

    }

    public LinkedList<Message> getAllMessages() throws SQLException {
        return getData("Select * From messages");
    }

}
