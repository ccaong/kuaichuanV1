package com.thinkd.xshare.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;

import java.util.List;

import static com.thinkd.xshare.base.App.addFileInfoToDel;
import static com.thinkd.xshare.base.App.isExist;

/**
 * Created by 百思移动 on 2017/10/25.
 * @author CCAONG
 */

public class FileInfoRvAdapter extends RecyclerView.Adapter<FileInfoRvAdapter.ApkViewHolder> {
    public List<FileInfo> mList;
    private int mType = FileInfo.TYPE_APK;
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ApkViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        ImageView ivShortcut;
        ImageView ivOkTick;
        TextView tvName;
        TextView tvSize;

        public ApkViewHolder(View view) {
            super(view);
            ivShortcut = (ImageView) view.findViewById(R.id.iv_shortcut);
            ivOkTick = (ImageView) view.findViewById(R.id.iv_ok_tick);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            tvSize = (TextView) view.findViewById(R.id.tv_size);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.apk_selected);
        }

    }

    public FileInfoRvAdapter(List<FileInfo> list, int type) {
        this.mList = list;
        this.mType = type;
    }

    @Override
    public FileInfoRvAdapter.ApkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apk, parent, false);
        ApkViewHolder holder = new ApkViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final FileInfoRvAdapter.ApkViewHolder holder, int position) {
        FileInfo fileInfo = mList.get(position);

        if (mType == FileInfo.TYPE_APK) {

            if (mList != null && mList.get(position) != null) {

                holder.ivShortcut.setImageBitmap(fileInfo.getBitmap());
                holder.tvName.setText(fileInfo.getName() == null ? "" : fileInfo.getName());
                holder.tvSize.setText(fileInfo.getSizeDesc() == null ? "" : fileInfo.getSizeDesc());

                //全局变量是否存在FileInfo
                if (isExist(fileInfo)) {
                    holder.ivOkTick.setVisibility(View.VISIBLE);
                    holder.relativeLayout.setVisibility(View.VISIBLE);
//                    holder.relativeLayout.setBackgroundColor("#33000000");
//                    holder.relativeLayout.setBackgroundColor(33000000);
                } else {
                    holder.ivOkTick.setVisibility(View.INVISIBLE);
                    holder.relativeLayout.setVisibility(View.INVISIBLE);
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
            }
        }
    }
    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
