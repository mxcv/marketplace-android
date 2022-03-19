package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Currency;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface CurrencyRepository {

	void getCurrencies(ResponseListener<List<Currency>> responseListener);
}
