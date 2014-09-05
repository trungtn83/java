package com.wai.seifan.dto;

public class UserInfo {
	String id;
	String username;
	String password;
	
	public UserInfo(String _username, String _password) {
		this.id = null;
		this.username = _username;
		this.password = _password;
	}
	
	public UserInfo(String _id, String _username, String _password) {
		this.id = _id;
		this.username = _username;
		this.password = _password;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
