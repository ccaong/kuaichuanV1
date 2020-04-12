package com.thinkd.xshare.ui.activity.history;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.AppHistoryAdapter;
import com.thinkd.xshare.adapter.bean.FileInfoBean;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.thinkd.xshare.util.FileUtils.conversionTime;
import static com.thinkd.xshare.util.FileUtils.groupByTimeToList;

public class AppHistoryActivity extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fl_guanggao)
    FrameLayout flGuanggao;

    //apk文件夹路径
    private String mPathname = FileUtils.getRootDirPath() + "apk";
    private List<FileInfoBean> mFileInfoBeans = new ArrayList<>();
    private List<FileInfo> mFileInfos = new ArrayList<>();
    private List<File> mFiles = new ArrayList<>();
    private CurrentAdapter mAdapter;

    private IntentFilter intentFilter;
    private FileUpdateReceiver fileUpdateReceiver;
    private String MY_BORDCAST = "MY_BORDCAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
        ButterKnife.bind(this);
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "App页面");

        //native广告
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        AdAppHelper.getInstance(getApplicationContext()).getNative(0, flGuanggao, layoutParams);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("");
        getSupportActionBar().setTitle(getResources().getString(R.string.app_title));

        initData();
        mFileInfoBeans = groupByTimeToList(mFileInfos);

        //设置适配器
        mAdapter = new CurrentAdapter(this, mFiles, mFileInfoBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(true);

        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        intentFilter = new IntentFilter();
        intentFilter.addAction(MY_BORDCAST);
        fileUpdateReceiver = new FileUpdateReceiver();
        registerReceiver(fileUpdateReceiver, intentFilter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取文件
     */
    private void initData() {
        File[] files = new File(mPathname).listFiles();
        getByFileName(files);
    }

    private String getByFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.length() != 0) {
                    if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                        getByFileName(file.listFiles());
                    } else {
                        String fileName = file.getName();
                        if (FileUtils.isApkFile(fileName)) {
                            File eachApk = new File(mPathname + "/" + fileName);

                            FileInfo fileInfo = new FileInfo();
                            String strDate = null;
                            long size = 0;

                            //获取时间
                            Date date = new Date(eachApk.lastModified());
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            strDate = sdf.format(date);
                            //获取apk的大小
                            size = eachApk.length();

                            fileInfo.setName(fileName);
                            fileInfo.setFilePath(eachApk.getPath());
                            fileInfo.setDate(strDate);
                            fileInfo.setSize(size);
                            mFiles.add(eachApk);
                            mFileInfos.add(fileInfo);
                            str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                        }
                    }
                }

            }
        }
        return str;
    }

    //设置item点击监听事件
    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    class FileUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mAdapter.deleteFile();
        }
    }

    class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

        private List<File> mDatas;
        public List<FileInfoBean> mListFileInfoBean;
        public ArrayList<AppHistoryAdapter> mAdapterList;
        private LayoutInflater mInflater;
        private Context mContext;
        AppHistoryAdapter mAppHistoryAdapter;

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            this.mOnItemClickListener = itemClickListener;
        }

        public CurrentAdapter(Context context, List<File> datas, List<FileInfoBean> fileInfoBeans) {
            mInflater = LayoutInflater.from(context);
            mContext = context;
            mDatas = datas;
            mListFileInfoBean = fileInfoBeans;
            mAdapterList = new ArrayList<>();
        }


        public void deleteFile() {
            for (int i = 0; i < mAdapterList.size(); i++) {
                if (mAdapterList.get(i).isEmpty()) {
                    if (i < mListFileInfoBean.size()) {
                        mListFileInfoBean.remove(i);
                    }
                    notifyDataSetChanged();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public CurrentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CurrentViewHolder holder = new CurrentViewHolder(mInflater.inflate(R.layout.item_show_file, null, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final CurrentViewHolder holder, int position) {
            final FileInfoBean fileInfoBean = mListFileInfoBean.get(position);
            //转换日期格式
            String strDate = conversionTime(mContext, fileInfoBean.getFileDate());
            holder.tvTime.setText(strDate);

            mAppHistoryAdapter = new AppHistoryAdapter(fileInfoBean.getFileInfoList(), FileInfo.TYPE_APK, mContext);
            mAdapterList.add(mAppHistoryAdapter);

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setAutoMeasureEnabled(true);

            holder.recyclerView.setLayoutManager(linearLayoutManager);
            holder.recyclerView.setAdapter(mAppHistoryAdapter);
        }

        @Override
        public int getItemCount() {
            return mListFileInfoBean.size();
        }

        class CurrentViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv_time)
            TextView tvTime;
            @Bind(R.id.rv_file)
            RecyclerView recyclerView;

            public CurrentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    //返回页面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(fileUpdateReceiver);
    }
}
