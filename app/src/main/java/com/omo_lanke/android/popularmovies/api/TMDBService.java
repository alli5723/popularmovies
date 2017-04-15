package com.omo_lanke.android.popularmovies.api;

import com.omo_lanke.android.popularmovies.utils.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by omo_lanke on 14/04/2017.
 */

public class TMDBService {
    private Endpoints endpoints;
    public TMDBService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        endpoints = retrofit.create(Endpoints.class);
    }

    public Endpoints endpoints(){
        return endpoints;
    }
}
