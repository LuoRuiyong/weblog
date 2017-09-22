package com.luoruiyong.weblog.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.weblog.base.BaseUi;
import com.luoruiyong.weblog.util.LogUtil;

/**微博图片浏览容器，实现平滑滑动图片
 * Created by Administrator on 2017/9/22.
 */

public class SlideShowViewGroup extends ViewGroup {
    private static final String CLASS_NAME = SlideShowViewGroup.class.getSimpleName()+"-->";
    private int childCount;
    private int childWidth = 0;
    private int childHeight = 0;
    private int index = 0;  //图片序号
    private int x;   //记录手指触摸到的控件的轴坐标
    private float startX;
    private float endX;
    private long startTime;
    private long endTime;
    private OnImageChangedListener imageChangedListener;
    private OnClickImageListener clickImageListener;

    public SlideShowViewGroup(Context context) {
        super(context);
    }

    public SlideShowViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideShowViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width;
        int height;
        childCount = getChildCount();
        if(childCount == 0){
            width = 0;
            height = 0;
        }else{
            width = BaseUi.WIDTH * childCount;
            height = BaseUi.HEIGHT;
            childWidth = BaseUi.WIDTH;
            childHeight  = BaseUi.HEIGHT;
        }
        LogUtil.d(CLASS_NAME+"浏览微博图片容器大小，宽："+width+" 高："+height);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            view.layout(i*childWidth,0,(i+1)*childWidth,childHeight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                startX = event.getX();
                x = (int)startX;
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distance = x - moveX;
                if(!(distance > 0 && index == childCount-1) && !(distance < 0 && index == 0)){
                    scrollBy(distance,0);
                }
                x = moveX;
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                endTime = System.currentTimeMillis();
                endX = event.getX();
                if(startX == endX){
                    //单击，交由onClickListener处理
                    clickImageListener.onClickImage();
                    return true;
                }else if(endTime - startTime < 200){
                    //速滑
                    if(startX > endX){
                        //左速滑
                        ++index;
                    }else{
                        //右速滑
                        --index;
                    }
                }else{
                    //缓慢滑动
                    index = (scrollX + childWidth/2)/childWidth;
                }
                if(index < 0){
                    index = 0;
                }else if(index > childCount -1){
                    index = childCount-1;
                }
                scrollTo(index*childWidth,0);
                LogUtil.d(CLASS_NAME+"scrollTo:("+index*childWidth+",0)");
                if(imageChangedListener != null){
                    imageChangedListener.onImageChanged(index,childCount);
                }
                break;
        }
        return true;
    }

    public void setSelectImage(int index){
        this.index = index;
        scrollTo(index*childWidth,0);
        LogUtil.d(CLASS_NAME+"第"+index+"张图片");
    }

    public void setOnImageChangedListener(OnImageChangedListener listener){
        this.imageChangedListener = listener;
    }

    public void setOnClickImageListener(OnClickImageListener listener){
        this.clickImageListener = listener;
    }

    public interface OnImageChangedListener{
        void onImageChanged(int index,int total);
    }

    public interface  OnClickImageListener{
        void onClickImage();
    }
}
