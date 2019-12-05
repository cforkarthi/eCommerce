package com.valli.shoppingapp.APIClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.valli.shoppingapp.APIInterface.ApiInterface.BASE_URL;

public class ProductApiClient {

    private static Retrofit retrofit;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
