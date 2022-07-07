package com.edward.application.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static boolean ensureFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        } else if (fileExist(path)) {
            return true;
        }
        File file = new File(path);
        if (file.isDirectory()) {
            return file.mkdir();
        } else {
            try {
                boolean success = file.createNewFile();
                return success;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public static boolean fileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return new File(path).exists();
    }

    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path) || !new File(path).exists()) {
            return false;
        }
        return new File(path).delete();
    }
}
