package com.example.appmovil.Dao;

public class User {
    private int id;
    private String name;
    private String username;
    private String avatarUrl;
    private String email;
    private String description;
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
    public String getEmail() { return email; }
    public String getDescription() { return description; }
    
    public boolean isFollowing() { return isFollowing; }
    public void setFollowing(boolean following) { isFollowing = following; }
    
    public void setEmail(String email) { this.email = email; }
    public void setDescription(String description) { this.description = description; }
}
