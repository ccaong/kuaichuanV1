package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.FileUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by altman29 on 2017/10/19.
 * e-mial:s1yuan_chen@163.com
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FileInfo> mDatas;

    private OnItemClickListener mOnItemClickListener;

    public ImageAdapter(Context context, List<FileInfo> list) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mDatas = list;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_share_jpg, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FileInfo fileInfo = mDatas.get(position);

//        Glide
//                .with(mContext)
//                .load(FileUtils.getLocalFilePath(fileInfo.getFilePath()))
//                .centerCrop()
//                .placeholder(R.mipmap.nophoto)
//                .crossFade()
//                .into(holder.mIvShortcut);

        holder.mIvShortcut.setImageBitmap(fileInfo.getBitmap());

        int result = fileInfo.getResult();
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_shortcut) ImageView mIvShortcut;
        @Bind(R.id.iv_ok_tick) ImageView mIvOkTick;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
}
