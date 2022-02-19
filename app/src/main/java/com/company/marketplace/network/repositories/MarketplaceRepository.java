package com.company.marketplace.network.repositories;

import android.content.Context;
import android.util.Log;

import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.User;
import com.company.marketplace.network.jwt.FileJwtRepository;
import com.company.marketplace.network.jwt.JwtRepository;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.NetworkErrorListener;
import com.company.marketplace.network.responses.ResponseListener;
import com.company.marketplace.network.responses.UnauthorizedErrorListener;
import com.company.marketplace.network.services.NetworkService;
import com.company.marketplace.network.services.SimpleCallback;

import java.util.List;

public class MarketplaceRepository implements UserRepository, CurrencyRepository, ItemRepository {

	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final NetworkErrorListener networkErrorListener;
	private final JwtRepository jwtRepository;

	public MarketplaceRepository(Context context,
								 UnauthorizedErrorListener unauthorizedErrorListener,
								 NetworkErrorListener networkErrorListener) {

		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.networkErrorListener = networkErrorListener;
		jwtRepository = new FileJwtRepository(context);
		NetworkService.initialize(context);
	}

	@Override
	public void login(String email, String password,
					  ResponseListener<Void> responseListener,
					  BadRequestErrorListener badRequestErrorListener) {

		NetworkService.getInstance()
			.getTokenService()
			.access(new User(email, password))
			.enqueue(new SimpleCallback<>(
				jwt -> {
					jwtRepository.setTokens(jwt);
					Log.i("JWT", "Access and refresh tokens were acquired.");
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				badRequestErrorListener,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void logout() {
		jwtRepository.setTokens(null);
		Log.i("JWT", "Access and refresh tokens were removed.");
	}

	@Override
	public void getUser(ResponseListener<User> responseListener) {

		NetworkService.getInstance()
			.getUserService()
			.getUser()
			.enqueue(new SimpleCallback<>(
				user -> {
					if (responseListener != null)
						responseListener.onResponse(user);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void createUser(User user,
						   ResponseListener<Void> responseListener,
						   BadRequestErrorListener badRequestErrorListener) {

		NetworkService.getInstance()
			.getUserService()
			.createUser(user)
			.enqueue(new SimpleCallback<>(
				ignored -> {
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void getCurrencies(ResponseListener<List<Currency>> responseListener) {

		NetworkService.getInstance()
			.getCurrencyService()
			.getCurrencies()
			.enqueue(new SimpleCallback<>(
				currencies -> {
					if (responseListener != null)
						responseListener.onResponse(currencies);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void getMyItems(ResponseListener<List<Item>> responseListener) {
		NetworkService.getInstance()
			.getItemService()
			.getMyItems()
			.enqueue(new SimpleCallback<>(
				items -> {
					if (responseListener != null)
						responseListener.onResponse(items);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void addItem(Item item,
						ResponseListener<Void> responseListener,
						BadRequestErrorListener badRequestErrorListener) {

		NetworkService.getInstance()
			.getItemService()
			.putItem(item)
			.enqueue(new SimpleCallback<>(
				ignored -> {
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				badRequestErrorListener,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void removeItem(int id,
						   ResponseListener<Void> responseListener,
						   BadRequestErrorListener badRequestErrorListener) {

		NetworkService.getInstance()
			.getItemService()
			.deleteItem(id)
			.enqueue(new SimpleCallback<>(
				ignored -> {
					if (responseListener != null)
						responseListener.onResponse(null);
				},
				badRequestErrorListener,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}
}
