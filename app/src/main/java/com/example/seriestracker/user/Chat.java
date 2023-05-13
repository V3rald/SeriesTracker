package com.example.seriestracker.user;

import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Chat {
    public String user;
    public String friend;
    public String text;
    public String date;

    public Chat(String user, String friend, String text) {
        this.user = user;
        this.friend = friend;
        this.text = text;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }
}
