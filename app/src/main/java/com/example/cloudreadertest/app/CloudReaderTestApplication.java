package com.example.cloudreadertest.app;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.example.cloudreadertest.utils.DebugUtil;
import com.example.http.HttpUtils;

/**
 * Created by 筑库 on 2017/11/6.
 */

public class CloudReaderTestApplication extends Application {

    private static CloudReaderTestApplication cloudReaderTestApplication;
    public static CloudReaderTestApplication getInstance(){
        return cloudReaderTestApplication;
    }
    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        super.onCreate();
        cloudReaderTestApplication = this;
        HttpUtils.getInstance().init(this, DebugUtil.DEBUG);
        initTextSize();
    }

    private void initTextSize(){
        Resources resources = getResources();
        Configuration configuration = new Configuration();
        configuration.setToDefaults();
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
    }
}
