package com.company.marketplace.ui.tools;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.ui.viewmodels.MarketplaceViewModel;

import java.util.List;
import java.util.function.Consumer;

public class ItemInfoFiller {

	private final Fragment fragment;
	private List<Category> categories;
	private List<Currency> currencies;
	private List<Country> countries;

	public ItemInfoFiller(Fragment fragment) {
		this.fragment = fragment;
	}

	public void fill(List<Item> items, Consumer<List<Item>> consumer) {
		if (categories == null || currencies == null || countries == null)
			load();
		new Thread(() -> {
			try {
				synchronized (this) {
					while (categories == null || currencies == null || countries == null)
						wait();
				}
				fillCategories(items);
				fillCurrencies(items);
				fillCities(items);
				fragment.requireActivity().runOnUiThread(() -> consumer.accept(items));
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
	}

	private void load() {
		MarketplaceViewModel marketplaceViewModel = new ViewModelProvider(fragment.requireActivity())
			.get(MarketplaceViewModel.class);
		marketplaceViewModel.getCategories().observe(fragment.getViewLifecycleOwner(), categories -> {
			synchronized (this) {
				this.categories = categories;
				notify();
			}
		});
		marketplaceViewModel.getCurrencies().observe(fragment.getViewLifecycleOwner(), currencies -> {
			synchronized (this) {
				this.currencies = currencies;
				notify();
			}
		});
		marketplaceViewModel.getCountries().observe(fragment.getViewLifecycleOwner(), countries -> {
			synchronized (this) {
				this.countries = countries;
				notify();
			}
		});
	}

	private void fillCategories(List<Item> items) {
		items.stream()
			.filter(i -> i.getCategory() != null)
			.forEach(i -> i.setCategory(
				categories.stream()
					.filter(c -> c.getId() == i.getCategory().getId())
					.findFirst()
					.orElse(i.getCategory())));
	}

	private void fillCurrencies(List<Item> items) {
		items.stream()
			.filter(i -> i.getCurrency() != null)
			.forEach(i -> i.setCurrency(
				currencies.stream()
					.filter(c -> c.getId() == i.getCurrency().getId())
					.findFirst()
					.orElse(i.getCurrency())));
	}

	private void fillCities(List<Item> items) {
		items.stream()
			.filter(i -> i.getUser() != null && i.getUser().getCity() != null)
			.forEach(i -> i.getUser().setCity(
				countries.stream()
					.flatMap(c -> c.getRegions().stream())
					.flatMap(r -> r.getCities().stream())
					.filter(c -> c.getId() == i.getUser().getCity().getId())
					.findFirst()
					.orElse(i.getUser().getCity())));
	}
}
