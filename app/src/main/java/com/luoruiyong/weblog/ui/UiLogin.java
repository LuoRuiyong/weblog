package com.luoruiyong.weblog.ui;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseAuth;
import com.luoruiyong.weblog.base.BaseMessage;
import com.luoruiyong.weblog.base.BaseModel;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.Customer;
import com.luoruiyong.weblog.model.Picture;
import com.luoruiyong.weblog.util.AppUtil;
import com.luoruiyong.weblog.util.LogUtil;
import com.luoruiyong.weblog.util.NetworkUtil;
import com.luoruiyong.weblog.util.SDUtil;

import java.util.HashMap;

/**登录界面控制器
 * Created by Administrator on 2017/9/7.
 */

public class UiLogin extends BaseUi{
    private static final String CLASS_NAME = UiLogin.class.getSimpleName() + "-->";
    private Button btn_login;
    private EditText et_account;
    private EditText et_password;
    private ImageView iv_user_icon;
    private CheckBox cb_remember_pass;
    private TextView tv_forget_pass;
    private TextView tv_error_tips;
    private LinearLayout ll_error_layout;
    private RelativeLayout rl_progressbar_layout;
    private MyOnClickListener onClickListener;
    private MyOnFocusChangeListener focusChangeListener;
    private Toolbar toolbar;

