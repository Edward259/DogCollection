package com.edward.application.db;

import com.edward.application.utils.GsonUtils;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.converter.TypeConverter;

import java.util.List;

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class ConverterStringList extends TypeConverter<String, List> {

    @Override
    public String getDBValue(List model) {
        return new Gson().toJson(model);
    }

    @Override
    public List<String> getModelValue(String data) {
        return GsonUtils.toList(data);
    }

}