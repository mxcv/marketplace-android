package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.company.marketplace.network.repositories.UserRepository;
import com.company.marketplace.ui.tools.LocationSelector;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment implements View.OnClickListener {

	private EditText nameView, phoneNumberView, emailView, passwordView, confirmPasswordView;
	private AutoCompleteTextView countryView, regionView, cityView;
	private LocationSelector locationSelector;
	private UserRepository userRepository;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_register, container, false);
		view.findViewById(R.id.registerRegister).setOnClickListener(this);
		nameView = ((TextInputLayout)view.findViewById(R.id.registerName)).getEditText();
		phoneNumberView = ((TextInputLayout)view.findViewById(R.id.registerPhoneNumber)).getEditText();
		emailView = ((TextInputLayout)view.findViewById(R.id.registerEmail)).getEditText();
		passwordView = ((TextInputLayout)view.findViewById(R.id.registerPassword)).getEditText();
		confirmPasswordView = ((TextInputLayout)view.findViewById(R.id.registerConfirmPassword)).getEditText();
		countryView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.registerCountry)).getEditText();
		regionView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.registerRegion)).getEditText();
		cityView = (AutoCompleteTextView)((TextInputLayout)view.findViewById(R.id.registerCity)).getEditText();

		userRepository = new MarketplaceRepositoryFactory(getActivity()).createUserRepository();
		userRepository.getCountries(countries ->
			locationSelector = new LocationSelector(countryView, regionView, cityView, countries)
		);

		return view;
	}

	@Override
	public void onClick(View v) {
		if (!passwordView.getText().toString().equals(confirmPasswordView.getText().toString()))
			Toast.makeText(getContext(), R.string.passwords_do_not_match, Toast.LENGTH_SHORT).show();
		else {
			userRepository.addUser(new User(
					emailView.getText().toString(),
					passwordView.getText().toString(),
					phoneNumberView.getText().toString(),
					nameView.getText().toString(),
					locationSelector.getSelectedCity()
				),
				ignored -> Navigation.findNavController(v).navigate(R.id.nav_login),
				() -> Toast.makeText(getContext(), R.string.registration_error, Toast.LENGTH_SHORT).show()
			);
		}
	}
}
