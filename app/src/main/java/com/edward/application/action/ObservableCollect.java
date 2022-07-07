package com.edward.application.action;

import android.text.TextUtils;
import android.util.Log;

import com.edward.application.data.ResultBean;
import com.edward.application.db.DogModel;
import com.edward.application.db.DogModelProvider;
import com.edward.application.utils.FileUtils;
import com.edward.application.utils.ImageUtils;
import com.edward.application.utils.RetrofitUtils;
import com.edward.application.utils.SPUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.edward.application.common.Constants.LOG_TAG;
import static com.edward.application.db.DogModelProvider.loadDogModelByBreed;
import static com.edward.application.db.DogModelProvider.saveDogModelFromResult;
import static com.edward.application.utils.ObservableUtils.getWorkThreadBaseObservable;

public class ObservableCollect {
    private static ObservableCollect instance = new ObservableCollect();

    private ObservableCollect() {
    }

    public static ObservableCollect instance() {
        return instance;
    }

    public Observable<DogModel> loadBreedData() {
        return getFetchAllBreedsObservable()
                .subscribeOn(Schedulers.io())
                .concatMapIterable(breeds -> breeds)
                .flatMap(breed -> fetchDogModel(breed))
                .map(model -> cacheCover(model))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @NotNull
    private Observable<List<String>> getFetchAllBreedsObservable() {
        return Observable.just(SPUtils.getDogBreeds())
                .filter(breeds -> breeds != null && breeds.size() > 0)
                .switchIfEmpty(RetrofitUtils.getFetchAllBreedsObservable()
                        .map(resultBean -> saveAllBreeds(resultBean))
                );
    }

    @NotNull
    private Observable<DogModel> fetchDogModel(String breed) {
        return Observable.just(loadDogModelByBreed(breed))
                .filter(model -> !TextUtils.isEmpty(model.getCoverPath()))
                .switchIfEmpty(RetrofitUtils.getRandomByBreedObservable(breed)
                        .map(resultBean -> saveDogModelFromResult(resultBean))
                );
    }

    public Observable<DogModel> updateCover(DogModel model) {
        return RetrofitUtils.getRandomByBreedObservable(model.getBreed())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(result -> model.setCoverPath(result.getMessage()))
                .flatMap(result -> replaceCover(model))
                .filter(success -> success)
                .map(success -> model)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Boolean> replaceCover(DogModel model) throws IOException {
        return Observable.just(ImageUtils.cacheCover(model, true))
                .subscribeOn(Schedulers.io());
    }

    private List<String> saveAllBreeds(ResultBean<HashMap<String, String[]>> resultBean) {
        List<String> breeds = new ArrayList<>(resultBean.getMessage().keySet());
        SPUtils.setDogBreeds(breeds);
        return breeds;
    }

    public void saveDogModel(DogModel model) {
        getWorkThreadBaseObservable(model).subscribe(dogModel -> dogModel.save());
    }

    private DogModel cacheCover(DogModel model) throws IOException {
        getWorkThreadBaseObservable(model).subscribe(new Consumer<DogModel>() {
            @Override
            public void accept(DogModel model) throws Throwable {
                ImageUtils.cacheCover(model);
            }
        }, throwable -> {
            throwable.printStackTrace();
            FileUtils.deleteFile(ImageUtils.getCoverCachePath(model));
            Log.e(LOG_TAG, "cacheCover failed for " + model.toString());
        });
        return model;
    }

    public Observable uploadDogInfo() {
        return Observable.just(DogModelProvider.loadCollectedDogModels())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMapIterable(dogModels -> dogModels)
                .flatMap(model -> uploadCollectedData(model))
                .observeOn(AndroidSchedulers.mainThread())
                .map(o -> {
                    Log.i(LOG_TAG, "uploadDogInfo update progress");
                    return o;
                }).doFinally(() -> {
                    Log.i(LOG_TAG, "uploadDogInfo doFinally");
                });

    }

    public Observable uploadCollectedData(DogModel model) {
        return Observable.create(emitter -> {
            // TODO: 2022/7/11 upload data
            Thread.sleep(50);
            emitter.onNext(model);
            emitter.onComplete();
        });
    }
}
