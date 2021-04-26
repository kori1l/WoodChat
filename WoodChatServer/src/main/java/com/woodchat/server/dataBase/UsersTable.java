package com.woodchat.server.dataBase;

import com.woodchat.server.message.User;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class UsersTable extends DataTable implements Operations {
    public UsersTable() throws SQLException {
        super("users");
    }

    @Override
    public void createTable() throws SQLException {
        super.executeStatement("CREATE TABLE IF NOT EXISTS users(" +
                "uid BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "user_name VARCHAR(30)," +
                "r_color INTEGER," +
                "g_color INTEGER," +
                "b_color INTEGER)");
        System.out.println("таблица users создана");

    }


    public LinkedHashSet<User> getData(String sql) throws SQLException {
        super.reopenConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        LinkedHashSet<User> userSet = new LinkedHashSet<>();
        while (resultSet.next()) {
            Color color = new Color(resultSet.getInt(3), resultSet.getInt(4), resultSet.getInt(5));
            userSet.add(new User(resultSet.getLong(1), resultSet.getString(2), color));
        }
        return userSet;
    }


    public void setData(User user) throws SQLException {
        LinkedHashSet<User> userSet = getData("Select * From users");
        Iterator<User> iterator = userSet.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            if (!iterator.next().equals(user)) {
                i += 1;
            }
        }
        if (userSet.size() == i) {
            super.reopenConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO users VALUES(NULL,'" + user.getUserName() + "'," + user.getColor().getRed() + "," + user.getColor().getGreen() + "," + user.getColor().getBlue() + ")");
        }
    }

    public LinkedHashSet<User> getAllUsers() throws SQLException {
        return getData("Select * From users");
    }

    public User getOneUser(Long uid) throws SQLException {
        Iterator<User> iterator = getData("Select * From users where uid = " + uid).iterator();
        return iterator.next();
    }

}
