package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentRegisterBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.LocationRepository;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.tools.LocationSelector;

import java.util.Objects;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private FragmentRegisterBinding binding;
	private LocationSelector locationSelector;
	private LocationRepository locationRepository;
	private UserRepository userRepository;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentRegisterBinding.inflate(inflater, container, false);
		binding.registerRegister.setOnClickListener(this);

		locationRepository = new MarketplaceRepositoryFactory(getContext()).createLocationRepository();
		userRepository = new MarketplaceRepositoryFactory(getContext()).createUserRepository();
		locationRepository.getCountries(countries ->
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
			Objects.requireNonNull(binding.registerConfirmPassword.getText()).toString()))
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
		else {
			userRepository.addUser(
				new User(
					Objects.requireNonNull(binding.registerEmail.getText()).toString(),
					Objects.requireNonNull(binding.registerPassword.getText()).toString(),
					Objects.requireNonNull(binding.registerPhoneNumber.getText()).toString(),
					Objects.requireNonNull(binding.registerName.getText()).toString(),
					locationSelector.getSelectedCity()
				),
				ignored -> Navigation.findNavController(v).navigate(R.id.nav_login),
				() -> Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_SHORT).show()
			);
		}
	}
}
