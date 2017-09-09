package com.luoruiyong.weblog.util;

import android.net.ParseException;
import android.text.TextUtils;

import com.luoruiyong.weblog.base.BaseMessage;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Customer;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;

/**常用的静态方法
 * Created by Administrator on 2017/9/9.
 */

public class AppUtil {
    private final static String CLASS_NAME = AppUtil.class.getSimpleName() + "-->";

    /**
     * 获取用户的会话ID;
     * @return 会话id
     */
    public static String getSessionId(){
        return Customer.getInstance().getSid();
    }

    /**
     * 加密文件名
     * @param url  资源文件在服务器中的路径
     * @return  加密后的固定长度的字符串
     */
    public static String md5(String url){
        String fileName = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(url.getBytes("UTF8"));
            byte[] data = md.digest();
            StringBuilder key = new StringBuilder();
            for(byte b : data){
                key.append(Integer.toHexString(0xFF & b));
            }
            fileName = key.toString();
            LogUtil.d(CLASS_NAME+"MD5加密后文件名："+fileName);
        } catch (NoSuchAlgorithmException e) {
            LogUtil.d(CLASS_NAME+"NoSuchAlgorithmException:" + e.getMessage());
        } catch (IOException e){
            LogUtil.d(CLASS_NAME+"IOException:" + e.getMessage());
        }
        return fileName;
    }

    /**
     * 初步解析json数据，存放在BaseMessage中
     * @param httpResult  服务器返回的数据
     * @return  BaseMessage对象
     * @throws Exception  json解析异常
     */
    public static BaseMessage getBaseMessageFromJson(String httpResult) throws Exception{
        try{
            JSONObject object = new JSONObject(httpResult);
            BaseMessage message = new BaseMessage();
            message.setCode(object.getString(BaseMessage.CODE));
            message.setMessage(object.getString(BaseMessage.MESSAGE));
            try{
                message.setResult(object.getString(BaseMessage.RESULT));
            }catch (Exception e){
                throw e;
            }
            return message;
        }catch (JSONException e) {
            throw new Exception(C.err.jsonFormat);
        }
    }

    /**
     * 首字符大写
     * @param str  待修改字符串
     * @return   修改结果
     */
    public static String ucFirst(String str){
        if(!TextUtils.isEmpty(str)){
            str = str.substring(0,1).toUpperCase()+str.substring(1);
        }
        return str;
    }

    /**
     * 解压服务器返回结果的主体
     * @param entity  结果内容
     * @param defaultCharset  编码
     * @return  结果字符串
     * @throws IOException
     * @throws ParseException
     */
    public static String gzipToString(final HttpEntity entity, final String defaultCharset) throws IOException, ParseException {
        if (entity == null) {
            throw new IllegalArgumentException("服务器返回的主体结果为空");
        }
        InputStream instream = entity.getContent();
        if (instream == null) {
            return "";
        }
        if (entity.getContentEncoding().getValue().contains("gzip")) {
            instream = new GZIPInputStream(instream);
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("服务器返回的主体过大，超出缓存内存");
        }
        int i = (int)entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = EntityUtils.getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public static String gzipToString(final HttpEntity entity)
            throws IOException, ParseException {
        return gzipToString(entity, null);
    }

}
