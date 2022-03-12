package com.company.marketplace.network.repositories;

import android.app.Activity;
import android.widget.Toast;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;

public class MarketplaceRepositoryFactory {

	private final Activity activity;

	public MarketplaceRepositoryFactory(Activity activity) {
		this.activity = activity;
	}

	public UserRepository createUserRepository() {
		return createMarketplaceRepository();
	}

	public ItemRepository createItemRepository() {
		return createMarketplaceRepository();
	}

	private MarketplaceRepository createMarketplaceRepository() {
		return new MarketplaceRepository(() -> {
			createUserRepository().logout();
			Account.get().setUser(null, activity);
		},
		() -> Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show());
	}
}
