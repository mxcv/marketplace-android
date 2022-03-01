package com.company.marketplace.network.repositories;

import android.content.Context;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.PageInput;
import com.company.marketplace.models.PageOutput;
import com.company.marketplace.models.User;
import com.company.marketplace.models.JwtType;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.NetworkErrorListener;
import com.company.marketplace.network.responses.ResponseListener;
import com.company.marketplace.network.responses.UnauthorizedErrorListener;
import com.company.marketplace.network.services.NetworkService;
import com.company.marketplace.network.services.SimpleCallback;

import java.util.List;

public class MarketplaceRepository implements UserRepository, ItemRepository {

	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final NetworkErrorListener networkErrorListener;

	public MarketplaceRepository(Context context,
								 UnauthorizedErrorListener unauthorizedErrorListener,
								 NetworkErrorListener networkErrorListener) {

		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.networkErrorListener = networkErrorListener;
		JwtRepository.initialize(context);
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
					JwtRepository.getInstance().setTokens(jwt);
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
		JwtRepository.getInstance().setTokens(null);
	}

	@Override
	public void getUser(ResponseListener<User> responseListener) {

		if (JwtRepository.getInstance().getToken(JwtType.ACCESS) == null) {
			if (unauthorizedErrorListener != null)
				unauthorizedErrorListener.onUnauthorizedError();
			return;
		}

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
	public void getCountries(ResponseListener<List<Country>> responseListener) {

		NetworkService.getInstance()
			.getLocationService()
			.getCountries()
			.enqueue(new SimpleCallback<>(
				countries -> {
					if (responseListener != null)
						responseListener.onResponse(countries);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void getMyItems(PageOutput pageOutput,
						   ResponseListener<PageInput> responseListener) {

		NetworkService.getInstance()
			.getItemService()
			.getMyItems(pageOutput)
			.enqueue(new SimpleCallback<>(
				pageInput -> {
					if (responseListener != null)
						responseListener.onResponse(pageInput);
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
	public void getCategories(ResponseListener<List<Category>> responseListener) {
		NetworkService.getInstance()
			.getCategoryService()
			.getCategories()
			.enqueue(new SimpleCallback<>(
				categories -> {
					if (responseListener != null)
						responseListener.onResponse(categories);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}
}
