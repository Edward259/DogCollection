package com.edward.application.utils;


import android.view.View;

import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class RxViewUtils {

    public interface onDoubleClickListener {
        void onDoubleClick(View view);
    }

    public static void setOnDoubleClickListener(View view,
                                                onDoubleClickListener onDoubleClickListener,
                                                View.OnClickListener onClickListener) {
        Observable<Object> observable = RxView.clicks(view).share();
        observable.buffer(observable.debounce(200, TimeUnit.MILLISECONDS))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(voids -> {
                    if (voids.size() == 1) {
                        onClickListener.onClick(view);
                    } else if (voids.size() >= 2) {
                        onDoubleClickListener.onDoubleClick(view);
                    }
                }, throwable -> throwable.printStackTrace());
    }
}
