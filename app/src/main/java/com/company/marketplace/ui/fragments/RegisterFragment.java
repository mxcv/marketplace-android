package com.company.marketplace.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentRegisterBinding;
import com.company.marketplace.models.City;
import com.company.marketplace.models.User;
import com.company.marketplace.ui.tools.LocationSelector;
import com.company.marketplace.ui.viewmodels.MarketplaceViewModel;
import com.company.marketplace.ui.viewmodels.RegisterViewModel;

import java.util.Objects;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private FragmentRegisterBinding binding;
	private RegisterViewModel registerViewModel;
	private LocationSelector locationSelector;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentRegisterBinding.inflate(inflater, container, false);
		binding.registerRegister.setOnClickListener(this);

		registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
		registerViewModel.getUserId().observe(getViewLifecycleOwner(), userId ->
			Navigation.findNavController(binding.getRoot()).navigate(R.id.nav_login));

		new ViewModelProvider(requireActivity())
			.get(MarketplaceViewModel.class)
			.getCountries()
			.observe(getViewLifecycleOwner(), countries ->
				locationSelector = new LocationSelector(
					binding.registerCountry,
					binding.registerRegion,
					binding.registerCity,
					countries));

		return binding.getRoot();
	}

	@Override
	public void onClick(View v) {
		if (!Objects.requireNonNull(binding.registerPassword.getText()).toString().equals(
			Objects.requireNonNull(binding.registerConfirmPassword.getText()).toString())) {
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
			return;
		}

		City city = locationSelector.getSelectedCity();
		if (city != null)
			city.setRegion(null);

		registerViewModel.register(new User(
			Objects.requireNonNull(binding.registerEmail.getText()).toString(),
			Objects.requireNonNull(binding.registerPassword.getText()).toString(),
			Objects.requireNonNull(binding.registerPhoneNumber.getText()).toString(),
			Objects.requireNonNull(binding.registerName.getText()).toString(),
			city));
	}
}
