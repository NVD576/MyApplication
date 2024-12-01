package com.example.myapplication;

public class UserComment {
    private String user;
    private String text;
    private String date;

    public UserComment(String user, String text, String date) {
        this.user = user;
        this.text = text;
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
