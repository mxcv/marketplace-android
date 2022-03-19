package com.company.marketplace.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Account {

	private static Account instance;
	private final MutableLiveData<User> user;

	private Account () {
		user = new MutableLiveData<>();
	}

	public static synchronized Account get(){
		if (instance == null)
			instance = new Account();
		return instance;
	}

	public LiveData<User> getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user.setValue(user);
	}
}
