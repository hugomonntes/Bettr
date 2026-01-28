package Bettr;

import java.io.Serializable;

public class Users implements Serializable{
    String name;
    String username;
    String email;
    String password_hash;

    public Users() {
    }

    public Users(String name, String username, String email, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password_hash = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password_hash = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getPassword() {
        return password_hash;
    }
}
