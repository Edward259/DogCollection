package com.edward.application;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.edward.application.action.ObservableCollect;
import com.edward.application.adapter.DogAdapter;
import com.edward.application.databinding.ActivityMainBinding;
import com.edward.application.eventbus.ShowLargeImageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import static com.edward.application.common.Constants.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        EventBus.getDefault().register(this);
        initView();
        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void ShowLargeImage(ShowLargeImageEvent event) {
        showLargeImg(event.dogModel);
    }

    private void showLargeImg(DogAdapter.DogItemModel dogModel) {
        binding.largeImg.setVisibility(View.VISIBLE);
        DogAdapter.DogItemModel.setCover(binding.largeImg, dogModel);
    }

    private void initView() {
        adapter = new DogAdapter();
        binding.list.setAdapter(adapter);
        binding.list.setLayoutManager(new GridLayoutManager(this, 2));
        binding.largeImg.setOnClickListener(v -> binding.largeImg.setVisibility(View.GONE));
    }

    private void loadData() {
        ObservableCollect.instance()
                .loadBreedData()
                .doOnSubscribe(disposable -> binding.loadingView.setVisibility(View.VISIBLE))
                .doFinally(() -> binding.loadingView.setVisibility(View.GONE))
                .subscribe(resultBean -> {
                    binding.loadingView.setVisibility(View.GONE);
                    adapter.addDogModel(resultBean);
                    Log.e(LOG_TAG, "subscribe: " + resultBean.toString());
                }, throwable -> {
                    Log.e(LOG_TAG, "initData: error" + throwable.getMessage());
                    throwable.printStackTrace();
                });
    }
}