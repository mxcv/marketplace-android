package com.company.marketplace.network.repositories;

import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.Page;
import com.company.marketplace.network.responses.BadRequestErrorListener;
import com.company.marketplace.network.responses.ResponseListener;

public interface FeedbackRepository {

	void getFeedback(int sellerId,
					 Integer pageIndex,
					 Integer pageSize,
					 ResponseListener<Page<Feedback>> responseListener);

	void getLeftFeedback(int sellerId,
						 ResponseListener<Feedback> responseListener);

	void addFeedback(Feedback feedback,
					 ResponseListener<Integer> responseListener,
					 BadRequestErrorListener badRequestErrorListener);

	void removeFeedback(int sellerId,
						ResponseListener<Void> responseListener,
						BadRequestErrorListener badRequestErrorListener);
}
