package com.luoruiyong.weblog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.util.LogUtil;

/**修改密码控制器
 * Created by Administrator on 2017/9/9.
 */

public class UiResetPassword extends BaseUi {
    private static final String CLASS_NAME = UiResetPassword.class.getSimpleName() + "-->";
    private EditText et_new_pass;
    private EditText et_confirm_pass;
    private Button btn_reset;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_reset_password);
        setToolbar();
        bindControls();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(CLASS_NAME+"销毁活动");
    }

    /**
     * 设置状态栏的属性
     */
    private void setToolbar(){
        LogUtil.d(CLASS_NAME+"初始化状态栏");
        Toolbar toorbar = (Toolbar) findViewById(R.id.toorbar);
        toorbar.setTitle("修改密码");
        setSupportActionBar(toorbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);   //显示状态栏最左边的按钮，默认资源id为android.R.id.home
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    /**
     * 绑定控件，添加监听事件
     */
    private void bindControls() {
        LogUtil.d(CLASS_NAME + "绑定控件");
        btn_reset = (Button) findViewById(R.id.reset);
        et_new_pass = (EditText) findViewById(R.id.new_password);
        et_confirm_pass = (EditText) findViewById(R.id.confirm_password);
        btn_reset.setOnClickListener(new MyOnClickListener());

    }

    //点击事件处理
    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.reset:
                    reset();
                    break;
            }
        }
    }

    //焦点事件处理
    private class MyOnFocusChangeListener implements View.OnFocusChangeListener{
        @Override
        public void onFocusChange(View view, boolean b) {
            switch (view.getId()){
                case R.id.new_password:
                    checkNewPassword();
                    break;
                case R.id.confirm_password:
                    checkPasswordSame();
                    break;
            }
        }
    }

    /**
     * 重置密码
     */
    private void reset() {
        LogUtil.d(CLASS_NAME+"联网重设密码");
    }

    /**
     * 判断所输入的密码是否符合标准
     */
    private void checkNewPassword() {
        LogUtil.d(CLASS_NAME+"检测新密码");
    }

    /**
     * 判断两次输入的密码是否一致
     */
    private void checkPasswordSame() {
        LogUtil.d(CLASS_NAME+"检测确认密码");
    }
}
