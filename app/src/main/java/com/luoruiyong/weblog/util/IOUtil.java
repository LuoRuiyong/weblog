package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Picture;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**图片联网下载帮助类
 * 本类对外功能
 * 1.远程获取原图
 * 2.远程获取自定义缩略图
 * 3.远程获取用户头像缩略图
 * 4.远程获取微博图片缩略图
 * Created by Administrator on 2017/9/21.
 */

public class IOUtil {

    private final static String CLASS_NAME = IOUtil.class.getSimpleName()+"-->";
    private static HashMap<String,Integer> requestUrlMap;
    private IOUtil(){}

    private static void initIOUtil(){
        if(requestUrlMap == null){
            requestUrlMap = new HashMap<>();
        }
    }

    /**
     * 远程获取图片
     * @param context  活动上下文
     * @param taskId   远程任务编号
     * @param pictureUrl  图片资源路径
     * @param width    指定图片的宽度
     * @param height   指定图片的高度
     */
    public static void getSampleBitmapRemote(final Context context, final int taskId, final String pictureUrl, final int pattern, final int width , final int height){
        initIOUtil();
        if (requestUrlMap.containsKey(pictureUrl)){
            return;
        }
        requestUrlMap.put(pictureUrl,0);
        LogUtil.d(CLASS_NAME+"当前下载图片线程数："+requestUrlMap.size());
        final OnLoadPictureTaskListener listener = (OnLoadPictureTaskListener) context;
        if(NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            requestUrlMap.remove(pictureUrl);
            listener.onLoadPictureError(taskId,C.err.network);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;
                Bitmap bitmap = null;
                HttpURLConnection connection = null;
                try{
                    listener.onLoadPictureStart(taskId);
                    url = new URL(pictureUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5*1000);
                    connection.setReadTimeout(5*1000);
                    connection.setDoInput(true);
                    InputStream inputStream = connection.getInputStream();
                    if(inputStream !=  null){
                        switch (pattern){
                            case Picture.PATTERN_ORIGINAL_IMAGE:
                                //下载原图
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                break;
                            case Picture.PATTERN_SAMPLE_CONTACT_ICON:
                                //下载头像缩略图
                                bitmap = getSampleContactIcon(context,inputStream,pictureUrl);
                                break;
                            case Picture.PATTERN_SAMPLE_BLOG_IMAGE:
                                //下载微博图片缩略图
                                bitmap = getSampleBlogImage(context,inputStream,pictureUrl);
                                break;
                            case Picture.PATTERN_SAMPLE_CUSTOM_IMAGE:
                                //下载自定义缩略图
                                bitmap = getSampleCustomImage(context,inputStream,pictureUrl,width,height);
                                break;
                        }
                        inputStream.close();
                        if(pattern != Picture.PATTERN_ORIGINAL_IMAGE){
                            //下载的图片不是原图，保存到内存区
                            MemoryUtil.addBitmap(pictureUrl,bitmap);
                        }
                        requestUrlMap.remove(pictureUrl);
                        listener.onLoadPictureComplete(taskId);
                    }
                }catch (ConnectException e){
                    requestUrlMap.remove(pictureUrl);
                    listener.onLoadPictureError(taskId,C.err.server);
                    LogUtil.d(CLASS_NAME+C.err.server);
                }catch (IOException e){
                    requestUrlMap.remove(pictureUrl);
                    listener.onLoadPictureError(taskId,"输入流错误");
                    LogUtil.d(CLASS_NAME+"输入流错误");
                }
            }
        }).start();
    }

    //远程获取图片
    public static void getBitmapRemote(final Context context, final int taskId, final String pictureUrl, final int pattern){
        getSampleBitmapRemote(context,taskId,pictureUrl,pattern,0,0);
    }

    //加载头像缩略图
    private static Bitmap getSampleContactIcon(Context context,InputStream inputStream,String pictureUrl){
        return getSampleCustomImage(context,inputStream,pictureUrl,Picture.CONTACT_ICON_WIDTH,Picture.CONTACT_ICON_HEIGHT);
    }

    //加载微博图片缩略图
    private static Bitmap getSampleBlogImage(Context context,InputStream inputStream,String pictureUrl){
        return getSampleCustomImage(context,inputStream,pictureUrl,Picture.BLOG_IMAGE_WIDTH,Picture.BLOG_IMAGE_HEIGHT);
    }

    /**
     * 加载自定义缩略图
     * @param inputStream   图片输入流
     * @param width   缩略图宽度
     * @param height  缩略图高度
     * @return
     */
    private static Bitmap getSampleCustomImage(Context context,InputStream inputStream,String pictureUrl, int width, int height){
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        SDUtil.saveImage(context,bitmap,pictureUrl);
        String fileName = SDUtil.getRealFileName(pictureUrl);
        inputStream.mark(0);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        int sampleRatio = SDUtil.getSampleRatio(options.outWidth,options.outHeight,width,height);
        BitmapFactory.decodeFile(fileName,options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleRatio;
        bitmap = BitmapFactory.decodeFile(fileName,options);
        if(bitmap == null){
            LogUtil.d(CLASS_NAME+"下载缩放图失败");
        }else {
            LogUtil.d(CLASS_NAME+"成功下载缩放图");
        }
        return bitmap;
    }

    /**
     * 远程下载图片回调接口
     */
    public interface OnLoadPictureTaskListener{
        //开始下载图片回调方法
        void onLoadPictureStart(int taskId);
        //下载图片出错回调方法
        void onLoadPictureError(int taskId,String error);
        //图片下载完成回调方法
        void onLoadPictureComplete(int taskId);
    }
}
