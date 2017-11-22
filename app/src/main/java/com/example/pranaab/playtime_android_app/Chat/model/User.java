package com.example.pranaab.playtime_android_app.Chat.model;

import com.stfalcon.chatkit.commons.models.IUser;

/**
 * Created by pranaab on 2017-11-20.
 */

public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private boolean online;
    private String uid;

    public User(String id, String name, String url, boolean io) {
        this.id = id;
        this.name = name;
        this.avatar = url;
        this.online = true;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }


    public boolean isOnline() {
        return online;
    }
}
