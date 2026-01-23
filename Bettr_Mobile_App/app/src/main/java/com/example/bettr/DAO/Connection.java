package com.example.bettr.Dao;

import android.util.Log;

import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
	String url;
	String usuario;
	String contraseña;
	public java.sql.Connection client;

	public Connection(String url, String usuario, String contraseña) {
		this.url = url;
		this.usuario = usuario;
		this.contraseña = contraseña;
	}

	public void init() {
		if (client == null) {
			try {
				client = DriverManager.getConnection(url, usuario, contraseña);
				Log.i("Conectado", "Conectado");
			} catch (SQLException e) {
				Log.i("NO Conectado", "NO Conectado " + e.getMessage());
			}
		}
	}

	public void stop() {
		try {
			client.close();
		} catch (SQLException e) {
		}
	}
}
