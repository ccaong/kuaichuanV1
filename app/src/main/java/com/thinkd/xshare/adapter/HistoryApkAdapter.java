package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.ApkUtils;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.LogUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by altman29 on 2017/10/25.
 * e-mial:s1yuan_chen@163.com
 */

public class HistoryApkAdapter extends RecyclerView.Adapter<HistoryApkAdapter.ViewHolder> {


    private Context mContext;
    private LayoutInflater mInflater;
    private List<FileInfo> mDatas;
    private OnItemClickListener mOnItemClickListener;

    public HistoryApkAdapter(Context context, List<FileInfo> list) {
        mContext = context;
        mDatas = list;
        mInflater = LayoutInflater.from(mContext);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_receiver_apk, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FileInfo fileInfo = mDatas.get(position);

        if (fileInfo != null) {
            String path = FileUtils.getLocalFilePath(fileInfo.getFilePath());
            String apkPath = path.substring(0, path.lastIndexOf("/"));
            apkPath = apkPath + "/" + fileInfo.getName() + ".apk";

            byte[] bytes = FileUtils.bitmapToByteArray(FileUtils
                    .drawableToBitmap(FileUtils.getApkThumbnail(mContext, apkPath)));
            LogUtils.e("name", fileInfo.getName());
            Glide
                    .with(mContext)
                    .load(bytes)
                    .placeholder(R.mipmap.icon_apk)
                    .error(R.mipmap.icon_apk)
                    .into(holder.mIvShortcut);

            holder.progressBar.setVisibility(View.GONE);
            holder.mTvName.setText(fileInfo.getName());
            holder.mTvSize.setText(FileUtils.showLongFileSzie(fileInfo.getSize()));
            if (ApkUtils.isInstalled(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()))) {
                holder.mBtClick.setVisibility(View.VISIBLE);
                holder.mBtClick.setText(mContext.getResources().getString(R.string.str_run));
            } else {
                holder.mBtClick.setVisibility(View.VISIBLE);
                holder.mBtClick.setText(mContext.getResources().getString(R.string.str_install));
            }
        }
        if (mOnItemClickListener != null) {
            holder.mBtClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, pos);
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
        @Bind(R.id.tv_name) TextView mTvName;
        @Bind(R.id.tv_size) TextView mTvSize;
        @Bind(R.id.bt_click) Button mBtClick;
        @Bind(R.id.pb_file)
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
}
