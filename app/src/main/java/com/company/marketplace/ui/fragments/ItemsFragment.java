package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentItemsBinding;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.SortType;
import com.company.marketplace.ui.adapters.ItemAdapter;
import com.company.marketplace.ui.tools.ItemInfoFiller;
import com.company.marketplace.ui.tools.LocationSelector;
import com.company.marketplace.ui.tools.ObjectSelector;
import com.company.marketplace.ui.viewmodels.ItemsViewModel;
import com.company.marketplace.ui.viewmodels.MarketplaceViewModel;
import com.company.marketplace.ui.viewmodels.SelectedItemViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemsFragment extends Fragment implements View.OnClickListener {

	private FragmentItemsBinding binding;
	private ItemsViewModel itemsViewModel;
	private ObjectSelector<Category> categorySelector;
	private ObjectSelector<Currency> currencySelector;
	private ObjectSelector<SortType> sortTypeSelector;
	private LocationSelector locationSelector;
	private List<Item> items;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentItemsBinding.inflate(inflater, container, false);
		binding.itemsDisplayOptions.displayOptionsApply.setOnClickListener(this);

		getViewModelStore().clear();
		itemsViewModel = new ViewModelProvider(this).get(ItemsViewModel.class);

		setItemsView();
		loadSelectors();

		return binding.getRoot();
	}

	@Override
	public void onClick(View v) {
		Objects.requireNonNull(binding.itemsItems.getAdapter()).notifyItemRangeRemoved(0, items.size());
		items.clear();

		itemsViewModel.clearItems();
		itemsViewModel.loadNextPage(getItemRequest());
		binding.itemsDisplayOptions.displayOptionsExpansionLayout.collapse(true);
	}

	private void setItemsView()
	{
		items = new ArrayList<>();
		binding.itemsItems.setAdapter(new ItemAdapter(getContext(), items, item -> {
			new ViewModelProvider(requireActivity())
				.get(SelectedItemViewModel.class)
				.select(item);
			Navigation.findNavController(binding.getRoot())
				.navigate(R.id.action_items_to_item);
		}));
		binding.itemsItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				LinearLayoutManager llm = (LinearLayoutManager)Objects.requireNonNull(binding.itemsItems.getLayoutManager());
				if (llm.findLastVisibleItemPosition() == items.size() - 1)
					itemsViewModel.loadNextPage(getItemRequest());
			}
		});
		itemsViewModel.getLastPage().observe(getViewLifecycleOwner(), page -> {
			if (page != null) {
				binding.itemsFound.setText(getString(R.string.found_count, page.getTotalCount()));
				addItems(page.getItems());
			}
		});

		if (itemsViewModel.getItems().getValue() == null)
			itemsViewModel.loadNextPage(getItemRequest());
		else
			addItems(itemsViewModel.getItems().getValue());
	}

	private void addItems(List<Item> newItems)
	{
		new ItemInfoFiller(this).fill(newItems, filledNewItems -> {
			int start = items.size();
			items.addAll(filledNewItems);
			Objects.requireNonNull(binding.itemsItems.getAdapter()).notifyItemRangeInserted(start, filledNewItems.size());
		});
	}

	private ItemRequest getItemRequest() {
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

		return itemRequest;
	}

	private void loadSelectors() {
		MarketplaceViewModel marketplaceViewModel = new ViewModelProvider(requireActivity())
			.get(MarketplaceViewModel.class);

		marketplaceViewModel.getCategories().observe(getViewLifecycleOwner(), categories ->
			categorySelector = new ObjectSelector<>(
				binding.itemsDisplayOptions.displayOptionsCategory,
				R.string.not_selected,
				categories,
				Category::getTitle));

		marketplaceViewModel.getCurrencies().observe(getViewLifecycleOwner(), currencies ->
			currencySelector = new ObjectSelector<>(
				binding.itemsDisplayOptions.displayOptionsCurrency,
				R.string.default_option,
				currencies,
				Currency::getSymbol));

		marketplaceViewModel.getCountries().observe(getViewLifecycleOwner(), countries ->
			locationSelector = new LocationSelector(
				binding.itemsDisplayOptions.displayOptionsCountry,
				binding.itemsDisplayOptions.displayOptionsRegion,
				binding.itemsDisplayOptions.displayOptionsCity,
				countries));

		marketplaceViewModel.getSortTypes().observe(getViewLifecycleOwner(), sortTypes ->
			sortTypeSelector = new ObjectSelector<>(
				binding.itemsDisplayOptions.displayOptionsSort,
				null,
				sortTypes,
				SortType::getName));
	}
}
