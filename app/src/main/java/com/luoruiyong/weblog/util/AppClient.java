package com.luoruiyong.weblog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.luoruiyong.weblog.base.C;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**网络通讯类,封装了Http请求
 * Created by Administrator on 2017/9/7.
 */

public class AppClient {
    private static final String CLASS_NAME = AppClient.class.getSimpleName() + "-->";
    private static final int CS_NONE = 0;
    private static final int CS_GZIP = 1;
    private static final int CONNECTIMEOUT = 10*1000;
    private static final int SOCKETTIMEOUT = 10*1000;
    private String charset = HTTP.UTF_8;    //默认编码
    private int compress = CS_NONE;   //默认压缩方式
    private String apiUrl;
    private Context context;
    private HttpClient httpClient;

    public AppClient(Context context,String apiUrl) {
        this.context = context;
        initUrl(apiUrl);
    }

    public AppClient(Context context,String apiUrl, String charset) {
        this.context = context;
        this.charset = charset;
        initUrl(apiUrl);
    }

    /**
     * 初始化资源路径，创建httpclient对象
     * @param apiUrl 接口路径资源
     */
    private void initUrl(String apiUrl) {
        this.apiUrl = apiUrl;
        String sid  = AppUtil.getSessionId();
        if(sid != null){
            this.apiUrl = this.apiUrl + "?sid=" + sid;
        }
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params,AppClient.CONNECTIMEOUT);
        HttpConnectionParams.setSoTimeout(params,AppClient.SOCKETTIMEOUT);
        httpClient = new DefaultHttpClient(params);
    }

    /**
     * get请求方法，无请求参数
     * @return  请求结果
     * @throws Exception  网络错误
     */
    public String get() throws Exception{
        if (NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        HttpResponse response = null;
        HttpGet httpGet = headerFilter(new HttpGet(this.apiUrl));
        try{
            LogUtil.d(CLASS_NAME+"GET请求："+apiUrl);
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = resultFilter(response.getEntity());
                LogUtil.d(CLASS_NAME+"GET请求结果:"+result);
                return result;
            }else{
                LogUtil.d(CLASS_NAME+"GET请求结果:服务器异常");
                throw new Exception(C.err.server);
            }
        }catch (Exception e) {
            LogUtil.d(CLASS_NAME+"请求异常："+C.err.network);
            throw new Exception(C.err.network);
        }
    }

    /**
     * post请求方法，有请求参数
     * @param params  请求参数
     * @return  请求结果
     * @throws Exception  网络错误
     */
    public String post(HashMap<String,String> params) throws Exception{
        if (NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        List<NameValuePair> postParams = new ArrayList<>();
        HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
        HttpResponse response ;
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(postParams,charset));
        try{
            LogUtil.d(CLASS_NAME+"POST请求："+apiUrl);
            LogUtil.d(CLASS_NAME+"POST请求参数："+postParams.toString());
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = resultFilter(response.getEntity());
                LogUtil.d(CLASS_NAME+"POST请求结果："+result);
                return result;
            }else{
                LogUtil.d(CLASS_NAME+"POST请求结果:服务器异常");
                throw new Exception(C.err.server);
            }
        }catch (Exception e) {
            LogUtil.d(CLASS_NAME+"请求异常："+C.err.network);
            throw new Exception(C.err.network);
        }
    }

    public String post(HashMap<String,String> params,List<NameValuePair> files) throws Exception{
        if(NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        List<NameValuePair> postParams = new ArrayList<>();
        HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
        HttpResponse response ;
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
        }
        MultipartEntity mpEntity = new MultipartEntity();
        StringBody stringBody;
        FileBody fileBody;
        File targetFile;
        String filePath;
        FormBodyPart fbp;
        for(NameValuePair param:postParams){
            stringBody = new StringBody(param.getValue(), Charset.forName("UTF-8"));
            fbp = new FormBodyPart(param.getName(),stringBody);
            mpEntity.addPart(fbp);
        }
        for(NameValuePair param:files){
            filePath = param.getValue();
            targetFile = new File(filePath);
            fileBody = new FileBody(targetFile,"octet-stream");
            fbp = new FormBodyPart(param.getName(),fileBody);
            mpEntity.addPart(fbp);
        }
        httpPost.setEntity(mpEntity);
        try{
            LogUtil.d(CLASS_NAME+"POST请求："+apiUrl);
            LogUtil.d(CLASS_NAME+"POST文件上传请求参数："+mpEntity.toString());
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = resultFilter(response.getEntity());
                LogUtil.d(CLASS_NAME+"POST文件上传请求结果："+ result);
                return result;
            }else{
                LogUtil.d(CLASS_NAME+"POST请求结果:服务器异常");
                throw new Exception(C.err.server);
            }
        }catch (Exception e) {
            LogUtil.d(CLASS_NAME+"请求异常："+C.err.network);
            throw new Exception(C.err.network);
        }
    }

    public Bitmap getImage() throws Exception{
        if (NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
        try{
            LogUtil.d(CLASS_NAME+"尝试从服务器获取图片："+apiUrl);
            HttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                if(entity.isStreaming()){
                    LogUtil.d(CLASS_NAME+"请求结果：成功");
                    InputStream inputStream = entity.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if(inputStream != null){
                        inputStream.close();
                    }
                    return bitmap;
                }else{
                    LogUtil.d(CLASS_NAME+"POST请求结果:服务器异常");
                    throw new Exception(C.err.server);
                }
            }else{
                LogUtil.d(CLASS_NAME+"POST请求结果:服务器异常");
                throw new Exception(C.err.server);
            }
        }catch (Exception e) {
            LogUtil.d(CLASS_NAME+"请求异常："+C.err.network);
            throw new Exception(C.err.network);
        }
    }

    public Bitmap getImage(HashMap<String ,String > params) throws Exception{
        if (NetworkUtil.getNetworkState(context) == NetworkUtil.TYPE.NONE){
            throw new Exception(C.err.network);
        }
        List<NameValuePair> postParams = new ArrayList<>();
        HttpPost httpPost = headerFilter(new HttpPost(this.apiUrl));
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(postParams,charset));
        try{
            LogUtil.d(CLASS_NAME+"尝试从服务器获取图片："+apiUrl);
            LogUtil.d(CLASS_NAME+"请求参数："+postParams.toString());
            HttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                if(entity.isStreaming()){
                    LogUtil.d(CLASS_NAME+"请求结果：成功");
                    InputStream inputStream = entity.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    if(inputStream != null){
                        inputStream.close();
                    }
                    return bitmap;
                }else{
                    LogUtil.d(CLASS_NAME+"GET请求结果:服务器异常");
                    throw new Exception(C.err.server);
                }
            }else{
                LogUtil.d(CLASS_NAME+"GET请求结果:服务器异常");
                throw new Exception(C.err.server);
            }
        }catch (Exception e) {
            LogUtil.d(CLASS_NAME+"请求异常："+C.err.network);
            throw new Exception(C.err.network);
        }
    }

    /**
     * 添加HttpGet请求头部
     * @param httpGet
     * @return
     */
    private HttpGet headerFilter(HttpGet httpGet){
        switch (this.compress){
            case CS_GZIP:
                httpGet.addHeader("Accept-Encoding","gzip");
                break;
            case CS_NONE:
                //do nothing
                break;
        }
        return  httpGet;
    }

    /**
     * 添加HttpGet请求头部
     * @param httpPost
     * @return
     */
    private HttpPost headerFilter(HttpPost httpPost){
        switch (this.compress){
            case CS_GZIP:
                httpPost.addHeader("Accept-Encoding","gzip");
                break;
            case CS_NONE:
                //do nothing
                break;
        }
        return  httpPost;
    }

    /**
     * 结果处理
     * @param entity  服务器返回的内容主体
     * @return  字符串类型的结果
     */
    private String resultFilter(HttpEntity entity){
        String result = null;
        switch (this.compress){
            case CS_NONE:
                try {
                    result = EntityUtils.toString(entity);
                }catch (IOException e){
                    LogUtil.d(CLASS_NAME+"IOException:"+e.getMessage());
                }
                break;
            case CS_GZIP:
                try{
                    result = AppUtil.gzipToString(entity,this.charset);
                }catch(Exception e){
                    LogUtil.d(CLASS_NAME+"Exception:"+e.getMessage());
                }
                break;
        }
        return result;
    }
}
