package com.company.marketplace.models;

public class User {

	public String email;
	public String password;
	public String phoneNumber;
	public String name;

	public User() { }

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String email, String password, String phoneNumber, String name) {
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.name = name;
	}
}
