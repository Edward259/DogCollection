package com.edward.application.api;


import com.edward.application.data.ResultBean;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("breeds/list/all")
    Observable<ResultBean<HashMap<String, String[]>>> getAllBreeds();

    @GET("breed/{breed}/images/random")
    Observable<ResultBean<String>> getRandomByBreed(@Path("breed") String breed);
}
