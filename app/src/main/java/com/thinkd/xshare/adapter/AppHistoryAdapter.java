package com.thinkd.xshare.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.ApkUtils;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.thinkd.xshare.base.BaseActivity.getContext;

/**
 * Created by 百思移动 on 2017/10/28.
 */

public class AppHistoryAdapter extends RecyclerView.Adapter<AppHistoryAdapter.ViewHolder> {
    public List<FileInfo> mlist;
    private int mType = FileInfo.TYPE_APK;
    private Context mContext;
    private String MY_BORDCAST = "MY_BORDCAST";

    private DisplayMp4Adapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(DisplayMp4Adapter.OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivShortcut;
        ImageView ivDel;
        TextView mTvName;
        TextView mTvSize;
        Button mBtClick;
        ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            ivShortcut = (ImageView) view.findViewById(R.id.iv_shortcut);
            ivDel = (ImageView) view.findViewById(R.id.iv_del);
            mTvName = (TextView) view.findViewById(R.id.tv_name);
            mTvSize = (TextView) view.findViewById(R.id.tv_size);
            mBtClick = (Button) view.findViewById(R.id.bt_click);
            progressBar = (ProgressBar) view.findViewById(R.id.pb_file);
        }
    }

    public AppHistoryAdapter(List<FileInfo> list, int type, Context context) {
        this.mlist = list;
        this.mType = type;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_apphistory, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FileInfo fileInfo = mlist.get(position);
        if (mType == FileInfo.TYPE_APK) {
            if (mlist != null && mlist.get(position) != null) {
                byte[] bytes = FileUtils.bitmapToByteArray(FileUtils
                        .drawableToBitmap(FileUtils.getApkThumbnail(mContext, fileInfo.getFilePath())));
                Glide
                        .with(mContext)
                        .load(bytes)
                        .placeholder(R.mipmap.icon_apk)
                        .error(R.mipmap.icon_apk)
                        .into(holder.ivShortcut);

                //加载数据
//                holder.mTvName.setText(getFileName(fileInfo.getFilePath()));
                holder.mTvName.setText(fileInfo.getName().substring(0, fileInfo.getName().lastIndexOf(".")));
                holder.mTvSize.setText(FileUtils.showLongFileSzie(fileInfo.getSize()));

                if (!ApkUtils.isInstalled(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()))) {
                    //未装过改应用 需要安装
                    holder.mBtClick.setText(mContext.getResources().getString(R.string.str_install));
                    holder.mBtClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ApkUtils.install(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("App页面", "安装", "点击");

                        }
                    });
                } else {
                    //装过该应用，直接打开
                    holder.mBtClick.setText(mContext.getResources().getString(R.string.str_run));
                    holder.mBtClick.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PackageManager pm = mContext.getPackageManager();
                            PackageInfo info = pm.getPackageArchiveInfo(fileInfo.getFilePath(), PackageManager.GET_ACTIVITIES);
                            ApplicationInfo appInfo = null;
                            if (info != null) {
                                appInfo = info.applicationInfo;
                                String packageName = appInfo.packageName;
                                Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(
                                        //这个是另外一个应用程序的包名
                                        packageName);
                                mContext.startActivity(intent);
                                Firebase.getInstance(getApplicationContext()).logEvent("App页面", "打开", "点击");
                            }
                        }
                    });
                }

            }
            //删除按钮
            holder.ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showExistDialog(fileInfo);
//                    showDialog(fileInfo);
                    Firebase.getInstance(getApplicationContext()).logEvent("App页面", "删除", "点击");
                }
            });

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

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    /**
     * 显示是否退出 对话框
     */
    private void showExistDialog(final FileInfo fileInfo){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setMessage(mContext.getResources().getString(R.string.delete_file_tip))
                .setPositiveButton(mContext.getResources().getString(R.string.prompt_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除文件
                        delFile(fileInfo);
                    }
                })
                .setNegativeButton(mContext.getResources().getString(R.string.prompt_no), null)
                .create()
                .show();
    }

    //系统样式的对话框
//    public void showDialog(final FileInfo fileInfo) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setTitle(mContext.getResources().getString(R.string.prompt_title))
//                .setMessage(mContext.getResources().getString(R.string.delete_file_tip))
//                .setNegativeButton(mContext.getResources().getString(R.string.prompt_no), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        return;
//                    }
//                }).setPositiveButton(mContext.getResources().getString(R.string.prompt_yes), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                //删除文件
//                delFile(fileInfo);
//                dialog.dismiss();
//            }
//        }).show();
//    }

    //删除选中的文件
    public void delFile(FileInfo fileInfo) {
        File file = new File(fileInfo.getFilePath());
        if (file == null || !file.exists() || file.isDirectory()) {
            Log.e("文件删除>>>>>>>>>>>", "文件删除失败");
        } else {
            mlist.remove(fileInfo);
            file.delete();
            Toast.makeText(mContext, mContext.getResources().getString(R.string.file_is_deleted), Toast.LENGTH_SHORT).show();

            //发送自定义广播，更新界面（如果某个Bean中的list中没有数据，就移除这个Bean）
            Intent intent = new Intent(MY_BORDCAST);
            mContext.sendBroadcast(intent);
            notifyDataSetChanged();
        }
    }

    public boolean isEmpty() {
        if (mlist.size() > 0) {
            return false;
        }
        return true;
    }

}
