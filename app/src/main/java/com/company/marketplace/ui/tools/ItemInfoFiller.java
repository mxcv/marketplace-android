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
		for (Item item : items)
			if (item.getCategory() != null) {
				item.setCategory(categories.stream()
					.filter(c -> c.getId() == item.getCategory().getId())
					.findFirst()
					.orElse(item.getCategory()));
			}

		return this;
	}

	public ItemInfoFiller fillCurrencies(List<Currency> currencies) {
		for (Item item : items)
			if (item.getCurrency() != null) {
				item.setCurrency(currencies.stream()
					.filter(c -> c.getId() == item.getCurrency().getId())
					.findFirst()
					.orElse(item.getCurrency()));
			}

		return this;
	}

	public ItemInfoFiller fillCities(List<Country> countries) {
		for (Item item : items)
			if (item.getUser() != null && item.getUser().getCity() != null) {
				item.getUser().setCity(countries.stream()
					.flatMap(c -> c.getRegions().stream())
					.flatMap(r -> r.getCities().stream())
					.filter(c -> c.getId() == item.getUser().getCity().getId())
					.findFirst()
					.orElse(item.getUser().getCity()));
			}

		return this;
	}
}
