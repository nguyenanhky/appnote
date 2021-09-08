package com.example.appnotes.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "notes")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate =true)
    private int id;

    @ColumnInfo(name ="title")
    private String title;

    @ColumnInfo(name = "date_time")
    private String datetime;

    @ColumnInfo(name = "subtitle")
    private String subtile;

    @ColumnInfo(name = "note_text")
    private String notetext;

    @ColumnInfo(name = "img_path")
    private String imgpath;

    @ColumnInfo(name = "color")
    private String color;

    @ColumnInfo(name = "web_link")
    private String weblink;

    // get and set

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSubtile() {
        return subtile;
    }

    public void setSubtile(String subtile) {
        this.subtile = subtile;
    }

    public String getNotetext() {
        return notetext;
    }

    public void setNotetext(String notetext) {
        this.notetext = notetext;
    }

    public String getImgpath() {
        return imgpath;
    }
    // duong dan toi anh
    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeblink() {
        return weblink;
    }

    public void setWeblink(String weblink) {
        this.weblink = weblink;
    }

    @NonNull
    @Override
    public String toString() {
        return title+" : "+datetime;
    }
}
