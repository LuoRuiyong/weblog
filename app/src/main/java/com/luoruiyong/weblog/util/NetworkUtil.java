package com.luoruiyong.weblog.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**网络帮助类
 * Created by Administrator on 2017/9/9.
 */

public class NetworkUtil {
    private static final String CLASS_NAME = NetworkUtil.class.getSimpleName() + "-->";
    private static int type;

    public static final class TYPE{
        public static final int NONE = 0;
        public static final int MOBILE = 1;
        public static final int WIFI = 2;
    }

    /**
     * 获取网络状态
     * @param context  上下文
     * @return  网络状态
     */
    public static final int getNetworkState(Context context){
        String message = "";
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if(info == null || !info.isConnected()){
            type = TYPE.NONE;
            message = "无网络";
        }else{
            switch (info.getType()){
                case ConnectivityManager.TYPE_MOBILE:
                    type = TYPE.MOBILE;
                    message = "移动网络";
                    break;
                case ConnectivityManager.TYPE_WIFI:
                    type = TYPE.WIFI;
                    message = "WIFI连接";
                    break;
            }
        }
        LogUtil.d(CLASS_NAME+"当前网络状态："+message);
        return type;
    }
}
