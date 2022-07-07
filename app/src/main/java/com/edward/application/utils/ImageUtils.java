package com.edward.application.utils;

import android.util.Log;

import com.edward.application.App;
import com.edward.application.db.DogModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static com.edward.application.common.Constants.LOG_TAG;

public class ImageUtils {
    public static String COVER_CACHE_PATH = App.getAppContext().getExternalFilesDir("image_cache").getAbsolutePath() + "/%s.jpg";

    public static boolean cacheCover(DogModel model) throws IOException {
        return cacheCover(model, false);
    }

    public static boolean cacheCover(DogModel model, boolean replace) throws IOException {
        if (!replace && FileUtils.fileExist(getCoverCachePath(model))) {
            return false;
        }
        URL url = new URL(model.getCoverPath());
        InputStream inputStream = url.openStream();
        File file = new File(getCoverCachePath(model));
        FileUtils.ensureFileExist(getCoverCachePath(model));
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int hasRead = 0;
        while ((hasRead = inputStream.read()) != -1) {
            fileOutputStream.write(hasRead);
        }
        fileOutputStream.close();
        inputStream.close();
        Log.e(LOG_TAG, "cacheCover: success " + model.toString());
        return true;
    }

    public static String getCoverCachePath(DogModel model) {
        return String.format(COVER_CACHE_PATH, model.getBreed());
    }
}
