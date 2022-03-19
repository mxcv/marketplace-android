package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.company.marketplace.R;
import com.company.marketplace.models.Account;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class LogoutFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new MarketplaceRepositoryFactory(getContext()).createUserRepository().logout(null);
		Account.get().setUser(null);
	}
}
