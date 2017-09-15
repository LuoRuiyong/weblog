package com.luoruiyong.weblog.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**内存存储管理，提供给AppCache使用
 * Created by Administrator on 2017/9/7.
 */

public class SDUtil {
    private static final String CLASS_NAME = SDUtil.class.getSimpleName() + "-->";
    public static final int USER_ICON = 0;
    public static final int BLOG_IMAGE = 1;

    /**
     * 保存图片
     * @param bitmap   图像位图
     * @param url  图片资源在服务器中的路径
     * @return  处理结果
     */
    public static boolean saveImage(Bitmap bitmap , String url, String type){
        String fileName = AppUtil.md5(url);
        fileName = getRealFileName(fileName,type);
        try {
            File file = new File(fileName);
            if(file.exists()){
                file.delete();
            }
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
            os.close();
            LogUtil.d(CLASS_NAME+"保存图片成功,路径："+fileName);
            return true;
        } catch (IOException e) {
            LogUtil.d(CLASS_NAME+"保存图片失败" + e.getMessage());
        }
        return false;
    }

    /**
     * 获取图片位图
     * @param url 图片资源在服务器中的路径
     * @param type   资源类型
     * @return  图片位图或null
     */
    public static Bitmap getImage(String url,String type){
        String fileName = AppUtil.md5(url);
        fileName = getRealFileName(fileName,type);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"从SD卡中获取图片失败");
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        LogUtil.d(CLASS_NAME+"成功从SD卡中获取图片");
        return BitmapFactory.decodeFile(fileName,options);
    }

    /**
     * 获取图片缩略图，防止出现OOM
     * @param url  图片资源在服务器中的路径
     * @param type   资源类型
     * @param width   要求宽度
     * @param height  要求高度
     * @return  缩略图
     */
    public static Bitmap getSampleImage(String url,String type,int width,int height){
        String fileName = AppUtil.md5(url);
        fileName = getRealFileName(fileName,type);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"从SD卡获取缩放图片失败");
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName,options);
        int sampleRatio = getSampleRatio(options.outWidth,options.outHeight,width,height);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleRatio;
        LogUtil.d(CLASS_NAME+"成功从SD卡获取缩放图片");
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
    public static boolean ClearCache(){
        File imagesDir = new File(C.dir.images);
        File iconDir = new File(C.dir.icons);
        if(deleteDirWithFile(imagesDir) && deleteDirWithFile(iconDir)){
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
     * @param fileName  文件名
     * @param type   资源类型
     * @return  真实路径
     */
    private static String getRealFileName(String fileName,String type){
        String fileDir = "";
        if(type.equals(Picture.TYPE_ICON)) {
            fileDir = C.dir.icons;
        }else if(type.equals(Picture.TYPE_IMAGE)){
            fileDir = C.dir.images;
        }
        File dir = new File(fileDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        fileName = fileDir + fileName + ".png";
        LogUtil.d(CLASS_NAME+"获得图片文件绝对路径："+fileName);
        return fileName;
    }

    /**
     * 获取图片缩放比例
     * @param realWidth  图片真实宽度
     * @param realHeight 图片真实高度
     * @param width    要求宽度
     * @param height   要求高度
     * @return   缩放比例
     */
    private static int getSampleRatio(int realWidth,int realHeight,int width,int height){
        int sampleRatio = 1;
        if(realWidth > width && realHeight > height){
            sampleRatio = Math.min(realWidth/width,realHeight/height);
        }
        LogUtil.d(CLASS_NAME+"图片缩放比例："+sampleRatio);
        return sampleRatio;
    }
}
