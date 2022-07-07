package com.edward.application.utils;


import com.edward.application.api.ApiService;
import com.edward.application.common.Constants;
import com.edward.application.data.ResultBean;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    private static final String TAG = RetrofitUtils.class.getSimpleName();

    public static Retrofit getBaseRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public static Observable<ResultBean<HashMap<String, String[]>>> getFetchAllBreedsObservable() {
        ApiService service = getBaseRetrofit().create(ApiService.class);
        return service.getAllBreeds()
                .retry(3);
    }

    public static Observable<ResultBean<String>> getRandomByBreedObservable(String breed) {
        ApiService service = getBaseRetrofit().create(ApiService.class);
        return service.getRandomByBreed(breed).
                retry(3)
                .doOnNext(dogBean -> {
                    dogBean.setBreed(breed);
                });
    }
}