package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;

import com.bumptech.glide.Glide;
import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.FileUtils;

import java.util.ArrayList;
import java.util.List;


import static com.thinkd.xshare.base.App.getDelFileInfoMap;
import static com.thinkd.xshare.base.App.isDelExist;
import static com.thinkd.xshare.base.App.isExist;

/**
 * Created by 百思移动 on 2017/10/26.
 *
 * @author CCAONG
 */

public class DisplayPhotoAdapter extends RecyclerView.Adapter<DisplayPhotoAdapter.ViewHolder> {

    public List<FileInfo> mList;
    private int mType = FileInfo.TYPE_JPG;
    private Context mContext;
    public static List<FileInfo> mDelFileInfos;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShortcut;
        ImageView ivOkTick;
        RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            ivShortcut = (ImageView) view.findViewById(R.id.iv_shortcut);
            ivOkTick = (ImageView) view.findViewById(R.id.iv_ok_tick);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.jpg_selected);
        }
    }

    public DisplayPhotoAdapter(List<FileInfo> list, int type, Context context) {
        this.mList = list;
        this.mType = type;
        this.mContext = context;
    }

    @Override
    public DisplayPhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jpghistory, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DisplayPhotoAdapter.ViewHolder holder, int position) {
        FileInfo fileInfo = mList.get(position);
        if (mType == FileInfo.TYPE_JPG) {
            if (mList != null && mList.get(position) != null) {
                Glide
                        .with(mContext)
                        .load(fileInfo.getFilePath())
                        .centerCrop()
                        .placeholder(R.mipmap.nophoto)
                        .crossFade()
                        .into(holder.ivShortcut);

                //待删除的集合中是否存在这个FileInfo
                if(getDelFileInfoMap().size()>0){
                    //主页下面显示照片的AC
                    if(isDelExist(fileInfo)){
                        holder.ivOkTick.setVisibility(View.VISIBLE);
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                    }else {
                        holder.ivOkTick.setVisibility(View.INVISIBLE);
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }else{
                    //选择文件时的AC
                    //全局变量是否存在FileInfo
                    if (isExist(fileInfo)) {
                        holder.ivOkTick.setVisibility(View.VISIBLE);
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                    } else {
                        holder.ivOkTick.setVisibility(View.INVISIBLE);
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }

                //点击事件
                if (mOnItemClickListener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(v, pos);
                        }
                    });
                }

                //长按事件
                if (mOnItemLongClickListener != null) {
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemLongClickListener.onItemLongClick(v, pos);
                            return true;
                        }
                    });
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public boolean deleteSelectedFile(FileInfo fileInfo) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).equals(fileInfo)) {
                mList.remove(i);
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        if(mList.size()>0){
            return false;
        }
        return true;
    }
}
