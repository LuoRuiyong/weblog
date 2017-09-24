package com.luoruiyong.weblog.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;

/**用户主页
 * Created by Administrator on 2017/9/23.
 */

public class FgPersonBrief extends Fragment{
    private static final String CLASS_NAME = FgPersonBrief.class.getSimpleName()+"-->";
    private View view;
    private TextView tv_address;
    private TextView tv_introduction;
    private RelativeLayout rl_more_info_layout;
    private LinearLayout ll_introduction_layolut;
    private RecyclerView rv_main;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_person_brief,container, false);
        initControls();
        return view;
    }

    private void initControls() {
        tv_address = view.findViewById(R.id.address);
        tv_introduction = view.findViewById(R.id.introduction);
        rl_more_info_layout = view.findViewById(R.id.more_info_layout);
        ll_introduction_layolut = view.findViewById(R.id.introduction_layout);
        MyOnClickListener clickListener = new MyOnClickListener();
        rl_more_info_layout.setOnClickListener(clickListener);
        ll_introduction_layolut.setOnClickListener(clickListener);
    }

    private class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.introduction_layout:

                    break;
                case R.id.more_info_layout:

                    break;

            }
        }
    }

    public void setAddress(String address){
        tv_address.setText(address);
    }

    public void setIntroduction(String introduction){
        tv_introduction.setText(introduction);
    }
}
