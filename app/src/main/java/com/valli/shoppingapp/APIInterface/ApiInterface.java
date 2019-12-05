package com.valli.shoppingapp.APIInterface;

import com.valli.shoppingapp.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
//        String BASE_URL = "http://www.mocky.io/v2/5dc3f39c30000067003477a9/";
    String BASE_URL = "http://www.mocky.io/v2/5dcbd78254000059009c1e72/";


    @GET(".")
    Call<List<Product>> getProductDetails();

}
