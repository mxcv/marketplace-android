package com.company.marketplace.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.company.marketplace.R;
import com.company.marketplace.models.Account;
import com.company.marketplace.databinding.ActivityMainBinding;
import com.company.marketplace.models.User;
import com.company.marketplace.network.repositories.MarketplaceRepositoryFactory;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements Observer<User> {

	private AppBarConfiguration appBarConfiguration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		setSupportActionBar(binding.appBarMain.toolbar);
		DrawerLayout drawer = binding.drawerLayout;
		NavigationView navigationView = binding.navView;

		appBarConfiguration = new AppBarConfiguration.Builder(
			R.id.nav_items, R.id.nav_my_items, R.id.nav_add_item, R.id.nav_login, R.id.nav_register, R.id.nav_logout)
			.setOpenableLayout(drawer)
			.build();

		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);

		Account.get().getUser().observe(this, this);
		new MarketplaceRepositoryFactory(this).createUserRepository().getCurrentUser(user ->
			Account.get().setUser(user));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
	}

	@Override
	public void onChanged(User user) {
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.removeHeaderView(navigationView.getHeaderView(0));
		navigationView.getMenu().clear();

		if (user == null) {
			navigationView.inflateMenu(R.menu.guest_drawer);
			Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
				.navigate(R.id.nav_login);
		}
		else {
			navigationView.inflateMenu(R.menu.seller_drawer);
			navigationView.inflateHeaderView(R.layout.nav_header_main);

			View headerView = navigationView.getHeaderView(0);
			((TextView)headerView.findViewById(R.id.navName)).setText(user.getName());
			((TextView)headerView.findViewById(R.id.navPhoneNumber)).setText(user.getPhoneNumber());
			if (user.getImage() == null)
				((ImageView)headerView.findViewById(R.id.navImage)).setImageResource(R.drawable.ic_person);
			else
				Picasso.get()
					.load(user.getImage().getFullPath())
					.into((ImageView)headerView.findViewById(R.id.navImage));
		}
	}
}
