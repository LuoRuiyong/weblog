package com.luoruiyong.weblog.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.luoruiyong.weblog.base.C;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
     * @param fileName  真实文件名
     * @return  处理结果
     */
    public static boolean saveImage(Bitmap bitmap , String fileName, int type){
        fileName = getRealFileName(fileName,type);
        try {
            File file = new File(fileName);
            file.createNewFile();
            OutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,os);
            os.flush();
            os.close();
            LogUtil.d(CLASS_NAME+"保存图片成功");
            return true;
        } catch (IOException e) {
            LogUtil.d(CLASS_NAME+"保存图片失败");
        }
        return false;
    }

    /**
     * 获取图片位图
     * @param fileName 文件名
     * @param type   资源类型
     * @return  图片位图或null
     */
    public static Bitmap getImage(String fileName,int type){
        fileName = getRealFileName(fileName,type);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"获取图片失败");
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        LogUtil.d(CLASS_NAME+"成功获取图片");
        return BitmapFactory.decodeFile(fileName,options);
    }

    /**
     * 获取图片缩略图，防止出现OOM
     * @param fileName  文件名
     * @param type   资源类型
     * @param width   要求宽度
     * @param height  要求高度
     * @return  缩略图
     */
    public static Bitmap getSampleImage(String fileName,int type,int width,int height){
        fileName = getRealFileName(fileName,type);
        File file = new File(fileName);
        if(!file.exists()){
            LogUtil.d(CLASS_NAME+"获取缩放图片失败");
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName,options);
        int sampleRatio = getSampleRatio(options.outWidth,options.outHeight,width,height);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleRatio;
        LogUtil.d(CLASS_NAME+"成功获取缩放图片");
        return BitmapFactory.decodeFile(fileName,options);
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
    private static String getRealFileName(String fileName,int type){
        String fileDir = "";
        switch (type){
            case USER_ICON:
                fileDir = C.dir.icons;
                break;
            case BLOG_IMAGE:
                fileDir = C.dir.images;
                break;
        }
        File dir = new File(fileDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        LogUtil.d(CLASS_NAME+"获取文件绝对路径："+fileDir+fileName);
        return fileDir + fileName;
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
