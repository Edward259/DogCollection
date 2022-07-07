package com.edward.application;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.lsposed.hiddenapibypass.HiddenApiBypass;

public class App extends Application {
    private static Context instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FlowManager.init(this);
        bypassHiddenApi();
    }

    private void bypassHiddenApi() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            HiddenApiBypass.addHiddenApiExemptions("");
        }
    }

    public static Context getAppContext() {
        return instance;
    }


}
