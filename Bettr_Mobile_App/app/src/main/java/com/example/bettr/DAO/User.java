package com.example.bettr.Dao;

import android.media.Image;

public class User {
	private int id; // Autoincrementado no hace falta setter
	private String name;
	private String username;
	private String email;
	private String password;
	private Image avatar; // Subir avatar dsps del registro si el usuario quiere por eso no lo meto en el constructor
	public int getId() {
		return id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}

	public Image getAvatar() {
		return avatar;
	}

	public User(String name, String username, String email, String password){
		setName(name);
		setUsername(username);
		setEmail(email);
		setPassword(password);
	}
}
