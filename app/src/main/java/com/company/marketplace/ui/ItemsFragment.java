package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentItemsBinding;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.SortType;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.tools.LocationSelector;
import com.company.marketplace.ui.tools.ObjectSelector;

import java.math.BigDecimal;
import java.util.Objects;

public class ItemsFragment extends Fragment implements View.OnClickListener {

	private FragmentItemsBinding binding;
	private ItemRepository itemRepository;
	private UserRepository userRepository;
	private LocationSelector locationSelector;
	private ObjectSelector<Category> categorySelector;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<SortType> sortTypeSelector;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemsBinding.inflate(inflater, container, false);

		binding.itemsDisplayOptions.displayOptionsApply.setOnClickListener(this);
		itemRepository = new MarketplaceRepositoryFactory(getActivity()).createItemRepository();
		userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();

		itemRepository.getCategories(categories -> {
			synchronized (this) {
				categorySelector = new ObjectSelector<>(
					(AutoCompleteTextView)binding.itemsDisplayOptions.displayOptionsCategory.getEditText(),
					R.string.not_selected,
					categories,
					Category::getTitle);
				notify();
			}
		});

		itemRepository.getCurrencies(currencies -> {
			synchronized (this) {
				currencySelector = new ObjectSelector<>(
					(AutoCompleteTextView)binding.itemsDisplayOptions.displayOptionsCurrency.getEditText(),
					R.string.default_option,
					currencies,
					Currency::getSymbol);
				notify();
			}
		});

		userRepository.getCountries(countries -> {
			synchronized (this) {
				locationSelector = new LocationSelector(
					(AutoCompleteTextView)Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsCountry.getEditText()),
					(AutoCompleteTextView)Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsRegion.getEditText()),
					(AutoCompleteTextView)Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsCity.getEditText()),
					countries);
				notify();
			}
		});

		itemRepository.getSortTypes(sortTypes ->
			sortTypeSelector = new ObjectSelector<>(
				(AutoCompleteTextView)binding.itemsDisplayOptions.displayOptionsSort.getEditText(),
				null,
				sortTypes,
				SortType::getName));

		getItems(new ItemRequest());
		return binding.getRoot();
	}

	@Override
	public void onClick(View v) {
		String query = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsQuery.getEditText()).getText().toString();
		String minPrice = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsPriceMin.getEditText()).getText().toString();
		String maxPrice = Objects.requireNonNull(binding.itemsDisplayOptions.displayOptionsPriceMax.getEditText()).getText().toString();

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
