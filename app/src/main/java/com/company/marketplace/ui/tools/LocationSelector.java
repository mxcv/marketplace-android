package com.company.marketplace.ui.tools;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.company.marketplace.R;
import com.company.marketplace.models.City;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Region;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LocationSelector {

	private final AutoCompleteTextView countryTextView, regionTextView, cityTextView;
	private final List<Country> countries;

	public LocationSelector(AutoCompleteTextView countryTextView,
							AutoCompleteTextView regionTextView,
							AutoCompleteTextView cityTextView,
							List<Country> countries) {

		this.countryTextView = countryTextView;
		this.regionTextView = regionTextView;
		this.cityTextView = cityTextView;
		this.countries = countries;

		countryTextView.addTextChangedListener(new LocationClickListener(
			regionTextView,
			() -> countries.stream()
				.filter(c -> c.getName().contentEquals(countryTextView.getText()))
				.findFirst()
				.get()
				.getRegions()
				.stream()
				.map(Region::getName)
				.collect(Collectors.toList())));
		regionTextView.addTextChangedListener(new LocationClickListener(
			cityTextView,
			() -> countries.stream()
				.flatMap(c -> c.getRegions().stream())
				.filter(c -> c.getName().contentEquals(regionTextView.getText()))
				.findFirst()
				.get()
				.getCities()
				.stream()
				.map(City::getName)
				.collect(Collectors.toList())));

		setAdapterWithDefault(
			countryTextView,
			countries.stream()
				.map(Country::getName)
				.collect(Collectors.toList()));
	}

	public List<Country> getCountries() {
		return countries;
	}

	public Country getSelectedCountry() {
		return countries.stream()
			.filter(c -> countryTextView.getText().toString().equals(c.getName()))
			.findFirst()
			.orElse(null);
	}

	public Region getSelectedRegion() {
		return countries.stream()
			.flatMap(c -> c.getRegions().stream())
			.filter(r -> regionTextView.getText().toString().equals(r.getName()))
			.findFirst()
			.orElse(null);
	}

	public City getSelectedCity() {
		return countries.stream()
			.flatMap(c -> c.getRegions().stream())
			.flatMap(r -> r.getCities().stream())
			.filter(c -> cityTextView.getText().toString().equals(c.getName()))
			.findFirst()
			.orElse(null);
	}

	private static void setAdapterWithDefault(AutoCompleteTextView textView, List<String> strings) {
		strings.add(0, textView.getContext().getString(R.string.not_selected));
		textView.setAdapter(new ArrayAdapter<>(
			textView.getContext(),
			android.R.layout.simple_spinner_dropdown_item,
			new ArrayList<>(strings)));
		textView.setText(strings.get(0), false);
	}

	private static class LocationClickListener implements TextWatcher {

		private final AutoCompleteTextView nextTextView;
		private final Supplier<List<String>> nextItemsMapper;
		private final String defaultString;

		public LocationClickListener(AutoCompleteTextView nextTextView,
									 Supplier<List<String>> nextItemsMapper) {

			this.nextTextView = nextTextView;
			this.nextItemsMapper = nextItemsMapper;
			this.defaultString = nextTextView.getContext().getString(R.string.not_selected);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			setAdapterWithDefault(
				nextTextView,
				s.toString().equals(defaultString) ? new ArrayList<>() : nextItemsMapper.get());
		}
	}
}
