package com.hosiluan.webservicedatabase.remote;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HoSiLuan on 8/11/2017.
 */

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getClient (String baseURL){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit;
    }

}
