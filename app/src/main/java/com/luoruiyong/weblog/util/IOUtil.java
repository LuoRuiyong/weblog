package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.luoruiyong.weblog.base.C;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**网络数据流管理，提供给AppCache使用
 * Created by Administrator on 2017/9/9.
 */

public class IOUtil {
    private static final String CLASS_NAME = IOUtil.class.getSimpleName() + "-->";

    /**
     * 从服务器中下载图片资源
     * @param apiUrl  图片资源路径
     * @return  bitmap图片
     */
    public static Bitmap getBitmapRemote (Context context,String apiUrl) throws Exception{
        Bitmap bitmap = null;
        HttpURLConnection connection = null;
        if(NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        try{
            URL url = new URL(apiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10*1000);
            connection.setReadTimeout(10*1000);
            connection.setDoInput(true);
            connection.connect();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream is = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }
            LogUtil.d(CLASS_NAME+"成功从服务器中获取图片");
        }catch (MalformedURLException e){
            LogUtil.d(CLASS_NAME+"非法URL:"+apiUrl);
        }catch (IOException e){
            LogUtil.d(CLASS_NAME+"连接"+apiUrl+"失败");
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return bitmap;
    }
}
