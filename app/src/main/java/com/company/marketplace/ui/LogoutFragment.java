package com.company.marketplace.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.company.marketplace.account.Account;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;

public class LogoutFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new MarketplaceRepositoryFactory(getActivity()).createUserRepository().logout();
		Account.get().setUser(null, getActivity());
	}
}
