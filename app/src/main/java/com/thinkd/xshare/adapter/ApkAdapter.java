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

import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.ApkUtils;
import com.thinkd.xshare.util.FileUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by altman29 on 2017/10/25.
 * e-mial:s1yuan_chen@163.com
 */

public class ApkAdapter extends RecyclerView.Adapter<ApkAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FileInfo> mDatas;

    public ApkAdapter(Context context, List<FileInfo> list) {
        mContext = context;
        mDatas = list;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_receiver_apk, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final FileInfo fileInfo = mDatas.get(position);
        holder.mIvShortcut.setImageBitmap(fileInfo.getBitmap());
        holder.mTvName.setText(fileInfo.getName());
//        holder.mTvName.setText(FileUtils.getFileName(fileInfo.getFilePath()));
        holder.mTvSize.setText(FileUtils.showLongFileSzie(fileInfo.getSize()));

        if (fileInfo.getResult() == FileInfo.FLAG_SUCCESS) {
            long total = fileInfo.getSize();
            holder.progressBar.setVisibility(View.GONE);
            if (ApkUtils.isInstalled(mContext, fileInfo.getFilePath())) {
                holder.mBtClick.setVisibility(View.VISIBLE);
                holder.mBtClick.setText(mContext.getResources().getString(R.string.str_open));
            } else {
                holder.mBtClick.setVisibility(View.VISIBLE);
                holder.mBtClick.setText(mContext.getResources().getString(R.string.str_install));
            }
            holder.mBtClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String path = FileUtils.getLocalFilePath(fileInfo.getFilePath());
                    String apkPath = path.substring(0, path.lastIndexOf("/"));
                    apkPath = apkPath + "/" + fileInfo.getName() + ".apk";
                    Firebase.getInstance(getApplicationContext()).logEvent("发送页面", "app安装", "点击");
                    ApkUtils.install(mContext, apkPath);

                }
            });

        } else if (fileInfo.getResult() == FileInfo.FLAG_FAILURE) {
            holder.progressBar.setVisibility(View.GONE);
        } else {
            long progress = fileInfo.getProcceed();
            long total = fileInfo.getSize();
            int percent = (int) (progress * 100 / total);
            holder.progressBar.setMax(100);
            holder.progressBar.setProgress(percent);
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
        @Bind(R.id.pb_file) ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
