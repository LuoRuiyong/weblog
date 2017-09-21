package com.luoruiyong.weblog.util;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**内存存储管理，提供给AppCache使用
 * 本类对外功能：
 * 1.保存原片到本地
 * 2.从本地获取原图
 * 3.从本地获取头像缩略图
 * 4.从本地获取微博图片缩略图
 * 5.从本地获取自定义缩略图
 * 6.获取用户通过调用系统图库所选择图片的路径
 * 7.清除应用缓存
 * 8.获取图片缩放比例
 * 9.获取图片本地存储绝对路径
 * Created by Administrator on 2017/9/7.
 */

public class SDUtil {
    private static final String CLASS_NAME = SDUtil.class.getSimpleName() + "-->";

    /**
     * 保存图片
     * @param bitmap   图像位图
     * @param url  图片资源在服务器中的路径
     * @return  处理结果
     */
    public static boolean saveImage(Context context,Bitmap bitmap , String url){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((BaseUi)context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},BaseUi.REQUEST_PERMISSION_CODE);
            LogUtil.d(CLASS_NAME+"申请SD卡读写权限");
        }else{
            String fileName = getRealFileName(url);
            String type = getFileType(url);
            LogUtil.d(CLASS_NAME+"尝试保存图片");
            try {
                File file = new File(fileName);
                if(file.exists()){
                    file.delete();
                }
                FileOutputStream os = new FileOutputStream(file);
                if (type.equals(Picture.TYPE_PNG)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
                }else if(type.equals(Picture.TYPE_JEPG)){
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
                }
                os.flush();
                os.close();
                LogUtil.d(CLASS_NAME+"成功保存原图,路径："+fileName);
                return true;
            } catch (IOException e) {
                LogUtil.d(CLASS_NAME+"保存原图失败" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 获取图片位图
     * @param url 图片资源在服务器中的路径
     * @return  图片位图或null
     */
    public static Bitmap getOriginalImage(String url){
        String fileName = getRealFileName(url);
        LogUtil.d(CLASS_NAME+"尝试从本地获取原图："+fileName);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"从本地中获取原图失败");
            return null;
        }
        LogUtil.d(CLASS_NAME+"成功从本地中获取原图");
        return BitmapFactory.decodeFile(fileName);
    }

    //获取头像缩略图
    public static Bitmap getSampleContactIcon(String url){
        return getSampleCustomImage(url,Picture.CONTACT_ICON_WIDTH,Picture.CONTACT_ICON_HEIGHT);
    }

    //获取微博图片缩略图
    public static Bitmap getSampleBlogImage(String url){
        return getSampleCustomImage(url,Picture.BLOG_IMAGE_WIDTH,Picture.BLOG_IMAGE_HEIGHT);
    }

    /**
     * 自定义获取图片缩略图
     * @param url  图片资源在服务器中的路径
     * @param width   要求宽度
     * @param height  要求高度
     * @return  缩略图
     */
    public static Bitmap getSampleCustomImage(String url, int width, int height){
        String fileName = getRealFileName(url);
        LogUtil.d(CLASS_NAME+"尝试从本地获取缩放图："+fileName);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"从本地获取缩放图失败");
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName,options);
        int sampleRatio = getSampleRatio(options.outWidth,options.outHeight,width,height);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleRatio;
        LogUtil.d(CLASS_NAME+"成功从SD卡获取缩放图");
        return BitmapFactory.decodeFile(fileName,options);
    }

    /**
     * 处理系统图库选择数据，获取选择图片的路径
     * @param data   用户选择的图片信息
     * @return  图片路径
     */
    public static String chooseImage(Context context,Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(Build.VERSION.SDK_INT >= 19){
            //4.4及以上系统使用这个方法处理图片
            if(DocumentsContract.isDocumentUri(context,uri)){
                //如果是document类型的uri，则通过document id处理
                String docId = DocumentsContract.getDocumentId(uri);
                if("com.android.providers.media.documents".equals(uri.getAuthority())){
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "="+id;
                    imagePath = getImagePath(context,uri,selection);
                }else if("com.android.providers.download.documents".equals(uri.getAuthority())){
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                    imagePath  = getImagePath(context,contentUri,null);
                }
            }else if("content".equalsIgnoreCase(uri.getScheme())){
                //如果是content类型的uri，则使用普通方式处理
                imagePath = getImagePath(context,uri,null);
            }else if("file".equalsIgnoreCase(uri.getScheme())){
                //如果是file类型的uri，直接获取图片路径
                imagePath = uri.getPath();
            }
        }else{
            //4.4以下系统使用这个方法处理图片
            imagePath = getImagePath(context,uri,null);
        }
        LogUtil.d(CLASS_NAME+"图像资源路径解析结果："+imagePath);
        return imagePath;
    }

    //从内容提供器中获取指定的图片路径
    private static String getImagePath(Context context,Uri uri,String selection){
        LogUtil.d(CLASS_NAME+"通过uri从内容提供器中查询指定图片资源"+uri.toString());
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 清除图片缓存
     * @return  处理结果
     */
    public static boolean clearCache(){
        File imagesDir = new File(C.dir.cache);
        if(deleteDirWithFile(imagesDir)){
            LogUtil.d(CLASS_NAME+"成功清除缓存");
            return true;
        }else{
            LogUtil.d(CLASS_NAME+"清除缓存失败");
            return false;
        }
    }

    /**
     * 遍历删除当前文件夹，包括子文件和子文件夹
     * @param dir   待删除文件夹
     * @return  处理结果
     */
    private static boolean deleteDirWithFile(File dir) {
        if(dir == null || !dir.exists() || !dir.isDirectory()){
            return true;
        }
        //遍历当前文件夹
        for(File file :dir.listFiles()){
            if(file.isFile()){
                file.delete();     //删除文件
            }else{
                deleteDirWithFile(file);   //递归遍历子文件夹
            }
        }
        dir.delete();  //删除当前文件夹
        return true;
    }

    /**
     * 获取图片资源的真实路径（目录+文件名），提供给获取图片，保存图片等函数调用
     * @param url 图片资源路径
     * @return  真实路径
     */
    public static String getRealFileName(String url){
        String fileName = AppUtil.md5(url);
        String fileDir = C.dir.cache;
        File dir = new File(fileDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        fileName = fileDir + fileName;
        LogUtil.d(CLASS_NAME+"获取到的图片文件绝对路径："+fileName);
        return fileName;
    }

    /**
     * 通过图片资源路径，获取图片的格式
     * @param url  图片资源路径
     * @return  图片格式
     */
    private static String getFileType(String url) {
        String[] strarr = url.split("\\.");
        String type = strarr[strarr.length-1];
        LogUtil.d(CLASS_NAME+"获取到的图片文件类型："+type);
        return type;
    }

    /**
     * 获取图片缩放比例
     * @param realWidth  图片真实宽度
     * @param realHeight 图片真实高度
     * @param width    要求宽度
     * @param height   要求高度
     * @return   缩放比例
     */
    public static int getSampleRatio(int realWidth,int realHeight,int width,int height){
        int sampleRatio = 1;
        if(realWidth > width && realHeight > height){
            sampleRatio = Math.min(realWidth/width,realHeight/height);
        }
        LogUtil.d(CLASS_NAME+"图片缩放比例："+sampleRatio);
        return sampleRatio;
    }
}
