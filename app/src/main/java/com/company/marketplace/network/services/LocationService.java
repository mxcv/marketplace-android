package com.company.marketplace.network.services;

import com.company.marketplace.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface LocationService {

	@GET("locations")
	Call<List<Country>> getCountries();
}
