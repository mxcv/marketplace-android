package com.company.marketplace.network.services;

import com.company.marketplace.models.AccessRefreshJwt;
import com.company.marketplace.models.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TokenService {

	@POST("tokens/access")
	Call<AccessRefreshJwt> access(@Body User user);

	@POST("tokens/refresh")
	Call<AccessRefreshJwt> refresh(@Body AccessRefreshJwt jwt);
}
