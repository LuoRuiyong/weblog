package com.luoruiyong.weblog.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.util.LogUtil;

/**微博图片展示父容器
 * Created by Administrator on 2017/9/18.
 */

public class BlogPictureViewGroup extends ViewGroup {

    private int childCount;
    private int childWidth;
    private int childHeight;
    private int margin = 10;
    private final static String CLASS_NAME = BlogPictureViewGroup.class.getSimpleName()+"-->";

    public BlogPictureViewGroup(Context context) {
        super(context);
    }

    public BlogPictureViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BlogPictureViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        childCount = getChildCount();
        //不同个数的子控件，容器会有不同的高度
        //子控件个数为1-3，一层
        //子控件个数为4-6，两层
        //子控件个数为7-9，三层
        int width = getMeasuredWidth();
        childWidth = width / 3;
        childHeight = childWidth;
        int height = (childCount+2)/3 * childWidth;
        LogUtil.d(CLASS_NAME+"容器宽度："+width+"   容器高度："+height+"  子控件个数："+childCount);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if(changed){
            for (int i = 0;i < childCount; i++){
                View view = getChildAt(i);
                view.layout(i%3*childWidth+(i%3+2)/3*margin,i/3*childHeight+(i/3+2)/3*margin,
                        (i%3+1)*childWidth,(i/3+1)*childHeight);
            }
        }
    }
}
