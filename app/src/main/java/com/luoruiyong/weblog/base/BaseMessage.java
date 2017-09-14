package com.luoruiyong.weblog.base;

import com.luoruiyong.weblog.util.AppUtil;
import com.luoruiyong.weblog.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**存放服务器返回的数据
 * Created by Administrator on 2017/9/10.
 */

public class BaseMessage {
    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String RESULT = "result";
    public static final String CLASS_NAME = BaseMessage.class.getSimpleName()+"-->";
    private String code;
    private String message;
    private String result;
    private Map<String,BaseModel> resultMap;  //存储单一模型数据，如用户信息
    private Map<String,ArrayList<BaseModel>> resultList;  //存储列表模型数据，如微博列表

    public BaseMessage() {
        resultMap = new HashMap<>();
        resultList = new HashMap<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    /**
     * 通过模板名获取模板对象
     * @param modelName 模板名
     * @return  模板对象
     * @throws Exception  空数据
     */
    public BaseModel getResult(String modelName) throws Exception {
        BaseModel model = resultMap.get(modelName);
        if(model == null){
            throw new Exception(C.err.noData);
        }
        return model;
    }

    /**
     * 通过模板名获取模板对象列表
     * @param modelName 模板名
     * @return 模板列表
     * @throws Exception 空数据
     */
    public List<BaseModel> getResultList(String modelName) throws Exception {
        ArrayList<BaseModel> list = resultList.get(modelName);
        if(list == null || list.size() == 0){
            throw new Exception(C.err.noData);
        }
        return list;
    }





    @Override
    public String toString() {
        return "[code="+code+" | message="+message+" | result="+result+"]";
    }

    /**
     * 存储结果数据，把json数据解析到对应的模型类中
     * @param result  服务器返回的真实数据
     */
    public void setResult(String result) throws Exception{
        LogUtil.d(CLASS_NAME+"尝试按结果中的模型类进行数据保存");
        this.result = result;
        if(result.length() > 0){
            try {
                JSONObject object = new JSONObject(result);
                Iterator<String> iterator = object.keys();
                while (iterator.hasNext()){
                    String key = iterator.next();
                    String modelName = getModelName(key);
                    String modelClassName = BaseModel.PACKAGE_NAME +modelName;
                    JSONArray jsonArray = object.optJSONArray(key);
                    if(jsonArray == null){
                        JSONObject jsonObject = object.optJSONObject(key);
                        if(jsonObject == null){
                            throw new Exception(C.err.noData);
                        }try{
                            this.resultMap.put(modelName,json2Model(modelClassName,jsonObject));
                        }catch (Exception e){
                            throw e;
                        }
                    }
                    else{
                        ArrayList<BaseModel> list = new ArrayList<>();
                        for(int i=0;i<jsonArray.length();i++){
                            list.add(json2Model(modelClassName,jsonArray.getJSONObject(i)));
                        }
                        this.resultList.put(modelName,list);
                    }
                }
                LogUtil.d(CLASS_NAME+"成功保存数据到相应模型类");
            }catch (JSONException e){
                LogUtil.d(CLASS_NAME+"JSON数据异常，无法保存数据到相应的模型类");
                LogUtil.d(CLASS_NAME+"异常信息："+e.getMessage());
                throw new Exception(C.err.jsonFormat);
            }
        }
    }

    /**
     * 通过反射，动态创建模型对象并赋值
     * @param className  完整类名
     * @param object  包含模型相应数据的json数据
     * @return  模型对象
     * @throws Exception  找不到指定类
     */
    private BaseModel json2Model(String className,JSONObject object) throws Exception {
        LogUtil.d(CLASS_NAME+"尝试动态加载模型类"+className+",并添加数据");
        try {
            BaseModel modelObj = (BaseModel) Class.forName(className).newInstance();
            Iterator<String> iterator = object.keys();
            while (iterator.hasNext()){
                String varField = iterator.next();
                String varValue = object.getString(varField);
                Field field = modelObj.getClass().getDeclaredField(varField);
                field.setAccessible(true);
                field.set(modelObj,varValue);
            }
            LogUtil.d(CLASS_NAME+"成功动态加载模型类，保存数据");
            return modelObj;
        } catch (Exception e) {
            LogUtil.d(CLASS_NAME+"动态加载"+className+"失败");
            LogUtil.d(CLASS_NAME+"异常信息："+e.getMessage());
            throw new Exception(C.err.modelError);
        }
    }

    /**
     * 获取模型名称
     * @param str  一个json单元
     * @return  模型名
     */
    private String getModelName(String str){
        String[] strarr = str.split("\\w");
        if(strarr.length > 0){
            str = strarr[0];
        }
        return AppUtil.ucFirst(str);
    }
}
