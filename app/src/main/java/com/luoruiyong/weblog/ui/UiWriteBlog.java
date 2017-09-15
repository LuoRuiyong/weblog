package com.luoruiyong.weblog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;

/**创建微博控制器
 * Created by Administrator on 2017/9/15.
 */

public class UiWriteBlog extends BaseUi {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_write_blog);
    }
}
