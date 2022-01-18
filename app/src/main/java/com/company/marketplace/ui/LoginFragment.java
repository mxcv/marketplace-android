package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.company.marketplace.R;
import com.company.marketplace.repositories.MarketplaceRepository;
import com.company.marketplace.repositories.UserRepository;

public class LoginFragment extends Fragment implements View.OnClickListener {

	private EditText emailEditText, passwordEditText;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_login, container, false);
		view.findViewById(R.id.login).setOnClickListener(this);
		emailEditText = view.findViewById(R.id.loginEmail);
		passwordEditText = view.findViewById(R.id.loginPassword);
		return view;
	}

	@Override
	public void onClick(View v) {
		UserRepository userRepository = new MarketplaceRepository(getContext(), null, () ->
			Toast.makeText(getContext(), R.string.connection_error, Toast.LENGTH_LONG).show()
		);
		userRepository.login(emailEditText.getText().toString(), passwordEditText.getText().toString(),
			ignored -> userRepository.getUser(user -> {
				Log.i("", user.email);
			}),
			() -> Toast.makeText(getContext(), R.string.invalid_email_or_password, Toast.LENGTH_SHORT).show()
		);
	}
}
