package com.luoruiyong.weblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;

/**微博列表
 * Created by Administrator on 2017/9/15.
 */

public class FgBlogsList extends Fragment {

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_blogs_list,container,false);
        return  view;
    }
}
