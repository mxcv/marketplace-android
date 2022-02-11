package com.company.marketplace.account;

import android.app.Activity;

import com.company.marketplace.models.User;

public interface UserChangedListener {
	void onUserChanged(User user, Activity activity);
}
