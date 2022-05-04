package com.company.marketplace.network.repositories;

import android.content.Context;

import com.company.marketplace.R;
import com.company.marketplace.models.Category;
import com.company.marketplace.models.City;
import com.company.marketplace.models.Country;
import com.company.marketplace.models.Currency;
import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.models.Item;
import com.company.marketplace.models.ItemRequest;
import com.company.marketplace.models.JwtType;
import com.company.marketplace.models.Page;
import com.company.marketplace.models.Region;
import com.company.marketplace.models.SortType;
import com.company.marketplace.models.User;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.NetworkErrorListener;
import com.company.marketplace.network.responses.ResponseListener;
import com.company.marketplace.network.responses.UnauthorizedErrorListener;
import com.company.marketplace.network.services.NetworkService;
import com.company.marketplace.network.services.SimpleCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class NetworkMarketplaceRepository implements CategoryRepository,
													 CurrencyRepository,
													 FeedbackRepository,
													 ImageRepository,
													 ItemRepository,
													 LocationRepository,
													 SortTypeRepository,
													 UserRepository {

	private static final int[] sortTypeStringResources = new int[] {
		R.string.sort_newest,
		R.string.sort_cheapest,
		R.string.sort_most_expensive
	};

	private final Context context;
	private final UnauthorizedErrorListener unauthorizedErrorListener;
	private final NetworkErrorListener networkErrorListener;

	public NetworkMarketplaceRepository(Context context,
										UnauthorizedErrorListener unauthorizedErrorListener,
										NetworkErrorListener networkErrorListener) {

		this.context = context;
		this.unauthorizedErrorListener = unauthorizedErrorListener;
		this.networkErrorListener = networkErrorListener;

		NetworkService.initialize(context);
		JwtRepository.initialize(context);
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
	public void getFeedback(int sellerId,
							Integer pageIndex,
							Integer pageSize,
							ResponseListener<Page<Feedback>> responseListener) {

		NetworkService.get()
			.getFeedbackService()
			.getFeedback(sellerId, pageIndex, pageSize)
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
	public void getLeftFeedback(int sellerId,
								ResponseListener<Feedback> responseListener) {

		NetworkService.get()
			.getFeedbackService()
			.getLeftFeedback(sellerId)
			.enqueue(new SimpleCallback<>(
				feedback -> {
					if (responseListener != null)
						responseListener.onResponse(feedback);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void addFeedback(Feedback feedback,
							ResponseListener<Integer> responseListener,
							BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
			.getFeedbackService()
			.postFeedback(feedback)
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
	public void removeFeedback(int sellerId,
							   ResponseListener<Void> responseListener,
							   BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
			.getFeedbackService()
			.deleteFeedback(sellerId)
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
	public void addItemImages(int itemId,
							  List<ImageOutput> images,
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
	public void getItems(ItemRequest itemRequest,
						 ResponseListener<Page<Item>> responseListener) {

		NetworkService.get()
			.getItemService()
			.getItems(
				itemRequest.getQuery(),
				itemRequest.getMinPrice(),
				itemRequest.getMaxPrice(),
				itemRequest.getCurrency() == null ? null : itemRequest.getCurrency().getId(),
				itemRequest.getCategory() == null ? null : itemRequest.getCategory().getId(),
				itemRequest.getCountry() == null ? null : itemRequest.getCountry().getId(),
				itemRequest.getRegion() == null ? null : itemRequest.getRegion().getId(),
				itemRequest.getCity() == null ? null : itemRequest.getCity().getId(),
				itemRequest.getUser() == null ? null : itemRequest.getUser().getId(),
				itemRequest.getSortType() == null ? null : itemRequest.getSortType().getId(),
				itemRequest.getPageIndex(),
				itemRequest.getPageSize())
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
	public void getCountries(ResponseListener<List<Country>> responseListener) {

		NetworkService.get()
			.getLocationService()
			.getCountries()
			.enqueue(new SimpleCallback<>(
				countries -> {
					for (Country country : countries)
						for (Region region : country.getRegions()) {
							for (City city : region.getCities())
								city.setRegion(region);
							region.setCountry(country);
						}
					if (responseListener != null)
						responseListener.onResponse(countries);
				},
				null,
				unauthorizedErrorListener,
				networkErrorListener
			));
	}

	@Override
	public void getSortTypes(ResponseListener<List<SortType>> responseListener) {

		responseListener.onResponse(IntStream.range(0, sortTypeStringResources.length)
			.mapToObj(i -> new SortType(i + 1, context.getString(sortTypeStringResources[i])))
			.collect(Collectors.toList()));
	}

	@Override
	public void login(User user,
					  ResponseListener<Void> responseListener,
					  BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
			.getTokenService()
			.access(user)
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
	public void logout(ResponseListener<Void> responseListener) {
		JwtRepository.get().setTokens(null);
		if (responseListener != null)
			responseListener.onResponse(null);
	}

	@Override
	public void getUser(int id,
						ResponseListener<User> responseListener) {

		NetworkService.get()
			.getUserService()
			.getUser(id)
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
	public void getCurrentUser(ResponseListener<User> responseListener) {

		if (JwtRepository.get().getToken(JwtType.ACCESS) == null) {
			if (unauthorizedErrorListener != null)
				unauthorizedErrorListener.onUnauthorizedError();
			return;
		}

		NetworkService.get()
			.getUserService()
			.getCurrentUser()
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
						ResponseListener<Integer> responseListener,
						BadRequestErrorListener badRequestErrorListener) {

		NetworkService.get()
			.getUserService()
			.postUser(user)
			.enqueue(new SimpleCallback<>(
				userId -> {
					if (responseListener != null)
						responseListener.onResponse(userId);
				},
				badRequestErrorListener,
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
