package com.woodchat.connection.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Message implements Serializable {
    private String text;
    private long id;
    private boolean flagNeedDelete;
    private boolean flagNeedChange;
    private boolean flagChanged;
    private Date timeMessage = new Date();
    private User user = new User();


    public Message() {
        this.text = "System";
    }

    public Message(String text) {

        this.text = text;
        this.user = new User();
    }

    public Message(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public Message(String text, long id, Date timeMessage, User user) {
        this.text = text;
        this.id = id;
        this.timeMessage = timeMessage;
        this.user = user;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Date getTimeMessage() {
        return timeMessage;
    }
    public void setTimeMessage(Date timeMessage) {
        this.timeMessage = timeMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return id == message.id && flagChanged == message.flagChanged && text.equals(message.text) && Objects.equals(timeMessage, message.timeMessage) && user.equals(message.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, id, flagChanged, timeMessage, user);
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", id=" + id +
                ", flagNeedDelete=" + flagNeedDelete +
                ", flagNeedChange=" + flagNeedChange +
                ", flagChanged=" + flagChanged +
                ", timeMessage=" + timeMessage +
                ", user=" + user +
                '}';
    }
}
