package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Country;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface LocationRepository {

	void getCountries(ResponseListener<List<Country>> responseListener);
}
