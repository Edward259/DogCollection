package com.edward.application.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

public class GsonUtils {

    public static List toList(String content) {
        List list = new Gson().fromJson(content, new TypeToken<List>() {
        }.getType());
        return list == null ? Collections.emptyList() : list;
    }
}
