package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
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
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;

public class ItemsFragment extends Fragment implements View.OnClickListener {

	private ExpansionLayout expansionLayoutView;
	private EditText queryView, minPriceView, maxPriceView;
	private AutoCompleteTextView categoryView, currencyView, countryView, regionView, cityView, sortView;
	private RecyclerView itemsView;
	private ItemRepository itemRepository;
	private UserRepository userRepository;
	private LocationSelector locationSelector;
	private ObjectSelector<Category> categorySelector;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<SortType> sortTypeSelector;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_items, container, false);
		view.findViewById(R.id.displayOptionsApply).setOnClickListener(this);
		expansionLayoutView = view.findViewById(R.id.displayOptionsExpansionLayout);
		queryView = ((TextInputLayout)view.findViewById(R.id.displayOptionsQuery)).getEditText();
		minPriceView = ((TextInputLayout)view.findViewById(R.id.displayOptionsPriceMin)).getEditText();
		maxPriceView = ((TextInputLayout)view.findViewById(R.id.displayOptionsPriceMax)).getEditText();
		categoryView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsCategory)).getEditText();
		currencyView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsCurrency)).getEditText();
		countryView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsCountry)).getEditText();
		regionView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsRegion)).getEditText();
		cityView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsCity)).getEditText();
		sortView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsSort)).getEditText();
		sortView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.displayOptionsSort)).getEditText();
		itemsView = view.findViewById(R.id.itemsItems);

		itemRepository = new MarketplaceRepositoryFactory(getActivity()).createItemRepository();
		userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();

		itemRepository.getCategories(categories -> {
			synchronized (this) {
				categorySelector = new ObjectSelector<>(
					categoryView,
					R.string.not_selected,
					categories,
					Category::getTitle);
				notify();
			}
		});

		itemRepository.getCurrencies(currencies -> {
			synchronized (this) {
				currencySelector = new ObjectSelector<>(
					currencyView,
					R.string.default_option,
					currencies,
					Currency::toString);
				notify();
			}
		});

		userRepository.getCountries(countries -> {
			synchronized (this) {
				locationSelector = new LocationSelector(countryView, regionView, cityView, countries);
				notify();
			}
		});

		itemRepository.getSortTypes(sortTypes ->
			sortTypeSelector = new ObjectSelector<>(
				sortView,
				null,
				sortTypes,
				SortType::getName));

		getItems(new ItemRequest());
		return view;
	}

	@Override
	public void onClick(View v) {
		ItemRequest itemRequest = new ItemRequest();
		itemRequest.setQuery(queryView.getText().toString().equals("") ? null
			: queryView.getText().toString());
		itemRequest.setMinPrice(minPriceView.getText().toString().equals("") ? null
			: new BigDecimal(minPriceView.getText().toString()));
		itemRequest.setMaxPrice(maxPriceView.getText().toString().equals("") ? null
			: new BigDecimal(maxPriceView.getText().toString()));
		itemRequest.setCategory(categorySelector == null ? null : categorySelector.getSelectedObject());
		itemRequest.setCurrency(currencySelector == null ? null : currencySelector.getSelectedObject());
		itemRequest.setCountry(locationSelector == null ? null : locationSelector.getSelectedCountry());
		itemRequest.setRegion(locationSelector == null ? null : locationSelector.getSelectedRegion());
		itemRequest.setCity(locationSelector == null ? null : locationSelector.getSelectedCity());
		itemRequest.setSortType(sortTypeSelector == null ? null : sortTypeSelector.getSelectedObject());

		expansionLayoutView.collapse(true);
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
				requireActivity().runOnUiThread(() -> itemsView.setAdapter(new ItemAdapter(getContext(), page.getItems())));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start());
	}
}
