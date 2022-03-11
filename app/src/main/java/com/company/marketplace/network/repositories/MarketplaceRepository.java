package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Category;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.JwtType;
import com.company.marketplace.models.Page;
import com.company.marketplace.models.User;
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

	public MarketplaceRepository(UnauthorizedErrorListener unauthorizedErrorListener,
								 NetworkErrorListener networkErrorListener) {

		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.networkErrorListener = networkErrorListener;
	}

	@Override
	public void login(String email, String password,
					  ResponseListener<Void> responseListener,
					  BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
			.getTokenService()
			.access(new User(email, password))
			.enqueue(new SimpleCallback<>(
				jwt -> {
					JwtRepository.get().setTokens(jwt);
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
		JwtRepository.get().setTokens(null);
	}

	@Override
	public void getUser(ResponseListener<User> responseListener) {

		if (JwtRepository.get().getToken(JwtType.ACCESS) == null) {
			if (unauthorizedErrorListener != null)
				unauthorizedErrorListener.onUnauthorizedError();
			return;
		}

		NetworkService.get()
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
	public void addUser(User user,
						ResponseListener<Void> responseListener,
						BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
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

		NetworkService.get()
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
	public void setUserImage(ImageOutput image,
							 ResponseListener<Void> responseListener) {

		NetworkService.get()
			.getImageService()
			.putUserImage(createImageForm("image", image))
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
	public void getMyItems(Integer skipCount, Integer takeCount,
						   ResponseListener<Page> responseListener) {

		NetworkService.get()
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

		NetworkService.get()
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

		NetworkService.get()
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

		NetworkService.get()
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

		NetworkService.get()
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
		for (ImageOutput image : images)
			imageParts.add(createImageForm("images", image));

		NetworkService.get()
			.getImageService()
			.postItemImages(itemId, imageParts)
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

	private MultipartBody.Part createImageForm(String name, ImageOutput image) {
		return MultipartBody.Part.createFormData(
			name,
			image.getFilename(),
			RequestBody.create(
				MediaType.parse("multipart/form-data"),
				image.getBytes()
			));
	}
}
