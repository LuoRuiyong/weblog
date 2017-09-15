package com.luoruiyong.weblog.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseMessage;
import com.luoruiyong.weblog.base.BaseModel;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.model.UniversalModel;
import com.luoruiyong.weblog.util.AppUtil;
import com.luoruiyong.weblog.util.LogUtil;
import com.luoruiyong.weblog.util.NetworkUtil;
import com.luoruiyong.weblog.util.SDUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import static com.luoruiyong.weblog.R.id.password_ok;

/**登录界面控制器
 * Created by Administrator on 2017/9/9.
 */

public class UiSign extends BaseUi{

    private static final String CLASS_NAME = UiSign.class.getSimpleName() + "-->";
    private ImageView iv_user_icon;
    private EditText et_account;
    private EditText et_nickname;
    private EditText et_password;
    private EditText et_conferm_password;
    private ImageView iv_account_ok;
    private ImageView iv_nickname_ok;
    private ImageView iv_password_ok;
    private ImageView iv_confirm_password_ok;
    private TextView tv_error_tips;
    private LinearLayout ll_error_layout;
    private RelativeLayout rl_progressbar_layout;
    private Button btn_sign;

    private String account;
    private String nickName;
    private String password;
    private String confirm_password;
    private String imagePath = null;   //用户选择的头像路径
    private boolean account_tag = false;   //账号有效性
    private boolean nickName_tag = false;  //昵称有效性
    private boolean password_tag = false;  //密码有效性
    private boolean confirm_password_tag = false;  //密码有效性
    private final static int CHOOSE_ICON = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_sign);
        LogUtil.d(CLASS_NAME+"界面初始化");
        setToolbar();
        bindControls();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(CLASS_NAME+"销毁活动");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                LogUtil.d(CLASS_NAME+"返回");
                this.finish();
                break;
        }
        return true;
    }

    /**
     * 绑定控件并添加相应的监听事件
     */
    private void bindControls() {
        LogUtil.d(CLASS_NAME+"绑定控件");
        et_account = (EditText) findViewById(R.id.account);
        et_nickname = (EditText) findViewById(R.id.nickname);
        et_password = (EditText) findViewById(R.id.password);
        et_conferm_password = (EditText) findViewById(R.id.confirm_password);
        iv_account_ok = (ImageView) findViewById(R.id.account_ok);
        iv_nickname_ok = (ImageView) findViewById(R.id.nickname_ok);
        iv_password_ok = (ImageView) findViewById(password_ok);
        iv_confirm_password_ok = (ImageView) findViewById(R.id.confirm_password_ok);
        iv_user_icon = (ImageView) findViewById(R.id.user_icon);
        btn_sign = (Button) findViewById(R.id.sign);
        tv_error_tips = (TextView) findViewById(R.id.error_tips);
        ll_error_layout = (LinearLayout) findViewById(R.id.error_layout);
        rl_progressbar_layout = (RelativeLayout) findViewById(R.id.layout_progressbar);
        //添加事件监听
        MyOnClickListener onClickListener = new MyOnClickListener();
        iv_user_icon.setOnClickListener(onClickListener);
        btn_sign.setOnClickListener(onClickListener);
        MyOnFocusChangeListener onFocusChangeListener = new MyOnFocusChangeListener();
        et_account.setOnFocusChangeListener(onFocusChangeListener);
        et_nickname.setOnFocusChangeListener(onFocusChangeListener);
        et_password.setOnFocusChangeListener(onFocusChangeListener);
        et_conferm_password.setOnFocusChangeListener(onFocusChangeListener);
        MyTextWatcher textWatcher = new MyTextWatcher();
        et_account.addTextChangedListener(textWatcher);
        et_nickname.addTextChangedListener(textWatcher);
        et_password.addTextChangedListener(textWatcher);
        et_conferm_password.addTextChangedListener(textWatcher);
    }

    /**
     * 设置状态栏的属性
     */
    private void setToolbar(){
        LogUtil.d(CLASS_NAME+"初始化状态栏");
        Toolbar toorbar = (Toolbar) findViewById(R.id.toorbar);
        toorbar.setTitle("注册");
        setSupportActionBar(toorbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);   //显示状态栏最左边的按钮，默认资源id为android.R.id.home
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    /**
     * 点击事件监听器
     */
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.sign:
                    //注册
                    sign();
                    break;
                case R.id.user_icon:
                    //选择本地图片，请求读写权限
                    if(ContextCompat.checkSelfPermission(UiSign.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(UiSign.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},CHOOSE_ICON);
                        LogUtil.d("申请内存读写权限");
                    }else {
                        //调用系统图库选择图片
                        openAlbum();
                    }
                    break;
            }
        }
    }

    /**
     * 打开系统图库
     */
    public void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        LogUtil.d("调用系统图库，选择本地图片作为头像");
        startActivityForResult(intent,CHOOSE_ICON);
    }

    /**
     * 输入框焦点监听器
     */
    private class MyOnFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                //输入框焦点失去事件，检测用户填写的信息是否有效
                checkUserInfo(view);
            }
        }
    }

    /**
     * 输入框内容监听器
     */
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //编辑之前，隐藏上一次的警告信息
            closeWarn();
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int i ,int i1, int i2) {
            //编辑时，隐藏相应的是否有效图标，正在编辑的内容设置为无效，失去焦点是会判断其是否有效
            if(et_account.isFocused()){
                iv_account_ok.setVisibility(View.GONE);
                account_tag = false;
            }else if(et_password.isFocused()){
                iv_password_ok.setVisibility(View.GONE);
                iv_confirm_password_ok.setVisibility(View.GONE);
                password_tag = false;
            }else if(et_nickname.isFocused()){
                iv_nickname_ok.setVisibility(View.GONE);
                nickName_tag = false;
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
            //检测确认密码是否与密码一致，同步检测
            if(et_conferm_password.isFocused()){
                checkConfirmPass();
            }
        }
    }

    /**
     * 检测用户输入的用户名、手机号、邮箱
     * @param view  事件源
     */
    private void checkUserInfo(View view) {
        switch (view.getId()){
            case R.id.account:
                //检测账号是否合理和是否已经被注册（联网）
                checkAccount();
                break;
            case R.id.nickname:
                //检测用户名是否合理和是否已经被注册（联网）
                checkNickName();
                break;
            case R.id.password:
                //检测密码是否合理（本地）
                checkPassword();
                break;
        }
    }

    /**
     * 检测账号
     */
    public void checkAccount(){
        account = et_account.getText().toString().trim();
        if(account.equals("")){
            //空字符时不检测
            account_tag = false;
            return;
        }
        if(account_tag=AppUtil.checkAccount(account)){
            LogUtil.d(CLASS_NAME+"联网检测账号");
            doAsyncTask(C.task.checkAccount,C.api.checkAccount,0);
        }else {
            showWarn(iv_account_ok,"该账号不符合要求，请重新输入");
        }
    }

    /**
     * 检测昵称
     */
    private void checkNickName(){
        nickName = et_nickname.getText().toString().trim();
        if(nickName.equals("")){
            nickName_tag = false;
            return;
        }
        if(nickName_tag = AppUtil.checkNickName(nickName)){
            LogUtil.d(CLASS_NAME+"联网检测昵称");
            doAsyncTask(C.task.checkNickName,C.api.checkNickName,0);
        }else{
            showWarn(iv_nickname_ok,"该昵称不符合要求，请重新输入");
        }
    }

    /**
     * 检测密码
     */
    private void checkPassword(){
        password = et_password.getText().toString().trim();
        if(password.equals("")){
            password_tag = false;
            return;
        }
        if(password_tag = AppUtil.checkPassword(password)){
            showOk(iv_password_ok);
        }else{
            showWarn(iv_password_ok,"您输入的密码不符合要求，请重新输入");
        }
    }

    /**
     * 检测确认密码
     */
    private void checkConfirmPass(){
        confirm_password = et_conferm_password.getText().toString().trim();
        if(confirm_password.equals("")){
            //内容为空时，不显示有效或错误图标，不检测
            confirm_password_tag = false;
            iv_confirm_password_ok.setVisibility(View.GONE);
            return;
        }
        if(!password_tag){
            //密码无效时，不检测
            showWarn(iv_password_ok,"您输入的密码不符合要求，请重新输入");
            return;
        }
        if(password.equals(confirm_password)){
            confirm_password_tag = true;
            showOk(iv_confirm_password_ok);
            LogUtil.d(CLASS_NAME+"两次输入的密码一致，密码有效");
        }else {
            iv_confirm_password_ok.setImageResource(R.drawable.error);
            iv_confirm_password_ok.setVisibility(View.VISIBLE);
            confirm_password_tag = false;
            LogUtil.d(CLASS_NAME+"两次输入的密码不一致，密码无效");
        }
    }

    /**
     * 重新检测所有需要提交的数据
     * @return true为数据有效，false为无效
     */
    private boolean checkInfoAgain() {
        //重新检测账号的有效性
        account = et_account.getText().toString().trim();
        if(account.equals("")){
            showWarn(iv_account_ok,"账号不能为空，请重新输入");
            return false;
        }
        account_tag = AppUtil.checkAccount(account);
        if (!account_tag) {
            showWarn(iv_account_ok,"该账号不符合要求，请重新输入");
            return false;
        }
        //重新检测昵称的有效性
        nickName = et_nickname.getText().toString().trim();
        if(nickName.equals("")){
            showWarn(iv_account_ok,"昵称不能为空，请重新输入");
            return false;
        }
        nickName_tag = AppUtil.checkNickName(nickName);
        if(!nickName_tag){
            showWarn(iv_nickname_ok,"该昵称不符合要求，请重新输入");
            return false;
        }
        //重新检测密码的有效性
        password = et_password.getText().toString().trim();
        if(password.equals("")){
            showWarn(iv_password_ok,"密码不能为空，请重新输入");
            return false;
        }
        checkPassword();
        if(!password_tag){
            return false;
        }else{
            confirm_password = et_conferm_password.getText().toString().trim();
            if(confirm_password.equals("")){
                showWarn(iv_account_ok,"确认密码不能为空不能为空，请重新输入");
                return false;
            }
            checkConfirmPass();
            if(!confirm_password_tag){
                showWarn(iv_confirm_password_ok,"两次输入的密码不一致，请重新输入");
                return false;
            }
        }
        return true;
    }

    /**
     * 注册
     */
    private void sign() {
        if (NetworkUtil.getNetworkState(this) == NetworkUtil.TYPE.NONE) {
            toast("无网络，请稍后再试");
            return;
        }
        if (!checkInfoAgain()){
            //初步检测账号，密码，昵称不完全符合基本要求，都不好往下执行
            return;
        }
        //账号，昵称，密码都满足，联网注册
        HashMap<String, String> params = new HashMap<>();
        params.put("account_normal", account);
        params.put("nickName", nickName);
        params.put("password", password);
        if(TextUtils.isEmpty(imagePath)) {
            //如果没有选择本地图片作为头像，不上传照片，使用默认图片
            doAsyncTask(C.task.sign, C.api.sign, params, 0);
        }else{
            //上传用户信息
            ArrayList<NameValuePair> files = new ArrayList<>();
            files.add(new BasicNameValuePair("icon", imagePath));
            doAsyncTask(C.task.sign, C.api.sign, params,files, 0);
            LogUtil.d(CLASS_NAME + "联网注册（上传头像）");
        }
        rl_progressbar_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCompleteTask(int taskId, BaseMessage message) {
        LogUtil.d(CLASS_NAME+"请求任务已完成，回调函数");
        rl_progressbar_layout.setVisibility(View.GONE);
        UniversalModel model = null;
        try {
            model = (UniversalModel) message.getResult(BaseModel.UNIVERSAL_MODEL);
        } catch (Exception e) {
            toast("服务器异常，请稍后再试");
            LogUtil.d(CLASS_NAME+e.getMessage());
        }
        switch (taskId) {
            case C.task.checkAccount:
                //联网检测账号处理结果
                if (model.getStatus().equals(UniversalModel.SUCCEED)) {
                    showOk(iv_account_ok);
                    account_tag = true;
                    LogUtil.d(CLASS_NAME + "账号联网查询结果：账号有效");
                } else {
                    account_tag = false;
                    showWarn(iv_account_ok, "该账号已被占用，请重新输入");
                    LogUtil.d(CLASS_NAME + "账号联网查询结果：账号已被占用");
                }
                break;
            case C.task.checkNickName:
                //联网检测昵称处理结果
                if (model.getStatus().equals(UniversalModel.SUCCEED)) {
                    showOk(iv_nickname_ok);
                    nickName_tag = true;
                    LogUtil.d(CLASS_NAME + "昵称联网查询结果：昵称有效");
                } else {
                    nickName_tag = false;
                    showWarn(iv_nickname_ok, "该昵称已被占用，请重新输入");
                    LogUtil.d(CLASS_NAME + "昵称联网查询结果：昵称已被占用");
                }
                break;
            case C.task.sign:
                //联网注册处理结果
                if (model.getStatus().equals(UniversalModel.SUCCEED)) {
                    LogUtil.d(CLASS_NAME + "注册成功");
                   toast("注册成功");
                    LogUtil.d(CLASS_NAME + "跳转到登录界面");
                    forward(UiLogin.class);
                } else {
                    LogUtil.d(CLASS_NAME + "注册失败");
                    showWarn(iv_account_ok, message.getMessage());
                }
        }
    }

    @Override
    public void onNetworkError(int taskId, String errorInfo) {
        //网络连接有误
        LogUtil.d(CLASS_NAME+errorInfo);
        rl_progressbar_layout.setVisibility(View.GONE);
        if(taskId == C.task.sign){
            toast(errorInfo);
        }
    }

    /**
     * 显示警告信息
     * @param imageView  产生警告信息的输入框所对应的显示图标
     * @param warn   //警告内容
     */
    private void showWarn(ImageView imageView,String warn){
        tv_error_tips.setText(warn);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.error);
        ll_error_layout.setVisibility(View.VISIBLE);
    }

    //显示有效图标
    private void showOk(ImageView imageView){
        imageView.setImageResource(R.drawable.ok);
        imageView.setVisibility(View.VISIBLE);
    }

    //隐藏警告信息
    private void closeWarn(){
        ll_error_layout.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CHOOSE_ICON){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                LogUtil.d("用户同意应用使用内存读写权限");
                openAlbum();
            }else{
                LogUtil.d("用户拒绝应用使用内存读写权限，无法获取头像资源");
                Toast.makeText(UiSign.this,"你拒绝了应用使用内存读写权限，无法添加用户头像",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == CHOOSE_ICON){
                LogUtil.d(CLASS_NAME+"解析选择结果");
                imagePath = SDUtil.chooseImage(UiSign.this,data);
                if(!TextUtils.isEmpty(imagePath)){
                    iv_user_icon.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                    LogUtil.d(CLASS_NAME+"头像添加成功");
                }else{
                    toast("添加头像失败");
                    LogUtil.d(CLASS_NAME+"头像添加失败");
                }
            }
        }
    }
}
