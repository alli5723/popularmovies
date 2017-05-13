package com.omo_lanke.android.popularmovies.api;

import com.omo_lanke.android.popularmovies.model.ApiResponse;
import com.omo_lanke.android.popularmovies.model.MovieDetail;
import com.omo_lanke.android.popularmovies.model.ReviewsResponse;
import com.omo_lanke.android.popularmovies.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public interface Endpoints {
    @GET("/3/discover/movie")
    Call<ApiResponse> listMovies(@Query("sort_by") String sort_by, @Query("api_key") String api_key, @Query("page") String page);

    @GET("/3/movie/{movie_id}")
    Call<MovieDetail> getDetails(@Path("movie_id") String movie_id, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/videos")
    Call<TrailerResponse> getTrailers(@Path("id") String id, @Query("api_key") String api_key);

    @GET("/3/movie/{id}/reviews")
    Call<ReviewsResponse> getReviews(@Path("id") String id, @Query("api_key") String api_key);
}
