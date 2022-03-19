package com.company.marketplace.network.repositories;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.Account;

public class MarketplaceRepositoryFactory {

	private final Context context;

	public MarketplaceRepositoryFactory(Context context) {
		this.context = context;
	}

	public UserRepository createUserRepository() {
		return createMarketplaceRepository();
	}

	public ItemRepository createItemRepository() {
		return createMarketplaceRepository();
	}

	private MarketplaceRepository createMarketplaceRepository() {
		return new MarketplaceRepository(
			context,
			() -> {
				createUserRepository().logout();
				Account.get().setUser(null);
			},
			() -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show());
	}
}
