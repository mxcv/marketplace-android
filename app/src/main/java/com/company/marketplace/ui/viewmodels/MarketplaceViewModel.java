package com.company.marketplace.ui.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.SortType;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

import java.util.List;

public class MarketplaceViewModel extends AndroidViewModel {

	private MutableLiveData<List<Category>> categories;
	private MutableLiveData<List<Country>> countries;
	private MutableLiveData<List<Currency>> currencies;
	private MutableLiveData<List<SortType>> sortTypes;

	public MarketplaceViewModel(@NonNull Application application) {
		super(application);
	}

	public LiveData<List<Category>> getCategories() {
		if (categories == null) {
			categories = new MutableLiveData<>();
			new MarketplaceRepositoryFactory(getApplication())
				.createCategoryRepository()
				.getCategories(categories::setValue);
		}
		return categories;
	}

	public LiveData<List<Country>> getCountries() {
		if (countries == null) {
			countries = new MutableLiveData<>();
			new MarketplaceRepositoryFactory(getApplication())
				.createLocationRepository()
				.getCountries(countries::setValue);
		}
		return countries;
	}

	public LiveData<List<Currency>> getCurrencies() {
		if (currencies == null) {
			currencies = new MutableLiveData<>();
			new MarketplaceRepositoryFactory(getApplication())
				.createCurrencyRepository()
				.getCurrencies(currencies::setValue);
		}
		return currencies;
	}

	public LiveData<List<SortType>> getSortTypes() {
		if (sortTypes == null) {
			sortTypes = new MutableLiveData<>();
			new MarketplaceRepositoryFactory(getApplication())
				.createSortTypeRepository()
				.getSortTypes(sortTypes::setValue);
		}
		return sortTypes;
	}
}
