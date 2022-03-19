package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentItemsBinding;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.SortType;
import com.company.marketplace.network.repositories.CategoryRepository;
import com.company.marketplace.network.repositories.CurrencyRepository;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.LocationRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.SortTypeRepository;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.tools.LocationSelector;
import com.company.marketplace.ui.tools.ObjectSelector;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemsFragment extends Fragment implements View.OnClickListener {

	private FragmentItemsBinding binding;
	private CategoryRepository categoryRepository;
	private CurrencyRepository currencyRepository;
	private ItemRepository itemRepository;
	private LocationRepository locationRepository;
	private SortTypeRepository sortTypeRepository;
	private LocationSelector locationSelector;
	private ObjectSelector<Category> categorySelector;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<SortType> sortTypeSelector;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemsBinding.inflate(inflater, container, false);
		binding.itemsDisplayOptions.displayOptionsApply.setOnClickListener(this);

		categoryRepository = new MarketplaceRepositoryFactory(getContext()).createCategoryRepository();
		currencyRepository = new MarketplaceRepositoryFactory(getContext()).createCurrencyRepository();
		itemRepository = new MarketplaceRepositoryFactory(getContext()).createItemRepository();
		locationRepository = new MarketplaceRepositoryFactory(getContext()).createLocationRepository();
		sortTypeRepository = new MarketplaceRepositoryFactory(getContext()).createSortTypeRepository();

		categoryRepository.getCategories(categories -> {
			synchronized (this) {
				categorySelector = new ObjectSelector<>(
					binding.itemsDisplayOptions.displayOptionsCategory,
					R.string.not_selected,
					categories,
					Category::getTitle);
				notify();
			}
		});

		currencyRepository.getCurrencies(currencies -> {
			synchronized (this) {
				currencySelector = new ObjectSelector<>(
					binding.itemsDisplayOptions.displayOptionsCurrency,
					R.string.default_option,
					currencies,
					Currency::getSymbol);
				notify();
			}
		});

		locationRepository.getCountries(countries -> {
			synchronized (this) {
				locationSelector = new LocationSelector(
					binding.itemsDisplayOptions.displayOptionsCountry,
					binding.itemsDisplayOptions.displayOptionsRegion,
					binding.itemsDisplayOptions.displayOptionsCity,
					countries);
				notify();
			}
		});

		sortTypeRepository.getSortTypes(sortTypes ->
			sortTypeSelector = new ObjectSelector<>(
				binding.itemsDisplayOptions.displayOptionsSort,
				null,
				sortTypes,
				SortType::getName));

		getItems(new ItemRequest());
		return binding.getRoot();
	}

	@Override
	public void onClick(View v) {
		String query = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsQuery.getText()).toString();
		String minPrice = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsPriceMin.getText()).toString();
		String maxPrice = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsPriceMax.getText()).toString();

		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setQuery(query.equals("") ? null : query);
		itemRequest.setMinPrice(minPrice.equals("") ? null : new BigDecimal(minPrice));
		itemRequest.setMaxPrice(maxPrice.equals("") ? null : new BigDecimal(maxPrice));
		itemRequest.setCategory(categorySelector == null ? null : categorySelector.getSelectedObject());
		itemRequest.setCurrency(currencySelector == null ? null : currencySelector.getSelectedObject());
		itemRequest.setCountry(locationSelector == null ? null : locationSelector.getSelectedCountry());
		itemRequest.setRegion(locationSelector == null ? null : locationSelector.getSelectedRegion());
		itemRequest.setCity(locationSelector == null ? null : locationSelector.getSelectedCity());
		itemRequest.setSortType(sortTypeSelector == null ? null : sortTypeSelector.getSelectedObject());

		binding.itemsDisplayOptions.displayOptionsExpansionLayout.collapse(true);
		getItems(itemRequest);
	}

	private void getItems(ItemRequest itemRequest) {
		itemRepository.getItems(itemRequest, page -> new Thread(() -> {
			try {
				synchronized (this) {
					while (currencySelector == null
						|| categorySelector == null
						|| locationSelector == null)
						wait();
				}

				for (Item item : page.getItems()) {
					if (item.getCurrency() != null)
						item.setCurrency(currencySelector
							.getObjects()
							.stream()
							.filter(c -> c.getId() == item.getCurrency().getId())
							.findFirst()
							.orElse(item.getCurrency()));

					if (item.getCategory() != null)
						item.setCategory(categorySelector
							.getObjects()
							.stream()
							.filter(c -> c.getId() == item.getCategory().getId())
							.findFirst()
							.orElse(item.getCategory()));

					if (item.getUser().getCity() != null)
						item.getUser().setCity(locationSelector
							.getCountries()
							.stream()
							.flatMap(c -> c.getRegions().stream())
							.flatMap(r -> r.getCities().stream())
							.filter(c -> c.getId() == item.getUser().getCity().getId())
							.findFirst()
							.orElse(item.getUser().getCity()));
				}
				requireActivity().runOnUiThread(() -> {
					binding.itemsFoundValue.setText(String.valueOf(page.getItems().size() + page.getLeftCount()));
					binding.itemsItems.setAdapter(new ItemAdapter(getContext(), page.getItems()));
				});
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start());
	}
}
