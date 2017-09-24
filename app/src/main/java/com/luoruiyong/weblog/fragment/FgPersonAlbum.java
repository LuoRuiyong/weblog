package com.luoruiyong.weblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.R;

/**用户主页
 * Created by Administrator on 2017/9/23.
 */

public class FgPersonAlbum extends Fragment{
    private static final String CLASS_NAME = FgPersonAlbum.class.getSimpleName()+"-->";
    private View view;
    private RecyclerView rv_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_person_album,container, false);
        return view;
    }
}
