package Bettr;

import java.io.Serializable;

import org.mindrot.jbcrypt.BCrypt;

public class Users implements Serializable{
    String name;
    String username;
    String email;
    String password_hash;

    public Users() {
    }

    public Users(String name, String username, String email, String password_hash) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
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
        BCrypt bCrypt = new BCrypt();
        return bCrypt.hashpw(password_hash, bCrypt.gensalt());
    }
}
