package com.company.marketplace.network.services;

import com.company.marketplace.models.Feedback;
import com.company.marketplace.models.Page;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FeedbackService {

	@GET("feedback/{sellerId}")
	Call<Page<Feedback>> getFeedback(@Path("sellerId") int sellerId,
									 @Query("pageIndex") Integer pageIndex,
									 @Query("pageSize") Integer pageSize);

	@GET("feedback/left/{sellerId}")
	Call<Feedback> getLeftFeedback(@Path("sellerId") int sellerId);

	@POST("feedback")
	Call<Integer> postFeedback(@Body Feedback feedback);

	@DELETE("feedback/{sellerId}")
	Call<Void> deleteFeedback(@Path("sellerId") int sellerId);
}
