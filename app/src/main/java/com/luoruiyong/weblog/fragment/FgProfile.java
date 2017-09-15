package com.luoruiyong.weblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.util.LogUtil;

/**首页碎片
 * Created by Administrator on 2017/9/14.
 */

public class FgProfile extends Fragment {
    private final static String CLASS_NAME = FgProfile.class.getSimpleName()+"-->";
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_profile,container,false);
        LogUtil.d(CLASS_NAME+"创建视图");
        return view;
    }
}
