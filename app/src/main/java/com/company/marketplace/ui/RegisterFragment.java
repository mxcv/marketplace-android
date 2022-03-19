package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.databinding.FragmentRegisterBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.tools.LocationSelector;

import java.util.Objects;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private FragmentRegisterBinding binding;
	private LocationSelector locationSelector;
	private UserRepository userRepository;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentRegisterBinding.inflate(inflater, container, false);
		binding.registerRegister.setOnClickListener(this);

		userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();
		userRepository.getCountries(countries ->
			locationSelector = new LocationSelector(
				(AutoCompleteTextView)Objects.requireNonNull(binding.registerCountry.getEditText()),
				(AutoCompleteTextView)Objects.requireNonNull(binding.registerRegion.getEditText()),
				(AutoCompleteTextView)Objects.requireNonNull(binding.registerCity.getEditText()),
				countries));

		return binding.getRoot();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	@Override
	public void onClick(View v) {
		if (!Objects.requireNonNull(binding.registerPassword.getEditText()).getText().toString().equals(
			Objects.requireNonNull(binding.registerConfirmPassword.getEditText()).getText().toString()))
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
		else {
			userRepository.addUser(
				new User(
					Objects.requireNonNull(binding.registerEmail.getEditText()).getText().toString(),
					Objects.requireNonNull(binding.registerPassword.getEditText()).getText().toString(),
					Objects.requireNonNull(binding.registerPhoneNumber.getEditText()).getText().toString(),
					Objects.requireNonNull(binding.registerName.getEditText()).getText().toString(),
					locationSelector.getSelectedCity()
				),
				ignored -> Navigation.findNavController(v).navigate(R.id.nav_login),
				() -> Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_SHORT).show()
			);
		}
	}
}
