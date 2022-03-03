package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.City;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Region;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.adapters.AdapterWithNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private EditText nameEditText, phoneNumberEditText, emailEditText, passwordEditText, confirmPasswordEditText;
	private Spinner countrySpinner, regionSpinner, citySpinner;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_register, container, false);
		view.findViewById(R.id.register).setOnClickListener(this);
		nameEditText = view.findViewById(R.id.registerName);
		phoneNumberEditText = view.findViewById(R.id.registerPhoneNumber);
		emailEditText = view.findViewById(R.id.registerEmail);
		passwordEditText = view.findViewById(R.id.registerPassword);
		confirmPasswordEditText = view.findViewById(R.id.registerConfirmPassword);
		countrySpinner = view.findViewById(R.id.addItemCountry);
		regionSpinner = view.findViewById(R.id.addItemRegion);
		citySpinner = view.findViewById(R.id.addItemCity);

		countrySpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), new ArrayList<>()));
		regionSpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), new ArrayList<>()));
		citySpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), new ArrayList<>()));

		countrySpinner.setEnabled(false);
		regionSpinner.setEnabled(false);
		citySpinner.setEnabled(false);

		countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					regionSpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), new ArrayList<>()));
					regionSpinner.setEnabled(false);
				}
				else {
					regionSpinner.setAdapter(new AdapterWithNull<>(
						Objects.requireNonNull(getContext()),
						((Country)countrySpinner.getSelectedItem()).getRegions())
					);
					regionSpinner.setEnabled(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					citySpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), new ArrayList<>()));
					citySpinner.setEnabled(false);
				}
				else {
					citySpinner.setAdapter(new AdapterWithNull<>(
						Objects.requireNonNull(getContext()),
						((Region)regionSpinner.getSelectedItem()).getCities())
					);
					citySpinner.setEnabled(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		new MarketplaceRepositoryFactory().create(getActivity()).getCountries(countries -> {
			countrySpinner.setAdapter(new AdapterWithNull<>(Objects.requireNonNull(getContext()), countries));
			countrySpinner.setEnabled(true);
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString()))
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
		else {
			UserRepository userRepository = new MarketplaceRepositoryFactory().create(getActivity());
			userRepository.createUser(new User(
					emailEditText.getText().toString(),
					passwordEditText.getText().toString(),
					phoneNumberEditText.getText().toString(),
					nameEditText.getText().toString(),
					citySpinner.getSelectedItem() == null ? null : (City)citySpinner.getSelectedItem()
				),
				ignored -> Navigation.findNavController(v).navigate(R.id.nav_login),
				() -> Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_SHORT).show()
			);
		}
	}
}
