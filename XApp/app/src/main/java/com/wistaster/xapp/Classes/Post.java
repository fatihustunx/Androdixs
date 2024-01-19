package com.wistaster.xapp.Classes;

import java.util.Date;

public class Post {

    private String id;
    private String userId;

    private String title;

    private String text;

    private Date creationTimes;

    public Post(){}

    public Post(String id,String userId, String title, String text,
                Date creationTimes) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.creationTimes = creationTimes;
    }

    public String getUserId() {
        return userId;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getCreationTimes() {
        return creationTimes;
    }

    public void setCreationTimes(Date creationTimes) {
        this.creationTimes = creationTimes;
    }
}