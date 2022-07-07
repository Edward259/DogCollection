package com.edward.application.utils;

import com.edward.application.db.DogModel;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ObservableUtils {
    @NonNull
    public static Observable<DogModel> getWorkThreadBaseObservable(DogModel model) {
        return getBaseObservable(model, Schedulers.io(), Schedulers.io());
    }

    @NonNull
    public static Observable<DogModel> getBaseObservable(DogModel model, @NonNull Scheduler subscribeOn, @NonNull Scheduler observeOn) {
        return Observable.just(model)
                .subscribeOn(subscribeOn)
                .observeOn(observeOn);
    }


}
