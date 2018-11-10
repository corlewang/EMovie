package com.laughing.emovie.base;

import android.support.multidex.MultiDexApplication;

import org.xutils.BuildConfig;
import org.xutils.x;

/**
 * Created by Wy on 2018/11/9.
 */

public class MyApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}

