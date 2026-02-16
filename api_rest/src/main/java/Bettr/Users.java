package Bettr;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;

public class Users implements Serializable {
    private int id;
    private String name;
    private String username;
    private String email;
    private String password_hash;
    private String description;
    private String avatar;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Users() {
    }

    public Users(String name, String username, String email, String password_hash) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.created_at = LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public String getDescription() {
        return description;
    }

    public String getAvatar() {
        return avatar;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword_hash(String password_hash) {
        this.password_hash = password_hash;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    // Method to hash password
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Method to verify password
    public boolean verifyPassword(String plainPassword) {
        return BCrypt.checkpw(plainPassword, this.password_hash);
    }
}
