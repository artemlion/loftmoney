package com.asolomkin.loftmoney;

import android.app.Application;

import com.asolomkin.loftmoney.remote.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoftApp extends Application {

    private Api api;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://loftschool.com/android-api/basic/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        api = retrofit.create(Api.class);
    }

    public Api getApi() {
        return api;
    }
}
