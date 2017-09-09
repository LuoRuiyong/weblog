package com.luoruiyong.weblog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.base.C;
import com.luoruiyong.weblog.util.LogUtil;

import java.util.HashMap;

/**登录界面控制器
 * Created by Administrator on 2017/9/7.
 */

public class UiLogin extends BaseUi implements CompoundButton.OnCheckedChangeListener{
    private static final String CLASS_NAME = UiLogin.class.getSimpleName() + "-->";
    private Button btn_login;
    private EditText et_account;
    private EditText et_password;
    private ImageView iv_user_icon;
    private CheckBox cb_remember_pass;
    private TextView tv_forget_pass;
    private MyOnClickListener listener;
    private MyTextWatcher textWatcher;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_login);
        bindControl();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign:
                Intent intent = new Intent(UiLogin.this,UiSign.class);
                this.startActivity(intent);
                break;
        }
        return true;
    }

    private void bindControl() {
        LogUtil.d(CLASS_NAME+"绑定控件");
        toolbar = (Toolbar) findViewById(R.id.toorbar);
        toolbar.setTitle("登录");
        setSupportActionBar(toolbar);
        btn_login = (Button) findViewById(R.id.login);
        et_account = (EditText) findViewById(R.id.account);
        et_password = (EditText) findViewById(R.id.password);
        cb_remember_pass = (CheckBox) findViewById(R.id.remember_pass);
        tv_forget_pass = (TextView) findViewById(R.id.forget_pass);
        iv_user_icon = (ImageView) findViewById(R.id.user_icon);
        //添加监听事件
        listener = new MyOnClickListener();
        btn_login.setOnClickListener(listener);
        tv_forget_pass.setOnClickListener(listener);
        textWatcher = new MyTextWatcher();
        et_account.addTextChangedListener(textWatcher);
        et_password.addTextChangedListener(textWatcher);
        cb_remember_pass.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        dealWithRecord(b);
    }

    /**
     * 输入框监视器
     */
    private class MyTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            getUserIcon();
        }
}

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.login:
                    login();
                    break;
                case R.id.forget_pass:
                    findPasswordByCellNumber();
                    break;
            }
        }
    }

    /**
     * 获取用户头像
     */
    private void getUserIcon() {

    }

    /**
     * 登录
     */
    private void login() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                AppClient client = new AppClient(UiLogin.this,"http://112.74.13.186:8001/index/login");
//                try {
//                    client.get();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
        HashMap<String,String> map = new HashMap<>();
        map.put("name","jame");
        map.put("pass","james");
        doAsyncTask(1, C.api.login,map,0);
    }


    /**
     * 通过手机验证找回密码
     * 默认方式
     */
    private void findPasswordByCellNumber() {
        Intent intent = new Intent(UiLogin.this,UiAuthentication.class);
        this.startActivity(intent);
    }


    /**
     * 用户信息处理
     * @param remember  是否记住密码
     */
    private void dealWithRecord(boolean remember){
        if(remember){
            //记住密码
        }else{
            //不记住密码
        }
    }

}
