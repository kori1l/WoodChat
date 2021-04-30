package com.woodchat.connection.message;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private long userId;
    private String userName;
    private Color color = Color.WHITE;

    public User() {
        this.userName = "System";
    }


    public User(String userName) {
        this.userName = userName;
    }

    public User(String userName, Color color) {
        this.userName = userName;
        this.color = color;
    }

    public User(long userId, String userName, Color color) {
        this.userId = userId;
        this.userName = userName;
        this.color = color;
    }

    public long getUserId() {
        return userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", color=" + color +
                '}';
    }


}
