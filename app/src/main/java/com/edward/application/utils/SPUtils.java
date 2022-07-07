package com.edward.application.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.edward.application.App;
import com.google.gson.Gson;

import java.util.List;

public class SPUtils {
    public static final String DOG_BREEDS_KEY = "dog_breeds_key";

    private static SharedPreferences sharedPreferences =
            App.getAppContext().getSharedPreferences("DogCollection", Context.MODE_PRIVATE);

    public static List<String> getDogBreeds() {
        String breeds = sharedPreferences.getString(DOG_BREEDS_KEY, "");
        return GsonUtils.toList(breeds);
    }

    public static boolean setDogBreeds(List<String> dogBreeds) {
        SharedPreferences.Editor editor = sharedPreferences.edit().putString(DOG_BREEDS_KEY, new Gson().toJson(dogBreeds));
        return editor.commit();
    }
}
