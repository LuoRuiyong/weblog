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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            LogUtil.d(CLASS_NAME+"URL通过MD5加密后得到的文件名："+fileName);
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
        LogUtil.d(CLASS_NAME+"尝试将JSON数据转换为BaseMessage对象数据");
        BaseMessage message = new BaseMessage();
        try{
            JSONObject object = new JSONObject(httpResult);
            message.setCode(object.getString(BaseMessage.CODE));
            message.setMessage(object.getString(BaseMessage.MESSAGE));
            try{
                message.setResult(object.getString(BaseMessage.RESULT));
            }catch (Exception e){
                throw e;
            }
            LogUtil.d(CLASS_NAME+"成功将JSON数据转换为BaseMessage对象数据");
            return message;
        }catch (JSONException e) {
            LogUtil.d(CLASS_NAME+"将JSON数据转换为BaseMessage对象数据失败");
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
     * 密码检测
     * @param password   用户输入的密码
     * @return  是否符合规则
     */
    public static boolean checkPassword(String password){
        Pattern pattern = Pattern.compile(C.pattern.password); // 编译正则表达式
        Matcher matcher = pattern.matcher(password); //匹配
        boolean tag = matcher.matches();
        if(tag){
            LogUtil.d(CLASS_NAME+"密码检测，"+password+"为合法密码");
        }else {
            LogUtil.d(CLASS_NAME+"密码检测，"+password+"为非法密码");
        }
        return tag;  //匹配结果
    }

    /**
     * 初步验证登录账户 ，
     * 1、匹配默认账户规则，
     * 2、匹配手机号码规则，
     * 3、匹配电子邮箱规则
     * @param account  用户输入的账户信息，
     * @return  是否符合规则
     */
    public static boolean checkLoginAccount(String account){
        String message = "";
        Pattern pattern = Pattern.compile(C.pattern.accountDefault); // 编译正则表达式
        Matcher matcher = pattern.matcher(account); //匹配默认账户规则
        boolean tag = matcher.matches();
        if(!tag){
            pattern = Pattern.compile(C.pattern.cellNumber); // 编译正则表达式
            matcher= pattern.matcher(account); //匹配手机号
            tag = matcher.matches();
           if(!tag){
               pattern = Pattern.compile(C.pattern.email); //匹配邮箱
               matcher = pattern.matcher(account);
               tag = matcher.matches();
               if(!tag){
                   message = "非法账号";
               }else{
                   message = "邮箱账号";
               }
           }else{
               message = "手机号吗";
           }
        }else{
            message = "用户名";
        }
        LogUtil.d(CLASS_NAME+"初步检测，账号"+account+"为"+message);
        return tag;
    }

    /**
     * 初步验证注册账户
     * @param account
     * @return
     */
    public static boolean checkAccount(String account){
        Pattern pattern = Pattern.compile(C.pattern.accountDefault); // 编译正则表达式
        Matcher matcher = pattern.matcher(account); //匹配默认账户规则
        boolean tag = matcher.matches();
        if(tag){
            LogUtil.d(CLASS_NAME+"初步检测，账号"+account+"为合法法账号");
        }else {
            LogUtil.d(CLASS_NAME+"初步检测，账号"+account+"为非法账号");
        }
        return tag;  //匹配结果
    }

    /**
     * 初步验证昵称
     * @param nickName
     * @return
     */
    public static boolean checkNickName(String nickName){
        Pattern pattern = Pattern.compile(C.pattern.nickName); // 编译正则表达式
        Matcher matcher = pattern.matcher(nickName); //匹配默认账户规则
        boolean tag = matcher.matches();
        if(tag){
            LogUtil.d(CLASS_NAME+"初步检测，昵称"+nickName+"为合法法昵称");
        }else {
            LogUtil.d(CLASS_NAME+"初步检测，昵称"+nickName+"为非法昵称");
        }
        return tag;  //匹配结果
    }



    public static boolean checkCellNumber(String cellNumber){
        Pattern pattern = Pattern.compile(C.pattern.cellNumber); // 编译正则表达式
        Matcher matcher = pattern.matcher(cellNumber); //匹配默认账户规则
        boolean tag = matcher.matches();
        if(tag){
            LogUtil.d(CLASS_NAME+"初步检测，手机号码"+cellNumber+"为合法手机号码");
        }else {
            LogUtil.d(CLASS_NAME+"初步检测，手机号码"+cellNumber+"为非法手机号码");
        }
        return tag;  //匹配结果
    }

    public static boolean checkEmail(String email){
        Pattern pattern = Pattern.compile(C.pattern.email); // 编译正则表达式
        Matcher matcher = pattern.matcher(email); //匹配默认账户规则
        boolean tag = matcher.matches();
        if(tag){
            LogUtil.d(CLASS_NAME+"初步检测，邮箱"+email+"为合法邮箱");
        }else {
            LogUtil.d(CLASS_NAME+"初步检测，邮箱"+email+"为非法邮箱");
        }
        return tag;  //匹配结果
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
