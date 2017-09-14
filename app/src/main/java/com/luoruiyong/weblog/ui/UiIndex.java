package com.luoruiyong.weblog.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.base.BaseUi;

/**应用首页
 * Created by Administrator on 2017/9/9.
 */

public class UiIndex extends BaseUi {
    private static final String CLASS_NAME = UiIndex.class.getSimpleName() + "-->";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
