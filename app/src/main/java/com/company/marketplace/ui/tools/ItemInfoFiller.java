package com.company.marketplace.ui.tools;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;

import java.util.List;

public class ItemInfoFiller {

	private final List<Item> items;

	public ItemInfoFiller(List<Item> items) {
		this.items = items;
	}

	public List<Item> getItems() {
		return items;
	}

	public ItemInfoFiller fillCategories(List<Category> categories) {
		items.stream()
			.filter(i -> i.getCategory() != null)
			.forEach(i -> i.setCategory(
				categories.stream()
					.filter(c -> c.getId() == i.getCategory().getId())
					.findFirst()
					.orElse(i.getCategory())));

		return this;
	}

	public ItemInfoFiller fillCurrencies(List<Currency> currencies) {
		items.stream()
			.filter(i -> i.getCurrency() != null)
			.forEach(i -> i.setCurrency(
				currencies.stream()
					.filter(c -> c.getId() == i.getCurrency().getId())
					.findFirst()
					.orElse(i.getCurrency())));

		return this;
	}

	public ItemInfoFiller fillCities(List<Country> countries) {
		items.stream()
			.filter(i -> i.getUser() != null && i.getUser().getCity() != null)
			.forEach(i -> i.getUser().setCity(
				countries.stream()
					.flatMap(c -> c.getRegions().stream())
					.flatMap(r -> r.getCities().stream())
					.filter(c -> c.getId() == i.getUser().getCity().getId())
					.findFirst()
					.orElse(i.getUser().getCity())));

		return this;
	}
}
