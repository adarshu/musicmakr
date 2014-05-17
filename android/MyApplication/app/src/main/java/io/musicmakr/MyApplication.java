package io.musicmakr;

import android.app.Application;

/**
 * Created by rayho on 5/17/14.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public MyApplication() {
        instance = this;
    }
}
