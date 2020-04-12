package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.FileUtils;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by altman29 on 2017/10/25.
 * e-mial:s1yuan_chen@163.com
 */

public class HistoryVideoAdapter extends RecyclerView.Adapter<HistoryVideoAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FileInfo> mDatas;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mOnItemClickListener = itemClickListener;
    }

    public HistoryVideoAdapter(Context context, List<FileInfo> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_receive_video, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        FileInfo fileInfo = mDatas.get(position);
//        Bitmap bitmap = FileUtils.getScreenshotBitmap(mContext, fileInfo.getFilePath(), FileUtils.TYPE_MP4);

//        holder.mIvShortcut.setImageBitmap(bitmap);
        Glide.with(mContext)
                .load(FileUtils.getLocalFilePath(fileInfo.getFilePath()))
                .placeholder(R.mipmap.novideo)
                .error(R.mipmap.novideo)
                .into(holder.mIvShortcut);

        holder.imageView.setVisibility(View.GONE);
        holder.progressBar.setVisibility(View.GONE);
        holder.mTvName.setText(FileUtils.getFileName(fileInfo.getFilePath()));
        holder.mTvSize.setText(FileUtils.getFileSize(fileInfo.getSize()));

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
        @Bind(R.id.tv_name) TextView mTvName;
        @Bind(R.id.tv_size) TextView mTvSize;
        @Bind(R.id.pb_file)ProgressBar progressBar;
        @Bind(R.id.iv_zhuangtai)ImageView imageView;
//        @Bind(R.id.iv_ok_tick) ImageView mIvOkTick;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }
}
