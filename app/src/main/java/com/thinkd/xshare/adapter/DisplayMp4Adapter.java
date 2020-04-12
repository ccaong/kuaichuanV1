package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.thinkd.xshare.R;

import com.thinkd.xshare.base.App;
import com.thinkd.xshare.entity.FileInfo;
import com.bumptech.glide.Glide;
import com.thinkd.xshare.util.FileUtils;

import java.util.List;


import static com.thinkd.xshare.base.App.getDelFileInfoMap;
import static com.thinkd.xshare.base.App.isDelExist;
import static com.thinkd.xshare.base.App.isExist;

/**
 * Created by 百思移动 on 2017/10/26.
 */

public class DisplayMp4Adapter extends RecyclerView.Adapter<DisplayMp4Adapter.ViewHolder> {

    public List<FileInfo> mlist;
    private int mType = FileInfo.TYPE_JPG;
    private Context mContext;


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
        ImageView iv_ok_tick;
        RelativeLayout relativeLayout;

        public ViewHolder(View view) {
            super(view);
            ivShortcut = (ImageView) view.findViewById(R.id.iv_shortcut);
            iv_ok_tick = (ImageView) view.findViewById(R.id.iv_ok_tick);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.jpg_selected);

        }
    }
    public DisplayMp4Adapter(List<FileInfo> list, int type,Context context) {
        this.mlist = list;
        this.mType = type;
        this.mContext = context;
    }


    @Override
    public DisplayMp4Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jpghistory, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DisplayMp4Adapter.ViewHolder holder, int position) {
        FileInfo fileInfo = mlist.get(position);
        if (mType == FileInfo.TYPE_MP4) {
            if (mlist != null && mlist.get(position) != null) {
                Glide
                        .with(mContext)
                        .load(fileInfo.getFilePath())
                        .centerCrop()
                        .placeholder(R.mipmap.novideo)
                        .crossFade()
                        .into(holder.ivShortcut);

                //全局变量是否存在FileInfo
                if(getDelFileInfoMap().size()>0){

                    if(isDelExist(fileInfo)){
                        holder.iv_ok_tick.setVisibility(View.VISIBLE);
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                    }else {
                        holder.iv_ok_tick.setVisibility(View.INVISIBLE);
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }else{
                    //全局变量是否存在FileInfo
                    if (isExist(fileInfo)) {
                        holder.iv_ok_tick.setVisibility(View.VISIBLE);
                        holder.relativeLayout.setVisibility(View.VISIBLE);
                    } else {
                        holder.iv_ok_tick.setVisibility(View.INVISIBLE);
                        holder.relativeLayout.setVisibility(View.INVISIBLE);
                    }
                }
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
        return mlist == null ? 0 : mlist.size();
    }

    public boolean deleteSelectedFile(FileInfo fileInfo) {
        for (int i = 0; i < mlist.size(); i++) {
            if (mlist.get(i).equals(fileInfo)) {
                mlist.remove(i);
                notifyDataSetChanged();
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty(){
        if(mlist.size()>0){
            return false;
        }
        return true;
    }
}
