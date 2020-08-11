package com.asolomkin.loftmoney;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @GET("./items")
    Single<List<MoneyItem>> getMoney(@Query("auth-token") String token, @Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addMoney(@Field("auth-token") String token,
                         @Field("price") String price,
                         @Field("name") String name,
                         @Field("type") String type);

//    @POST("./items/remove")
//    @FormUrlEncoded
//    Completable removeItem(@Field("id") String id,
//                           @Field("auth-token") String token);
    @POST("items/remove")
    Call<AuthResponse> removeItem(@Query("id") String id, @Query("auth-token") String token);


    @GET("balance")
    Call<BalanceResponce> getBalance(@Query("auth-token") String token);

}
