package com.company.marketplace.account;

import com.company.marketplace.models.User;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private static Account instance;
	private final List<UserChangedListener> userChangedListeners;
	private User user;

	private Account () {
		userChangedListeners = new ArrayList<>();
	}

	public static synchronized Account getInstance(){
		if (instance == null)
			instance = new Account();
		return instance;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
		for (UserChangedListener listener : userChangedListeners)
			listener.onUserChanged(user);
	}

	public void addUserChangedListener(UserChangedListener listener) {
		userChangedListeners.add(listener);
	}
	public void removeUserChangedListener(UserChangedListener listener) {
		userChangedListeners.remove(listener);
	}
}
