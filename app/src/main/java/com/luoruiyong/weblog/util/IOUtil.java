package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Picture;

import java.io.ByteArrayOutputStream;
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
    private static HashMap<String,Integer> requestUrlMap;  //url请求记录图
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
    public static void getBitmapRemote(final Context context, final int taskId, final String pictureUrl, final int width , final int height){
        initIOUtil();
        if (requestUrlMap.containsKey(pictureUrl)){
            //如果下载正在进行中，不再开启新线程
            return;
        }
        //添加请求记录
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
                InputStream inputStream = null;
                HttpURLConnection connection = null;
                try{
                    listener.onLoadPictureStart(taskId);
                    url = new URL(pictureUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setConnectTimeout(5*1000);
                    connection.setReadTimeout(5*1000);
                    connection.setDoInput(true);
                    inputStream = connection.getInputStream();
                    if(inputStream !=  null){
                        switch (taskId){
                            case C.task.getOriginalImage:
                                //下载原图
                                bitmap = BitmapFactory.decodeStream(inputStream);
                                break;
                            case C.task.getSampleContactIcon:
                                //下载头像缩略图
                                bitmap = getSampleContactIcon(context,inputStream,pictureUrl);
                                break;
                            case C.task.getSampleBlogImage:
                                //下载微博图片缩略图
                                bitmap = getSampleBlogImage(context,inputStream,pictureUrl);
                                break;
                            case C.task.getSampleCustomImage:
                            case C.task.getSampleFullScreenImage:
                                //下载自定义缩略图
                                bitmap = getSampleCustomImage(context,inputStream,pictureUrl,width,height);
                                break;
                        }
                        if(bitmap == null){
                            //下载失败
                            listener.onLoadPictureError(taskId,C.err.stream);
                        }else {
                            //下载成功
                            if(taskId == C.task.getOriginalImage){
                                //下载原图,保存到本地download目录下
                                SDUtil.saveDownloadImage(context,bitmap,pictureUrl);
                            }else if(taskId == C.task.getSampleFullScreenImage){
                                //下载全屏缩略图，保存到另一个缓存区
                                MemoryUtil.addFullScreenBitmap(pictureUrl,bitmap);
                            }else{
                                //下载缩略图，保存到内存和本地cache目录下
                                MemoryUtil.addBitmap(pictureUrl,bitmap);
                                SDUtil.saveCacheImage(context,bitmap,pictureUrl);
                            }
                            listener.onLoadPictureComplete(taskId,pictureUrl);
                        }
                    }
                }catch (ConnectException e){
                    listener.onLoadPictureError(taskId,C.err.server);
                }catch (IOException e){
                    listener.onLoadPictureError(taskId,C.err.stream);
                }finally {
                    //移除图片请求记录
                    requestUrlMap.remove(pictureUrl);
                    if(inputStream != null){
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    //远程获取图片
    public static void getBitmapRemote(final Context context, final int taskId, final String pictureUrl){
        getBitmapRemote(context,taskId,pictureUrl,0,0);
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
        byte[] data = getBytesFromInputStream(inputStream);
        if(data != null){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data,0,data.length,options);
            options.inSampleSize = SDUtil.getSampleRatio(options.outWidth,options.outHeight,width,height);
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length,options);
            if(bitmap == null){
                LogUtil.d(CLASS_NAME+"下载缩放图失败");
            }else {
                LogUtil.d(CLASS_NAME+"成功下载缩放图");
                MemoryUtil.addBitmap(pictureUrl,bitmap);
                SDUtil.saveCacheImage(context,bitmap,pictureUrl);
                return bitmap;
            }
        }
        return null;
    }

    /**
     * 将输入流转化为字节数组
     * @param inputStream 图片输入流
     * @return  图片字节数组
     */
    private static byte[] getBytesFromInputStream(InputStream inputStream){
        try
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int len;
            while ((len = inputStream.read(buffer) )!= -1){
                 bos.write(buffer,0,len);
            }
            bos.flush();
            bos.close();
            LogUtil.d(CLASS_NAME+"成功将流转化为字节数组");
            return bos.toByteArray();
        }catch (IOException e){
            LogUtil.d(CLASS_NAME+"将流转化为字节数组发生错误");
            return null;
        }
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
        void onLoadPictureComplete(int taskId,String url);
    }
}
