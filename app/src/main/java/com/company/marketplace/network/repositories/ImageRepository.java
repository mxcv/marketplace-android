package com.company.marketplace.network.repositories;

import com.company.marketplace.models.ImageOutput;
import com.company.marketplace.network.responses.ResponseListener;

import java.util.List;

public interface ImageRepository {

	void addItemImages(int itemId,
					   List<ImageOutput> images,
					   ResponseListener<Void> responseListener);

	void setUserImage(ImageOutput image,
					  ResponseListener<Void> responseListener);
}
