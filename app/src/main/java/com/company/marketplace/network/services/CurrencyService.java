package com.company.marketplace.network.services;

import com.company.marketplace.models.Currency;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CurrencyService {

	@GET("currencies")
	Call<List<Currency>> getCurrencies();
}
