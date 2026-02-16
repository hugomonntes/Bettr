package com.example.bettr.Dao;

public class User {
    private int id;
    private String name;
    private String username;
    private String avatarUrl;
    private boolean isFollowing;

    public User(int id, String name, String username, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.avatarUrl = avatarUrl;
        this.isFollowing = false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getUsername() { return username; }
    public String getAvatarUrl() { return avatarUrl; }
    
    public boolean isFollowing() { return isFollowing; }
    public void setFollowing(boolean following) { isFollowing = following; }
}
