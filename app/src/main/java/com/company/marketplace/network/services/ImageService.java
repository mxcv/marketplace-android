package com.company.marketplace.network.services;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImageService {

	@Multipart
	@POST("images/items/{itemId}")
	Call<Void> postItemImages(@Path("itemId") int itemId, @Part List<MultipartBody.Part> images);

	@Multipart
	@PUT("images/users")
	Call<Void> putUserImage(@Part MultipartBody.Part image);
}
