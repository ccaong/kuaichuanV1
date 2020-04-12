package com.thinkd.xshare.adapter;

import android.content.Context;
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
import com.thinkd.xshare.util.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * 文件接收列表 Adapter
 * 未使用
 */
public class FileReceiverAdapter extends BaseAdapter {

    private Context mContext;
    private Map<String, FileInfo> mDataHashMap;
    private String[] mKeys;


    List<Map.Entry<String, FileInfo>> fileInfoMapList;


    public FileReceiverAdapter(Context mContext) {
        this.mContext = mContext;
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        //排序
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }

    /**
     * 更新数据
     */
    public void update() {
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fileInfoMapList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileInfoMapList.get(position).getValue();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FileInfo fileInfo = (FileInfo) getItem(position);

        FileSenderHolder viewHolder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_transfer, null);
            viewHolder = new FileSenderHolder();

            //缩略图
            viewHolder.iv_shortcut = (ImageView) convertView.findViewById(R.id.iv_shortcut);

            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            //传输过程文字
            viewHolder.tv_progress = (TextView) convertView.findViewById(R.id.tv_progress);
            //传输进度条
            viewHolder.pb_file = (ProgressBar) convertView.findViewById(R.id.pb_file);
            //取消按钮
            viewHolder.btn_operation = (Button) convertView.findViewById(R.id.btn_operation);
            //√
            viewHolder.iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FileSenderHolder) convertView.getTag();
        }

        if (fileInfo != null) {
            //初始化
            viewHolder.pb_file.setVisibility(View.VISIBLE);
            viewHolder.iv_tick.setVisibility(View.GONE);

            if (FileUtils.isJpgFile(fileInfo.getFilePath())) {//图片格式
//                Glide.with(mContext)
//                        .load(fileInfo.getFilePath())
//                        .centerCrop()
//                        .placeholder(R.mipmap.icon_jpg)
//                        .crossFade()
//                        .into(viewHolder.iv_shortcut);
                //文件接收时候的图片的缩略图是在FileInfo里面的
                if (fileInfo.getBitmap() != null) {
                    viewHolder.iv_shortcut.setImageBitmap(fileInfo.getBitmap());
                } else {
                    viewHolder.iv_shortcut.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.nophoto));
                }

                viewHolder.tv_name.setText(FileUtils.getFileName(fileInfo.getFilePath()));


                if (fileInfo.getResult() == FileInfo.FLAG_SUCCESS) { //文件传输成功
                    long total = fileInfo.getSize();
                    viewHolder.pb_file.setVisibility(View.GONE);
                    viewHolder.tv_progress.setText(FileUtils.getFileSize(total) + "/" + FileUtils.getFileSize(total));

                    viewHolder.btn_operation.setVisibility(View.VISIBLE);
                    viewHolder.iv_tick.setVisibility(View.INVISIBLE);

                    if (FileUtils.isJpgFile(fileInfo.getFilePath())) {
                        //图片格式
                        viewHolder.btn_operation.setText(mContext.getResources().getString(R.string.str_open));
                        viewHolder.btn_operation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            }
                        });

                    }
//                    else{//装过改应用
//                        viewHolder.btn_operation.setText(mContext.getResources().getString(R.string.str_run));
//                    }
//                    viewHolder.iv_tick.setVisibility(View.VISIBLE);
                } else if (FileUtils.isJpgFile(fileInfo.getFilePath()) ||//图片格式
                        FileUtils.isMp3File(fileInfo.getFilePath()) || //音乐格式
                        FileUtils.isMp4File(fileInfo.getFilePath())) {//视屏音乐格式
                    //视屏格式
                    viewHolder.btn_operation.setText(mContext.getResources().getString(R.string.str_open));

                }

            } else if (fileInfo.getResult() == FileInfo.FLAG_FAILURE) { //文件传输失败
                viewHolder.pb_file.setVisibility(View.GONE);
            } else {//文件传输中
                long progress = fileInfo.getProcceed();
                long total = fileInfo.getSize();
                viewHolder.tv_progress.setText(FileUtils.getFileSize(progress) + "/" + FileUtils.getFileSize(total));

                int percent = (int) (progress * 100 / total);
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

        } else {

            viewHolder.iv_shortcut.setVisibility(View.GONE);
            viewHolder.tv_name.setVisibility(View.GONE);
            viewHolder.tv_progress.setVisibility(View.GONE);
            viewHolder.pb_file.setVisibility(View.GONE);
            viewHolder.btn_operation.setVisibility(View.GONE);
            viewHolder.iv_tick.setVisibility(View.GONE);
        }


        return convertView;
}

static class FileSenderHolder {
    ImageView iv_shortcut;
    TextView tv_name;
    TextView tv_progress;
    ProgressBar pb_file;

    Button btn_operation;
    ImageView iv_tick;
}
}
