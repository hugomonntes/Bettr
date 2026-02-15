package Bettr;

import java.io.Serializable;

public class Habit implements Serializable {
    private int id;
    private int user_id;
    private String username;
    private String description;
    private String image_url;
    private String habit_type;
    private int likes_count;
    private int streak_count;
    private String created_at;

    public Habit() {
    }

    public Habit(int user_id, String description, String image_url, String habit_type) {
        this.user_id = user_id;
        this.description = description;
        this.image_url = image_url;
        this.habit_type = habit_type;
        this.likes_count = 0;
        this.streak_count = 0;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getHabit_type() {
        return habit_type;
    }

    public int getLikes_count() {
        return likes_count;
    }

    public int getStreak_count() {
        return streak_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setHabit_type(String habit_type) {
        this.habit_type = habit_type;
    }

    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }

    public void setStreak_count(int streak_count) {
        this.streak_count = streak_count;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

