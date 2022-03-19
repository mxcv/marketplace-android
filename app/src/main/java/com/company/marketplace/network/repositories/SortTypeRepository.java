package com.company.marketplace.network.repositories;

import com.company.marketplace.models.SortType;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface SortTypeRepository {

	void getSortTypes(ResponseListener<List<SortType>> responseListener);
}
