package com.luoruiyong.weblog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.util.LogUtil;

/**身份验证控制器
 * Created by Administrator on 2017/9/9.
 */

public class UiAuthentication extends BaseUi {
    private static final String CLASS_NAME = UiAuthentication.class.getSimpleName() + "-->";
    private EditText et_cell_number;
    private EditText et_auth_code;
    private Button btn_get_auth_code;
    private Button btn_do_auth;
    private TextView tv_no_cell;
    private TextView tv_verification_mode;
    private MyOnClickListener listener;
    private boolean useCellPhone = true;  //验证方式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_find_password);
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
     * 绑定控件
     */
    private void bindControls() {
        LogUtil.d(CLASS_NAME+"绑定控件");
        et_cell_number = (EditText) findViewById(R.id.cell_number);
        et_auth_code = (EditText) findViewById(R.id.auth_code);
        btn_do_auth = (Button) findViewById(R.id.do_auth);
        btn_get_auth_code = (Button) findViewById(R.id.get_auth_code);
        tv_no_cell = (TextView) findViewById(R.id.label_no_cell);
        tv_verification_mode = (TextView) findViewById(R.id.label_verification_mode);
        listener = new MyOnClickListener();
        btn_do_auth.setOnClickListener(listener);
        btn_get_auth_code.setOnClickListener(listener);
        tv_no_cell.setOnClickListener(listener);
    }

    /**
     * 设置状态栏
     * 显示返回键
     */
    private void setToolbar() {
        LogUtil.d(CLASS_NAME+"初始化状态栏");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toorbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("身份验证");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.get_auth_code:
                    if(useCellPhone){
                        sendMessageToUser();
                    }else{
                        sendEmailToUser();
                    }
                    break;
                case R.id.do_auth:
                    doauth();
                    break;
                case R.id.label_no_cell:
                    changeToEmailVerificationMode();
                    break;
            }
        }
    }

    /**
     * 向用户发送验证信息
     */
    private void sendMessageToUser() {
        LogUtil.d(CLASS_NAME+"向用户发送验证信息的短信");
    }

    /**
     * 向用户发送包含验证信息的邮件
     */
    private void sendEmailToUser() {
        LogUtil.d(CLASS_NAME+"向用户发送包含验证信息的邮件");

    }

    /**
     * 身份验证
     */
    private void doauth() {
        LogUtil.d(CLASS_NAME+"身份验证");
    }

    /**
     * 使用邮箱验证方式
     */
    private void changeToEmailVerificationMode() {
        LogUtil.d(CLASS_NAME+"使用邮箱验证方式");
    }
}
