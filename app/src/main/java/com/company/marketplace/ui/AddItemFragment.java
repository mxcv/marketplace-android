package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.network.repositories.ItemRepository;
import com.company.marketplace.network.repositories.MarketplaceRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.ui.adapters.AdapterWithNull;

import java.math.BigDecimal;
import java.util.Objects;

public class AddItemFragment extends Fragment implements View.OnClickListener {

	private EditText titleEditText, priceEditText, descriptionEditText;
	private Spinner currencySpinner, categorySpinner;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_add_item, container, false);
		view.findViewById(R.id.addItem).setOnClickListener(this);
		titleEditText = view.findViewById(R.id.addItemTitle);
		priceEditText = view.findViewById(R.id.addItemPrice);
		descriptionEditText = view.findViewById(R.id.addItemDescription);
		currencySpinner = view.findViewById(R.id.addItemCurrency);
		categorySpinner = view.findViewById(R.id.addItemCategory);

		ItemRepository itemRepository = new MarketplaceRepositoryFactory().create(getActivity());
		itemRepository.getCurrencies(currencies -> {
				ArrayAdapter<Currency> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, currencies);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				currencySpinner.setAdapter(adapter);
			});
		itemRepository.getCategories(categories -> categorySpinner.setAdapter(new AdapterWithNull<>(getContext(), categories)));
		return view;
	}

	@Override
	public void onClick(View v) {
		BigDecimal price = new BigDecimal(priceEditText.getText().toString());
		if (titleEditText.getText().toString().trim().isEmpty()) {
			Toast.makeText(getContext(), R.string.title_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (price.compareTo(new BigDecimal(0)) < 0 || price.compareTo(new BigDecimal("999999999.99")) > 0) {
			Toast.makeText(getContext(), R.string.price_range, Toast.LENGTH_SHORT).show();
			return;
		}

		new MarketplaceRepositoryFactory().create(getActivity())
			.addItem(new Item(
					titleEditText.getText().toString(),
					descriptionEditText.getText().toString(),
					price,
					(Currency) currencySpinner.getSelectedItem(),
					(Category) categorySpinner.getSelectedItem()
				),ignored -> Navigation.findNavController(
						Objects.requireNonNull(getActivity()),
						R.id.nav_host_fragment_content_main
					).navigate(R.id.nav_my_items),
				() -> Toast.makeText(getContext(), R.string.add_item_error, Toast.LENGTH_SHORT).show()
			);
	}
}
