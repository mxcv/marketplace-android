package com.company.marketplace.network.repositories;

import android.content.Context;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.Page;
import com.company.marketplace.models.User;
import com.company.marketplace.models.JwtType;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.NetworkErrorListener;
import com.company.marketplace.network.responses.ResponseListener;
import com.company.marketplace.network.responses.UnauthorizedErrorListener;
import com.company.marketplace.network.services.NetworkService;
import com.company.marketplace.network.services.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
			.postUser(user)
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
	public void getMyItems(Integer skipCount, Integer takeCount,
						   ResponseListener<Page> responseListener) {

		NetworkService.getInstance()
			.getItemService()
			.getMyItems(skipCount, takeCount)
			.enqueue(new SimpleCallback<>(
				page -> {
					if (responseListener != null)
						responseListener.onResponse(page);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void addItem(Item item,
						ResponseListener<Integer> responseListener,
						BadRequestErrorListener badRequestErrorListener) {

		NetworkService.getInstance()
			.getItemService()
			.postItem(item)
			.enqueue(new SimpleCallback<>(
				id -> {
					if (responseListener != null)
						responseListener.onResponse(id);
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

	@Override
	public void addItemImages(int itemId, List<ImageOutput> images,
							  ResponseListener<Void> responseListener) {

		List<MultipartBody.Part> imageParts = new ArrayList<>(images.size());
		for (ImageOutput image : images) {
			RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image.getBytes());
			imageParts.add(MultipartBody.Part.createFormData("images", image.getFilename(), requestFile));
		}

		NetworkService.getInstance()
			.getImageService()
			.postImages(itemId, imageParts)
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
}
