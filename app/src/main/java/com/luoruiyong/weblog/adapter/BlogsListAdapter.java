package com.luoruiyong.weblog.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luoruiyong.weblog.R;
import com.luoruiyong.weblog.model.Blog;
import com.luoruiyong.weblog.model.Picture;
import com.luoruiyong.weblog.myview.BlogPictureViewGroup;
import com.luoruiyong.weblog.util.LogUtil;
import com.luoruiyong.weblog.util.SDUtil;

import java.util.ArrayList;

/**微博列表数据适配器
 * Created by Administrator on 2017/9/15.
 */

public class BlogsListAdapter extends RecyclerView.Adapter <BlogsListAdapter.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private OnItemClickListener itemClickListener;
    private OnBindViewHolderListener bindViewHolderListener;
    private ArrayList<Blog> blogsList;
    private static final String CLASS_NAME = BlogsListAdapter.class.getSimpleName()+"-->";

    public BlogsListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setBlogsList(ArrayList<Blog> blogsList) {
        this.blogsList = blogsList;
    }

    static class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView iv_user_icon;
        TextView tv_add_concern;
        TextView tv_share;
        TextView tv_comment;
        TextView tv_praise;
        TextView tv_nick_name;
        TextView tv_create_time;
        TextView tv_blog_content;
        RelativeLayout rl_connerned_layout;
        RelativeLayout rl_share_layout;
        RelativeLayout rl_comment_layout;
        RelativeLayout rl_praise_layout;
        LinearLayout ll_blog_layout;
        BlogPictureViewGroup blogPictureViewGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_user_icon = itemView.findViewById(R.id.user_icon);
            tv_comment = itemView.findViewById(R.id.comment);
            tv_praise = itemView.findViewById(R.id.praise);
            tv_share = itemView.findViewById(R.id.share);
            tv_add_concern = itemView.findViewById(R.id.add_concern);
            tv_nick_name =itemView.findViewById(R.id.nickname);
            tv_create_time = itemView.findViewById(R.id.create_time);
            tv_blog_content = itemView.findViewById(R.id.blog_content);
            rl_connerned_layout = itemView.findViewById(R.id.concerned);
            rl_share_layout = itemView.findViewById(R.id.share_layout);
            rl_comment_layout = itemView.findViewById(R.id.comment_layout);
            rl_praise_layout = itemView.findViewById(R.id.praise_layout);
            ll_blog_layout =  itemView.findViewById(R.id.blog_layout);
            blogPictureViewGroup = itemView.findViewById(R.id.blog_picture);
        }
    }

    @Override
    public BlogsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blogs_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        holder.iv_user_icon.setOnClickListener(this);
        holder.tv_nick_name.setOnClickListener(this);
        holder.tv_add_concern.setOnClickListener(this);
        holder.rl_share_layout.setOnClickListener(this);
        holder.rl_comment_layout.setOnClickListener(this);
        holder.rl_praise_layout.setOnClickListener(this);
        holder.ll_blog_layout.setOnClickListener(this);
        LogUtil.d(CLASS_NAME+"创建ViewHolder");
        return holder;
    }

    @Override
    public void onBindViewHolder(BlogsListAdapter.ViewHolder holder, int position) {
        LogUtil.d(CLASS_NAME+"绑定ViewHolder，位置："+position);
        Blog blog = blogsList.get(position);
        String iconUrl = blog.getEditorIconUrl();
        ArrayList<String> pictureUrl = blog.getPictureUrl();
        //第一次绑定
        if (holder.iv_user_icon.getTag()==null){
            //逐步回调UiIndex中的方法，联网加载头像和微博图片
            if(bindViewHolderListener!=null) {
                bindViewHolderListener.bindViewHolder(position);
            }
            //设置标记，点击事件需要使用到
            holder.iv_user_icon.setTag(position);
            holder.tv_nick_name.setTag(position);
            holder.tv_add_concern.setTag(position);
            holder.rl_share_layout.setTag(position);
            holder.rl_comment_layout.setTag(position);
            holder.rl_praise_layout.setTag(position);
            holder.ll_blog_layout.setTag(position);
            addImageView(holder,pictureUrl,position);
        }
        //设置数据
        holder.tv_nick_name.setText(blog.getEditorNickname());
        holder.tv_blog_content.setText(blog.getContent());
        holder.tv_create_time.setText(blog.getCreateTime());
        holder.tv_comment.setText(blog.getCommentCount());
        holder.tv_share.setText(blog.getShareCount());
        holder.tv_praise.setText(blog.getPraiseCount());
        if(blog.getFanRelationship() != null && blog.getFanRelationship().equals("true")){
            //已关注
            holder.rl_connerned_layout.setVisibility(View.VISIBLE);
            holder.tv_add_concern.setEnabled(false);
        }
        loadPicture(holder,pictureUrl,iconUrl);
    }

    private void loadPicture(ViewHolder holder, ArrayList<String> pictureUrl,String iconUrl) {
        int childCount = holder.blogPictureViewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView image = (ImageView) holder.blogPictureViewGroup.getChildAt(i);
            Bitmap bitmap = SDUtil.getImage(pictureUrl.get(i), Picture.TYPE_IMAGE);
            if(bitmap != null){
                image.setImageBitmap(bitmap);
            }
        }
        Bitmap bitmap = SDUtil.getImage(iconUrl,Picture.TYPE_ICON);
        if(bitmap != null){
            holder.iv_user_icon.setImageBitmap(bitmap);
        }
    }

    private void addImageView(BlogsListAdapter.ViewHolder holder, ArrayList<String> pictureList, int position) {
        if(pictureList != null && pictureList.size() > 0){
            int pictureCount = pictureList.size();
            for (int i=0;i<pictureCount;i++){
                ImageView image = new ImageView(mContext);
                image.setOnClickListener(this);
                image.setId(i);
                image.setTag(position);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                image.setLayoutParams(params);
                image.setImageResource(R.drawable.blog_picture_default);
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.blogPictureViewGroup.addView(image);
            }
            holder.blogPictureViewGroup.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(blogsList == null){
            return 0;
        }
        return blogsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(blogsList.get(position).getPictureUrl() == null){
            return 0;
        }
        return blogsList.get(position).getPictureUrl().size();
    }

    public void setOnItemClickListener(OnItemClickListener lister){
        this.itemClickListener = lister;
    }

    public void setOnBindViewHolderListener(OnBindViewHolderListener lister){
        this.bindViewHolderListener = lister;
    }

    @Override
    public void onClick(View view) {
        if(itemClickListener != null){
            itemClickListener.onItemClick(view,(int)view.getTag());
        }
    }

    public  interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnBindViewHolderListener {
        void bindViewHolder(int position);
    }
}
