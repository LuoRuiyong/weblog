package com.luoruiyong.weblog.myview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.util.LogUtil;

/**下拉更新
 * Created by Administrator on 2017/9/23.
 */

public class PullRefreshViewGroup extends ViewGroup {

    private static final String CLASS_NAME = PullRefreshViewGroup.class.getSimpleName()+"-->";

    private int status = 0;  //状态
    private int STATUS_NORMAL = 0; //正常状态
    private int STATUS_READY_REFRESH = 1;  //准备刷新状态
    private int STATUS_READY_LOAD = 2;  //准备刷新状态
    private int STATUS_REFRESHING = 3;  //正在加载跟多状态
    private int STATUS_LOADING = 4;  //正在加载状态
    private int lastChild;
    private int lastMoveY = 0;
    private int lastInterceptY = 0;
    private int effictiveHeaderHeight;
    private int effictiveFooterHeight;

    private RelativeLayout rl_header;
    private RelativeLayout rl_footer;
    private ProgressBar pb_up_refresh;
    private ProgressBar pb_down_refresh;
    private TextView tv_up_tip;
    private TextView tv_down_tip;

    private OnRefreshDataListener listener;
    public PullRefreshViewGroup(Context context) {
        super(context);
    }

    public PullRefreshViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullRefreshViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //所有xml里面的孩子被实例化后调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lastChild = getChildCount() -1;  //记录最后一个孩子，也就是页脚加载更多布局
        addRefreshHeader();
        addRefreshFooter();

    }

    private void addRefreshHeader() {
        rl_header = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_header,null,false);
        pb_down_refresh = rl_header.findViewById(R.id.down_progressbar);
        tv_down_tip = rl_header.findViewById(R.id.down_tip);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        addView(rl_header,params);
    }

    private void addRefreshFooter() {
        rl_footer = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_footer,null,false);
        pb_up_refresh = rl_footer.findViewById(R.id.up_progressbar);
        tv_up_tip = rl_footer.findViewById(R.id.up_tip);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
        addView(rl_footer,params);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i <getChildCount(); i++) {
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        int contentHeight  = 0;
        for (int i = 0; i < getChildCount() ; i++) {
            View view = getChildAt(i);
            if(view == rl_header){
                //顶部刷新头布局
                view.layout(0,-view.getMeasuredHeight(),view.getMeasuredWidth(),0);
                effictiveHeaderHeight = view.getHeight();
            }else if(view == rl_footer){
                //底部加载更多布局
                view.layout(0,contentHeight,view.getMeasuredWidth(),getMeasuredHeight()+view.getMeasuredHeight());
                effictiveFooterHeight = view.getHeight();
            }else{
                view.layout(0,contentHeight,view.getMeasuredWidth(),view.getMeasuredHeight()+contentHeight);
                contentHeight += view.getMeasuredHeight();
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int y = (int) event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastMoveY = y;
                intercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                View view = getChildAt(lastChild);
                if(y > lastInterceptY){
                    //下滑
                    if(intercept = getRefreshIntercept(view)){
                        //拦截，进入准备刷新状态
                        LogUtil.d(CLASS_NAME+"拦截，进入准备刷新状态");
                        status = STATUS_READY_REFRESH;
                    }
                }else if(y < lastInterceptY){
                    //上滑
                    if(intercept = getLoadMoreIntercept(view)){
                        //拦截，进入准备加载状态
                        LogUtil.d(CLASS_NAME+"拦截，进入准备加载状态");
                        status = STATUS_READY_LOAD;
                    }
                }else{
                    if(status == STATUS_REFRESHING){
                        //正在更新,终止
                        refreshDataError();
                    }else if(status == STATUS_LOADING){
                        //正在加载，终止
                        loadDataError();
                    }
                    intercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastInterceptY = y;
        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getY();
        if(status == STATUS_REFRESHING || status == STATUS_LOADING){
            //正在更新或加载
            return true;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = lastMoveY - y;
                LogUtil.d(CLASS_NAME+distance+"   "+getScrollY());
                if(status == STATUS_READY_LOAD){
                    if(getScrollY() < effictiveFooterHeight){
                        pb_up_refresh.setVisibility(GONE);
                        tv_up_tip.setText("上拉加载更多");
                        scrollBy(0,distance/3);
                    }else{
                        pb_up_refresh.setVisibility(GONE);
                        tv_up_tip.setText("松开加载");
                    }
                }else if(status == STATUS_READY_REFRESH){
                    if(getScrollY() > -effictiveFooterHeight){
                        pb_down_refresh.setVisibility(GONE);
                        tv_down_tip.setText("下拉刷新");
                        scrollBy(0,distance/3);
                    }else {
                        pb_down_refresh.setVisibility(GONE);
                        tv_down_tip.setText("松开刷新");
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(status == STATUS_READY_REFRESH){
                    if(getScrollY() <= -effictiveHeaderHeight ){
                        status = STATUS_REFRESHING;
                        pb_down_refresh.setVisibility(View.VISIBLE);
                        tv_down_tip.setText("正在更新...");
                        if(listener!= null){
                            listener.onDownPullRefreshData();
                        }
                    }else{
                        status = STATUS_NORMAL;
                        scrollBy(0,-getScrollY());
                    }
                }else if(status == STATUS_READY_LOAD){
                    if(getScrollY() >= effictiveFooterHeight){
                        status = STATUS_REFRESHING;
                        pb_up_refresh.setVisibility(View.VISIBLE);
                        tv_up_tip.setText("正在加载...");
                        if(listener != null){
                            listener.onUpPullLoadData();
                        }
                    }else{
                        status = STATUS_NORMAL;
                        scrollBy(0,-getScrollY());
                    }
                }
                break;
        }
        lastMoveY = y;
        return super.onTouchEvent(event);
    }

    private boolean getRefreshIntercept(View view) {
        boolean intercept = false;
        if(view instanceof RecyclerView){
            RecyclerView  recyclerView = (RecyclerView)view;
            if(recyclerView.computeVerticalScrollOffset() <= 0){
                intercept = true;
            }
        }
        return intercept;
    }
    private boolean getLoadMoreIntercept(View view){
        boolean intercept = false;
        if(view instanceof RecyclerView){
            RecyclerView recyclerView = (RecyclerView) view;
            if(recyclerView.computeVerticalScrollExtent()+recyclerView.computeVerticalScrollOffset()
                    >= recyclerView.computeVerticalScrollRange()){
                intercept = true;
            }
        }
        return  intercept;
    }

    public void setOnRefreshDataListener(Context context){
        this.listener = (OnRefreshDataListener) context;
    }

    public void hideRefreshLayout(){
        scrollTo(0,0);
        status = STATUS_NORMAL;
    }

    public void hideLoadMoreLayout(){
        scrollTo(0,effictiveFooterHeight);
        status = STATUS_NORMAL;
    }

    public void refreshDataSucceed(){
        scrollTo(0,0);
        tv_down_tip.setText("下拉更新");
        pb_down_refresh.setVisibility(View.GONE);
        status = STATUS_NORMAL;
    }

    public void refreshDataError(){
        scrollTo(0,0);
        tv_down_tip.setText("下拉更新");
        pb_down_refresh.setVisibility(View.GONE);
        status = STATUS_NORMAL;
    }

    public void loadDataSucceed(){
        scrollTo(0,0);
        tv_up_tip.setText("上拉加载更多");
        pb_up_refresh.setVisibility(View.GONE);
        status = STATUS_NORMAL;
    }

    public void loadDataError(){
        scrollTo(0,0);
        tv_up_tip.setText("上拉加载更多");
        pb_up_refresh.setVisibility(View.GONE);
        status = STATUS_NORMAL;
    }

    public interface OnRefreshDataListener{
        void onDownPullRefreshData();
        void onUpPullLoadData();
    }

}
