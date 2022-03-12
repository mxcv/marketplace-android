package com.company.marketplace.account;

import android.app.Activity;

import com.company.marketplace.models.User;

import java.util.ArrayList;
import java.util.List;

public class Account {

	private static Account instance;
	private final List<UserChangedListener> userChangedListeners;
	private User user;

	private Account () {
		userChangedListeners = new ArrayList<>();
		user = null;
	}

	public static synchronized Account get(){
		if (instance == null)
			instance = new Account();
		return instance;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user, Activity activity) {
		this.user = user;
		for (UserChangedListener listener : userChangedListeners)
			listener.onUserChanged(user, activity);
	}

	public void addUserChangedListener(UserChangedListener listener) {
		userChangedListeners.add(listener);
	}
	public void removeUserChangedListener(UserChangedListener listener) {
		userChangedListeners.remove(listener);
	}
}
