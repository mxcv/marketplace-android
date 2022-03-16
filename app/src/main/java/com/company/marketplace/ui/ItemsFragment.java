package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.tools.LocationSelector;
import com.company.marketplace.ui.tools.ObjectSelector;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ItemsFragment extends Fragment implements View.OnClickListener {

	private static final Integer[] sortTypeResources = new Integer[] {
		R.string.sort_newest,
		R.string.sort_cheapest,
		R.string.sort_most_expensive };

	private AutoCompleteTextView categoryView, currencyView, countryView, regionView, cityView, sortView;
	private RecyclerView itemsView;
	private ItemRepository itemRepository;
	private UserRepository userRepository;
	private LocationSelector locationSelector;
	private ObjectSelector<Category> categorySelector;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<String> sortTypeSelector;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_items, container, false);
		view.findViewById(R.id.displayOptionsApply).setOnClickListener(this);
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

		itemRepository.getCategories(categories ->
			categorySelector = new ObjectSelector<>(
				categoryView,
				R.string.not_selected,
				categories,
				Category::getTitle));

		itemRepository.getCurrencies(currencies ->
			currencySelector = new ObjectSelector<>(
				currencyView,
				R.string.default_option,
				currencies,
				Currency::toString));

		userRepository.getCountries(countries ->
			locationSelector = new LocationSelector(
				countryView,
				regionView,
				cityView,
				countries));

		sortTypeSelector = new ObjectSelector<>(sortView, null,
			Arrays.stream(sortTypeResources)
				.map(this::getString)
				.collect(Collectors.toList()),
			x -> x);

		onClick(view.findViewById(R.id.displayOptionsApply));
		return view;
	}

	@Override
	public void onClick(View v) {
		ItemRequest itemRequest = new ItemRequest();
		itemRepository.getItems(itemRequest, page -> {
			itemsView.setAdapter(new ItemAdapter(getContext(), page.getItems()));
		});
	}
}
