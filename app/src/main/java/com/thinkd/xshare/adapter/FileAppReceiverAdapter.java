package com.thinkd.xshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.ApkUtils;
import com.thinkd.xshare.util.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 文件接收列表 Adapter
 * Created by 百思移动 on 2017/10/16.
 * 未使用
 */
public class FileAppReceiverAdapter extends BaseAdapter {

    private Context mContext;
    private Map<String, FileInfo> mDataHashMap;
    private String[] mKeys;
    private List<FileInfo>mFileInfos;


    List<Map.Entry<String, FileInfo>> fileInfoMapList;


    public FileAppReceiverAdapter(Context mContext) {
        this.mContext = mContext;
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        //排序
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }

    public FileAppReceiverAdapter(Context mContext,List<FileInfo> mHmp) {
        this.mContext = mContext;
        mFileInfos = mHmp;
//        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
//        //排序
//        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }



    /**
     * 更新数据
     */
    public void update(){
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        notifyDataSetChanged();
    }


    //FIXME 返回的不应该是fileInfoMapList的size。应该是收到的App的个数
    @Override
    public int getCount() {
        return mFileInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = (FileInfo) getItem(position);

        FileSenderHolder viewHolder = null;
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.item_transfer, null);
            viewHolder = new FileSenderHolder();
            viewHolder.iv_shortcut = (ImageView) convertView.findViewById(R.id.iv_shortcut);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            viewHolder.pb_file = (ProgressBar) convertView.findViewById(R.id.pb_file);
            viewHolder.btn_operation = (Button) convertView.findViewById(R.id.btn_operation);
            viewHolder.iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);

            viewHolder.viewLine = convertView.findViewById(R.id.view_line);

            viewHolder.tv_item = (TextView) convertView.findViewById(R.id.tv_class_name);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (FileSenderHolder) convertView.getTag();
        }

        if(fileInfo != null){
            //初始化
            viewHolder.pb_file.setVisibility(View.VISIBLE);
            viewHolder.iv_tick.setVisibility(View.GONE);

            if(FileUtils.isApkFile(fileInfo.getFilePath())){ //Apk格式
                if(fileInfo.getBitmap() != null){
                    viewHolder.iv_shortcut.setImageBitmap(fileInfo.getBitmap());
                }else{
                    viewHolder.iv_shortcut.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon_apk));
                }


                viewHolder.tv_name.setText(FileUtils.getFileName(fileInfo.getFilePath()));

                if(position==0){
                    viewHolder.tv_item.setText("App(2/3)");
                    viewHolder.tv_item.setVisibility(View.VISIBLE);
                    viewHolder.viewLine.setVisibility(View.VISIBLE);
                }


                if(fileInfo.getResult() == FileInfo.FLAG_SUCCESS){ //文件传输成功
                    long total = fileInfo.getSize();
                    viewHolder.pb_file.setVisibility(View.GONE);
                    viewHolder.tv_progress.setText(FileUtils.getFileSize(total) + "/" + FileUtils.getFileSize(total));

                    viewHolder.btn_operation.setVisibility(View.VISIBLE);
                    viewHolder.iv_tick.setVisibility(View.INVISIBLE);

                    if(FileUtils.isApkFile(FileUtils.getLocalFilePath(fileInfo.getFilePath()))){ //Apk格式
                        if(!ApkUtils.isInstalled(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()))){ //未装过改应用 需要安装
                            viewHolder.btn_operation.setText(mContext.getResources().getString(R.string.str_install));
                            viewHolder.btn_operation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ApkUtils.install(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                                }
                            });

                        }else{//装过该应用
                            viewHolder.btn_operation.setText(mContext.getResources().getString(R.string.str_run));
                            viewHolder.btn_operation.setOnClickListener(new View.OnClickListener() {
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
                                    }
                                }
                            });
                        }
                        viewHolder.iv_tick.setVisibility(View.VISIBLE);
                    }

                }else if(fileInfo.getResult() == FileInfo.FLAG_FAILURE) { //文件传输失败
                    viewHolder.pb_file.setVisibility(View.GONE);
                }else{//文件传输中
                    long progress = fileInfo.getProcceed();
                    long total = fileInfo.getSize();
                    viewHolder.tv_progress.setText(FileUtils.getFileSize(progress) + "/" + FileUtils.getFileSize(total));

                    int percent = (int)(progress *  100 / total);
                    viewHolder.pb_file.setMax(100);
                    viewHolder.pb_file.setProgress(percent);

                    //TODO 传输过程中取消的问题
                    viewHolder.btn_operation.setText(mContext.getString(R.string.str_cancel));
                    viewHolder.btn_operation.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //可否通过广播来实现？
                         }
                    });
                }
            }
            else{

                viewHolder.iv_shortcut.setVisibility(View.GONE);
                viewHolder.tv_name.setVisibility(View.GONE);
                viewHolder.tv_progress.setVisibility(View.GONE);
                viewHolder.pb_file.setVisibility(View.GONE);
                viewHolder.btn_operation.setVisibility(View.GONE);
                viewHolder.iv_tick.setVisibility(View.GONE);

            }



        }

        return convertView;
    }

    static class FileSenderHolder {
        ImageView iv_shortcut;
        TextView tv_name;
        TextView tv_progress;
        ProgressBar pb_file;
        TextView tv_item;
        View viewLine;
        Button btn_operation;
        ImageView iv_tick;
    }
}
