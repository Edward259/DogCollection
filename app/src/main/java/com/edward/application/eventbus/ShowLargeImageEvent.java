package com.edward.application.eventbus;

import com.edward.application.adapter.DogAdapter;

public class ShowLargeImageEvent {
    public DogAdapter.DogItemModel dogModel;

    public ShowLargeImageEvent(DogAdapter.DogItemModel dogModel) {
        this.dogModel = dogModel;
    }
}
