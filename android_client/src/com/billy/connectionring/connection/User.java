package com.billy.connectionring.connection;

import com.billy.connectionring.sharedpreference.SharedPreferenceSystem;

import android.content.Context;

public class User {

	private static User instance;
	
	private String id;
	private String pw;
	
	public static synchronized User getInstance() {
		if(instance == null) {
			// shared preference�κ��� �о����.
			String id = null;
			String password = null;
			instance = new User(id, password);
		}
		return instance;
	}
	
	private User(String id, String pw)
	{
		this.id = id;
		this.pw = pw;
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
}