    private String account ;   //记录用户账号
    private String oldAccount;  //记录用户账号，用于判断账号是否被修改
    private String password;   //记录用户密码
    private Picture picture;   //记录头像信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        LogUtil.d(CLASS_NAME+"界面初始化");
        setToorBar();
        bindControl();
        recoverData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(CLASS_NAME+"销毁活动");
    }

    //初始化菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }

    //菜单栏响应事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign:
                LogUtil.d(CLASS_NAME+"跳转到用户注册界面");
                overlay(UiSign.class);
                break;
        }
        return true;
    }

    //设置状态栏
    private void setToorBar(){
        LogUtil.d(CLASS_NAME+"初始化状态栏");
        toolbar = (Toolbar) findViewById(R.id.toorbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
    }

    //绑定控件，并添加相应的监听事件
    private void bindControl() {
        LogUtil.d(CLASS_NAME+"绑定控件");
        btn_login = (Button) findViewById(R.id.login);
        et_account = (EditText) findViewById(R.id.account);
        et_password = (EditText) findViewById(R.id.password);
        cb_remember_pass = (CheckBox) findViewById(R.id.remember_pass);
        tv_forget_pass = (TextView) findViewById(R.id.forget_pass);
        tv_error_tips = (TextView) findViewById(R.id.error_tips);
        iv_user_icon = (ImageView) findViewById(R.id.user_icon);
        ll_error_layout = (LinearLayout) findViewById(R.id.error_layout);
        rl_progressbar_layout = (RelativeLayout) findViewById(R.id.layout_progressbar);
        //添加监听事件
        onClickListener = new MyOnClickListener();
        btn_login.setOnClickListener(onClickListener);
        tv_forget_pass.setOnClickListener(onClickListener);
        focusChangeListener = new MyOnFocusChangeListener();
        et_account.setOnFocusChangeListener(focusChangeListener);
        et_password.setOnFocusChangeListener(focusChangeListener);
        MyTextWatcher textWatcher = new MyTextWatcher();
        et_account.addTextChangedListener(textWatcher);
        et_password.addTextChangedListener(textWatcher);
    }

    /**
     * 如果存在数据记录，则恢复数据
     */
    private void recoverData(){
        account = "";
        oldAccount = "";
        password = "";
        picture = new Picture();
        SharedPreferences spf = getPreferences(Context.MODE_PRIVATE);
        boolean remember = spf.getBoolean("remember",false);
        if(remember){
            LogUtil.d(CLASS_NAME+"恢复数据");
            account = spf.getString("account_normal","");
            password = spf.getString("password","");
            oldAccount = account;
            et_account.setText(oldAccount);
            et_password.setText(password);
            picture.setUrl(spf.getString("url",""));
            picture.setType(spf.getString("type",""));
        }
        cb_remember_pass.setChecked(remember);
        //此处需要进行SD卡的读写操作，6.0以上需要动态申请权限
        if(ContextCompat.checkSelfPermission(UiLogin.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UiLogin.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }else{
            //应用拥有读写权限，尝试加载头像
            if(AppUtil.checkLoginAccount(account) && TextUtils.isEmpty(picture.getUrl())){
                //记录数据中不存在头像url，联网获取url，并下载新头像
                doAsyncTask(C.task.getUserIconUrl,C.api.getUserIconUrl,0);
            }else{
                //通过url加载头像
                loadUserIcon();
            }
        }
    }

    /**
     * 用户信息处理
     * @param remember  是否记住用户登录信息
     */
    private void dealWithRecord(boolean remember){
        SharedPreferences.Editor editor = getPreferences(Context.MODE_PRIVATE).edit();
        if(remember){
            //记住密码
            editor.putString("account_normal",et_account.getText().toString().trim());
            editor.putString("password",et_password.getText().toString().trim());
            editor.putString("url",picture.getUrl());
            editor.putString("type",picture.getType());
            LogUtil.d(CLASS_NAME+"记住密码");
        }else{
            //不记住密码
            editor.putString("account_normal","");
            editor.putString("password","");
            editor.putString("url","");
            editor.putString("type","");
            LogUtil.d(CLASS_NAME+"不记住密码");
        }
        editor.putBoolean("remember",remember);
        editor.commit();
    }

    /**
     * 焦点状态框监视器
     */
    private class MyOnFocusChangeListener implements View.OnFocusChangeListener {
        @Override
        public void onFocusChange(View view, boolean b) {
            if(!b){
                //账号输入框失去焦点的时候，初步检测账号并尝试获取头像资源
                account = et_account.getText().toString().trim();
                boolean account_tag = AppUtil.checkLoginAccount(account);
                if (account_tag){
                    //初步检测账号有效
                    if((account.equals(oldAccount) && picture.getUrl().equals(""))
                            || !account.equals(oldAccount)) {
                        //内容发生改变，或此前头像的url为空，联网加载新头像
                        UiLogin.this.doAsyncTask(C.task.getUserIconUrl, C.api.getUserIconUrl, 0);
                    }
                }else if(!AppUtil.checkLoginAccount(account)){
                    //账号不符合要求，更换默认头像
                    iv_user_icon.setImageResource(R.drawable.user_icon_default);
                    picture.setUrl("");
                    picture.setType("");
                }
                oldAccount = account;
            }
        }
    }

    /**
     * 点击事件监视器
     */
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    //用户登录
                    login();
                    break;
                case R.id.forget_pass:
                    //找回密码，需要先进行身份验证
                    LogUtil.d(CLASS_NAME+"跳转身份验证界面");
                    overlay(UiAuthentication.class);
                    break;
            }
        }
    }

    /**
     * 输入框内容监听器
     */
    private class MyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //当用户将要编辑时，隐藏上一次的错误提示
            ll_error_layout.setVisibility(View.GONE);
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {}
    }


    /**
     * 尝试登录
     */
    private void login() {
        if(NetworkUtil.getNetworkState(this) == NetworkUtil.TYPE.NONE){
            toast("无网络，请稍后再试");
            return;
        }
        account = et_account.getText().toString().trim();
        password = et_password.getText().toString().trim();
        boolean account_tag = AppUtil.checkLoginAccount(account);
        boolean password_tag = AppUtil.checkPassword(password);
        if(account_tag && password_tag){
            //初步验证账号和密码成功，联网登录
            dealWithRecord(cb_remember_pass.isChecked());   //根据用户要求保存信息
            HashMap<String,String> params = new HashMap<>();  //添加登录请求参数
            params.put("name",account);
            params.put("pass",password);
            LogUtil.d(CLASS_NAME+"联网用户登录");
            rl_progressbar_layout.setVisibility(View.VISIBLE);   //进度条显示
            doAsyncTask(C.task.login,C.api.login,params,0);      //联网登录任务
        }else{
            //账号或密码不正确
            if(!account_tag){
                if(account.equals("")){
                    tv_error_tips.setText("账号不能为空，请重新输入");
                }else{
                    tv_error_tips.setText("用户不存在，请确认后重新输入");
                }
                iv_user_icon.setImageResource(R.drawable.user_icon_default);
                picture.setUrl("");
                picture.setType("");
            }else{
                if (password.equals("")) {
                    tv_error_tips.setText("密码能为空，请重新输入");
                }else{
                    tv_error_tips.setText("密码错误，请确认后重新输入");
                }
            }
            ll_error_layout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 异步请求完成后回调函数
     * @param taskId  任务编号
     * @param message  结果数据
     */
    @Override
    public void onCompleteTask(int taskId, BaseMessage message) {
        LogUtil.d(CLASS_NAME+"回调任务完成方法，任务编号："+taskId);
        rl_progressbar_layout.setVisibility(View.GONE);
        switch (taskId){
            case C.task.getUserIconUrl:
                //获取头像url处理结果返回
                try {
                    Picture temp = (Picture) message.getResult(BaseModel.PICTURE);
                    picture.setUrl(temp.getUrl());
                    picture.setType(temp.getType());
                    HashMap<String,String> map = new HashMap<>();
                    map.put("account_normal",account);
                    LogUtil.d(CLASS_NAME+"尝试下载头像");
                    doAsyncTask(C.task.getUserIcon,picture.getUrl(),map,0);
                } catch (Exception e) {
                    LogUtil.d(CLASS_NAME+"异常："+e.getMessage());
                    toast(e.getMessage());
                }
                break;
            case C.task.login:
                //登录处理结果返回
                try {
                    Customer customer = (Customer) message.getResult(BaseModel.CUSTOMER);
                    BaseAuth.setCustomer(customer);
                    if(TextUtils.isEmpty(customer.getNickName())){
                        BaseAuth.setLogin(false);
                        tv_error_tips.setText("账号或用户名不正确，请确认后重新输入");
                        ll_error_layout.setVisibility(View.VISIBLE);
                        LogUtil.d("登录失败");
                    }else{
                        BaseAuth.setLogin(true);
                        toast("登录成功");
                        LogUtil.d("登录成功  ");
                        LogUtil.d(CLASS_NAME+"用户信息："+customer.toString());
                        forward(UiIndex.class);
                    }
                } catch (Exception e) {
                    LogUtil.d(CLASS_NAME+"异常："+e.getMessage());
                    toast(e.getMessage());
                }
                break;
        }
    }

    //图片获取完成回调函数
    @Override
    public void onCompleteTask(int taskId) {
        switch (taskId){
            case C.task.getUserIcon:
                //此处需要进行SD卡的读写操作，需要动态申请权限
                if(ContextCompat.checkSelfPermission(UiLogin.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UiLogin.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                    LogUtil.d("申请内存读写权限");
                }else{
                    //应用用于读写权限，加载头像
                    loadUserIcon();
                }
                break;
        }

    }

    /**
     * 异步任务出现网络异常时回调方法
     * @param taskId  任务编号
     * @param errorInfo 异常信息
     */
    @Override
    public void onNetworkError(int taskId, String errorInfo) {
        LogUtil.d(CLASS_NAME+ errorInfo);
        rl_progressbar_layout.setVisibility(View.GONE);
        if(taskId == C.task.login){
            toast(errorInfo);
        }
    }

    //权限申请结果回调函数
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LogUtil.d("成功申请内存读写权限");
                    loadUserIcon();
                }else{
                    LogUtil.d("用户拒绝应用使用内存读写权限");
                    toast("您拒绝了应用使用储存权限，无法保存头像信息");
                }
        }
    }

    /**
     * 加载头像
     */
    private void loadUserIcon(){
        if(picture != null && !TextUtils.isEmpty(picture.getUrl())){
            Bitmap bitmap = SDUtil.getImage(picture.getUrl(),picture.getType());
            if(bitmap == null){
                //从SD卡获取失败，联网下载
                HashMap<String,String> map = new HashMap<>();
                map.put("account_normal",account);
                doAsyncTask(C.task.getUserIcon,picture.getUrl(),map,0);
            }else{
                //获取成功，显示
                iv_user_icon.setImageBitmap(bitmap);
            }
        }
    }
}
