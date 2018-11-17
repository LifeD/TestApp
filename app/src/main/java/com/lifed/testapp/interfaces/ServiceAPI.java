package com.lifed.testapp.interfaces;

import com.lifed.testapp.models.Data;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ServiceAPI {

    String BASE_URL = "https://api.myjson.com/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("bins/r2pdk")
    Call<Data> getData();
}
