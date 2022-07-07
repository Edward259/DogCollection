package com.edward.application.db;

import android.util.Log;

import com.edward.application.data.ResultBean;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import static com.edward.application.common.Constants.LOG_TAG;

public class DogModelProvider {

    public static DogModel loadDogModelByBreed(String breed) {
        DogModel dogModel = SQLite.select()
                .from(DogModel.class)
                .where(DogModel_Table.breed.eq(breed))
                .querySingle();
        Log.e(LOG_TAG, "queryFromDb: " + (dogModel == null ? breed + " is null " : dogModel.toString()));
        if (dogModel == null) {
            dogModel = new DogModel();
            dogModel.setBreed(breed);
        }
        return dogModel;
    }

    public static List<DogModel> loadCollectedDogModels() {
        List<DogModel> dogModels = SQLite.select()
                .from(DogModel.class)
                .where(DogModel_Table.isCollected.eq(true))
                .queryList();
        return dogModels;
    }

    public static DogModel saveDogModelFromResult(ResultBean<String> resultBean) {
        DogModel dogModel = new DogModel();
        dogModel.setBreed(resultBean.getBreed());
        dogModel.setCoverPath(resultBean.getMessage());
        dogModel.save();
        Log.e(LOG_TAG, "generateDogModel: " + dogModel.toString());
        return dogModel;
    }

    public static boolean saveDogModel(DogModel model) {
        return model.save();
    }
}
