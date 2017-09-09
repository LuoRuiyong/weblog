package com.luoruiyong.weblog.base;

import android.content.Context;

import org.litepal.LitePalApplication;

/**应用上下文
 * Created by Administrator on 2017/9/8.
 */

public class MyApplication extends LitePalApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
