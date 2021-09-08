package com.example.appnotes.modelnotification;

import java.io.Serializable;

public class MyTodo implements Serializable {
    private String key,title,date,describe;

    public MyTodo() {
    }

    public MyTodo(String key, String title, String describe, String date) {
        this.key = key;
        this.title = title;
        this.date = date;
        this.describe = describe;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
