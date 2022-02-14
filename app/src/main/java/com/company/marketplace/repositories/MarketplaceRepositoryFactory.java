package com.company.marketplace.repositories;

import android.app.Activity;
import android.widget.Toast;

import com.company.marketplace.R;
import com.company.marketplace.account.Account;

public class MarketplaceRepositoryFactory {

	public MarketplaceRepository create(Activity activity) {
		return new MarketplaceRepository(activity, () -> {
			create(activity).logout();
			Account.getInstance().setUser(null, activity);
		}, () -> {
			Toast.makeText(activity, R.string.connection_error, Toast.LENGTH_LONG).show();
		});
	}
}
