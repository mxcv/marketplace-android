package com.company.marketplace.account;

import com.company.marketplace.models.User;

public interface UserChangedListener {
	void onUserChanged(User user);
}
