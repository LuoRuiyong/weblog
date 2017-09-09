package com.luoruiyong.weblog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.util.LogUtil;

/**登录界面控制器
 * Created by Administrator on 2017/9/9.
 */

public class UiSign extends BaseUi implements View.OnClickListener {

    private static final String CLASS_NAME = UiSign.class.getSimpleName() + "-->";
    private ImageView iv_user_icon;
    private EditText et_nickname;
    private EditText et_cell_number;
    private EditText et_email;
    private EditText et_address;
    private EditText et_password;
    private EditText et_conferm_password;
    private Button btn_sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_sign);
        bindControls();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
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
        setToolbar();
        et_nickname = (EditText) findViewById(R.id.nickname);
        et_cell_number = (EditText) findViewById(R.id.cell_number);
        et_email = (EditText) findViewById(R.id.email);
        et_address = (EditText) findViewById(R.id.address);
        et_password = (EditText) findViewById(R.id.password);
        et_conferm_password = (EditText) findViewById(R.id.confirm_password);
        iv_user_icon = (ImageView) findViewById(R.id.user_icon);
        btn_sign = (Button) findViewById(R.id.sign);
        iv_user_icon.setOnClickListener(this);
        btn_sign.setOnClickListener(this);
    }

    /**
     * 设置状态栏的属性
     */
    private void setToolbar(){
        Toolbar toorbar = (Toolbar) findViewById(R.id.toorbar);
        toorbar.setTitle("注册");
        setSupportActionBar(toorbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);   //显示状态栏最左边的按钮，默认资源id为android.R.id.home
        actionBar.setHomeAsUpIndicator(R.drawable.back);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign:
                createNewCustomer();
                break;
            case R.id.user_icon:
                chooseLocalIcon();
                break;
        }
    }

    /**
     * 注册
     */
    private void createNewCustomer() {
        LogUtil.d(CLASS_NAME+"创建新用户");

    }

    /**
     * 从本地选择图片作为头像
     */
    private void chooseLocalIcon() {
        LogUtil.d(CLASS_NAME+"选择本地图片");
    }
}
