package com.wistaster.xapp.Classes;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "Posts")
public class Post {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(foreign = true,
            foreignAutoRefresh = true)
    private User user;
    @DatabaseField
    private String title;
    @DatabaseField
    private String text;

    public Post(){}

    public Post(User user, String title, String text) {
        this.user = user;
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}