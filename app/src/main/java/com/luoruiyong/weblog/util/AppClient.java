package com.luoruiyong.weblog.util;

import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Customer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**网络通讯类
 * Created by Administrator on 2017/9/7.
 */

public class AppClient {

    private static final int CS_NONE = 0;
    private static final int CS_GZIP = 1;
    private static final int CONNECTIMEOUT = 2*1000;
    private static final int SOCKETTIMEOUT = 2*1000;
    private String charset = HTTP.UTF_8;    //默认编码
    private int compress = CS_NONE;   //默认压缩方式
    private String apiUrl;
    private HttpClient httpClient;

    public AppClient(String apiUrl) {
        initUrl(apiUrl);
    }

    public AppClient(String apiUrl, String charset) {
        this.charset = charset;
        initUrl(apiUrl);
    }

    /**
     * 初始化资源路径，创建httpclient对象
     * @param apiUrl 接口路径资源
     */
    private void initUrl(String apiUrl) {
        this.apiUrl = C.api.base + apiUrl;
        String sid  = Customer.getInstance().getSid();
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
        HttpResponse response = null;
        HttpGet httpGet = new HttpGet(this.apiUrl);
        try{
            LogUtil.d("AppClient.get.url" ,this.apiUrl);
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(response.getEntity());
                LogUtil.d("AppClient.get.result" ,result);
                return result;
            }else{
                return null;
            }
        }catch (ConnectionPoolTimeoutException e){
            throw new Exception(C.err.network);
        }catch (IOException e){
            LogUtil.d("Exception",e.getMessage());
        }
        return null;
    }

    /**
     * post请求方法，有请求参数
     * @param params  请求参数
     * @return  请求结果
     * @throws Exception  网络错误
     */
    public String post(HashMap<String,String> params) throws Exception{
        List<NameValuePair> postParams = new ArrayList<>();
        HttpPost httpPost = new HttpPost(this.apiUrl);
        HttpResponse response ;
        Iterator iterator = params.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry) iterator.next();
            postParams.add(new BasicNameValuePair(entry.getKey().toString(),entry.getValue().toString()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(postParams,charset));
        try{
            LogUtil.d("AppClient.post.url" ,this.apiUrl);
            LogUtil.d("AppClient.post.params" ,postParams.toString());
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result =  EntityUtils.toString(response.getEntity());
                LogUtil.d("AppClient.post.result" , result);
                return result;
            }else{
                return null;
            }
        }catch (ConnectTimeoutException e){
            throw new Exception(C.err.network);
        }catch (IOException e) {
            LogUtil.d("Exception", e.getMessage());
        }
        return null;
    }

    public String post(HashMap<String,String> params,List<NameValuePair> files) throws Exception{
        List<NameValuePair> postParams = new ArrayList<>();
        HttpPost httpPost = new HttpPost(this.apiUrl);
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
            LogUtil.d("AppClient.post.files.url" ,this.apiUrl);
            LogUtil.d("AppClient.post.files.params" ,mpEntity.toString());
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result =  EntityUtils.toString(response.getEntity());
                LogUtil.d("AppClient.post.files.result" , result);
                return result;
            }else{
                return null;
            }
        }catch (ConnectTimeoutException e){
            throw new Exception(C.err.network);
        }catch (IOException e) {
            LogUtil.d("Exception", e.getMessage());
        }
        return null;
    }

    /**
     * 添加HttpGet请求头部
     * @param httpGet
     * @return
     */
    private HttpGet headerFilter(HttpGet httpGet){
        switch (this.compress){
            case CS_GZIP:

                break;
            case CS_NONE:

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

                break;
            case CS_NONE:

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

                break;
            case CS_GZIP:

                break;
        }
        return result;
    }
}
