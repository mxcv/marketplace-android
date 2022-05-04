package com.company.marketplace.network.repositories;

import android.content.Context;
import android.widget.Toast;

import com.company.marketplace.R;
import com.company.marketplace.models.Account;

public class MarketplaceRepositoryFactory {

	private final Context context;

	public MarketplaceRepositoryFactory(Context context) {
		this.context = context;
	}

	public CategoryRepository createCategoryRepository() {
		return createMarketplaceRepository();
	}
	public CurrencyRepository createCurrencyRepository() {
		return createMarketplaceRepository();
	}
	public FeedbackRepository createFeedbackRepository() {
		return createMarketplaceRepository();
	}
	public ImageRepository createImageRepository() {
		return createMarketplaceRepository();
	}
	public ItemRepository createItemRepository() {
		return createMarketplaceRepository();
	}
	public LocationRepository createLocationRepository() {
		return createMarketplaceRepository();
	}
	public SortTypeRepository createSortTypeRepository() {
		return createMarketplaceRepository();
	}
	public UserRepository createUserRepository() {
		return createMarketplaceRepository();
	}

	private NetworkMarketplaceRepository createMarketplaceRepository() {
		return new NetworkMarketplaceRepository(
			context,
			() -> {
				createUserRepository().logout(null);
				Account.get().setUser(null);
			},
			() -> Toast.makeText(context, R.string.connection_error, Toast.LENGTH_LONG).show());
	}
}
