package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.FileSenderAdapter;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.common.FileSender;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendFileActivity extends BaseActivity {

    /**
     * Topbar相关UI
     */
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /**
     * 进度条 已传 耗时等UI组件
     */
    @Bind(R.id.progress_view)
    FrameLayout mProgressView;
    @Bind(R.id.pb_total)
    ProgressBar pb_total;
    @Bind(R.id.tv_value_storage)
    TextView tv_value_storage;
    @Bind(R.id.tv_unit_storage)
    TextView tv_unit_storage;
    @Bind(R.id.tv_value_time)
    TextView tv_value_time;
    @Bind(R.id.tv_unit_time)
    TextView tv_unit_time;

    /**
     * 其他UI
     */
    @Bind(R.id.lv_result)
    ListView lv_result;

    List<Map.Entry<String, FileInfo>> mFileInfoMapList;

    FileSenderAdapter mFileSenderAdapter;

    List<FileSender> mFileSenderList = new ArrayList<FileSender>();

    private String mMsgId;


    long mTotalLen = 0;     //所有总文件的进度
    long mCurOffset = 0;    //每次传送的偏移量
    long mLastUpdateLen = 0; //每个文件传送onProgress() 之前的进度
    String[] mStorageArray = null;

    long mTotalTime = 0;
    long mCurTimeOffset = 0;
    long mLastUpdateTime = 0;
    String[] mTimeArray = null;

    int mHasSendedFileCount = 0;


    public static final int MSG_UPDATE_FILE_INFO = 0X6666;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //TODO 未完成 handler实现细节以及封装
            if (msg.what == MSG_UPDATE_FILE_INFO) {
                updateTotalProgressView();
                if (mFileSenderAdapter != null) mFileSenderAdapter.notifyDataSetChanged();
            }

        }
    };

    /**
     * 更新进度 和 耗时的 View
     */
    private void updateTotalProgressView() {
        try {
            //设置传送的总容量大小
            mStorageArray = FileUtils.getFileSizeArrayStr(mTotalLen);
            tv_value_storage.setText(mStorageArray[0]);
            tv_unit_storage.setText(mStorageArray[1]);

            //设置传送的时间情况
            mTimeArray = FileUtils.getTimeByArrayStr(mTotalTime);
            tv_value_time.setText(mTimeArray[0]);
            tv_unit_time.setText(mTimeArray[1]);


            //设置传送的进度条情况
            if (mHasSendedFileCount == App.getAppContext().getFileInfoMap().size()) {
                pb_total.setProgress(0);
                tv_value_storage.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_value_time.setTextColor(getResources().getColor(R.color.color_yellow));
                return;
            }

            long total = App.getAppContext().getAllSendFileInfoSize();
            int percent = (int) (mTotalLen * 100 / total);
            pb_total.setProgress(percent);

            if (total == mTotalLen) {
                pb_total.setProgress(0);
                tv_value_storage.setTextColor(getResources().getColor(R.color.color_yellow));
                tv_value_time.setTextColor(getResources().getColor(R.color.color_yellow));
            }
        } catch (Exception e) {
            //convert storage array has some problem
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_sender);
        ButterKnife.bind(this);
        mProgressView.setVisibility(View.VISIBLE);
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "发送页面");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.send_file_title));
        tvTitle.setText("");

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("msgId") != null) {
            mMsgId = intent.getStringExtra("msgId");
        }
        String ssid = (isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE);
        init();
    }


    /**
     * 初始化
     */
    private void init() {
        pb_total.setMax(100);
        mFileSenderAdapter = new FileSenderAdapter(getContext());
        lv_result.setAdapter(mFileSenderAdapter);
        List<Map.Entry<String, FileInfo>> fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(App.getAppContext().getFileInfoMap().entrySet());
        mFileInfoMapList = fileInfoMapList;
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);

        //Android6.0 requires android.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        } else {
            initSendServer(fileInfoMapList);//开启传送文件
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initSendServer(mFileInfoMapList);//开启传送文件
            } else {
                // Permission Denied
                Toast.makeText(this, "权限被拒绝，无法发送文件", Toast.LENGTH_SHORT).show();
                finishNormal();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * 开始传送文件
     *
     * @param fileInfoMapList
     */
    private void initSendServer(List<Map.Entry<String, FileInfo>> fileInfoMapList) {

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        String ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        String uniqueId = ssid + DEVICE_ID.substring(10, 14);

        List<OutReach> reaches = App.getOutReaches();
        User userBySsidd = DaoHelper.getUserBySsidd(uniqueId);
        if (reaches != null && reaches.size() > 0) {
            for (OutReach reach : reaches) {
                if (!reach.getIpAddress().equals(userBySsidd.getIpAddress()) && !reach.getIpAddress().equals("default")) {
                    for (Map.Entry<String, FileInfo> entry : fileInfoMapList) {
                        String target = reach.getIpAddress();
                        if ("0.0.0.0".equals(reach.getIpAddress())) {
                            target = "192.168.43.1";
                        }
                        final FileInfo fileInfo = entry.getValue();
                        FileSender fileSender = new FileSender(getContext(), fileInfo, target, Constant.DEFAULT_SERVER_PORT);
                        fileSender.setOnSendListener(new FileSender.OnSendListener() {
                            @Override
                            public void onStart() {
                                mLastUpdateLen = 0;
                                mLastUpdateTime = System.currentTimeMillis();
                            }

                            @Override
                            public void onProgress(long progress, long total) {
                                //=====更新进度 流量 时间视图 start ====//
                                mCurOffset = progress - mLastUpdateLen > 0 ? progress - mLastUpdateLen : 0;
                                mTotalLen = mTotalLen + mCurOffset;
                                mLastUpdateLen = progress;

                                mCurTimeOffset = System.currentTimeMillis() - mLastUpdateTime > 0 ? System.currentTimeMillis() - mLastUpdateTime : 0;
                                mTotalTime = mTotalTime + mCurTimeOffset;
                                mLastUpdateTime = System.currentTimeMillis();
                                //=====更新进度 流量 时间视图 end ====//

                                //更新文件传送进度的ＵＩ
                                fileInfo.setProcceed(progress);
                                App.getAppContext().updateFileInfo(fileInfo);
                                mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                            }

                            @Override
                            public void onSuccess(FileInfo fileInfo) {
                                //=====更新进度 流量 时间视图 start ====//
                                mHasSendedFileCount++;

                                mTotalLen = mTotalLen + (fileInfo.getSize() - mLastUpdateLen);
                                mLastUpdateLen = 0;
                                mLastUpdateTime = System.currentTimeMillis();
                                //=====更新进度 流量 时间视图 end ====//

                                // 成功
                                fileInfo.setResult(FileInfo.FLAG_SUCCESS);
                                App.getAppContext().updateFileInfo(fileInfo);
                                mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                            }

                            @Override
                            public void onFailure(Throwable t, FileInfo fileInfo) {
                                mHasSendedFileCount++;//统计发送文件
                                //失败
                                fileInfo.setResult(FileInfo.FLAG_FAILURE);
                                App.getAppContext().updateFileInfo(fileInfo);
                                mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                            }
                        });

                        mFileSenderList.add(fileSender);
                        App.FILE_SENDER_EXECUTOR.execute(fileSender);
                    }
                }
            }
        }
    }


    public static boolean isNullOrBlank(String str) {
        return str == null || str.equals("");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //需要判断是否有文件在发送？
        if (hasFileSending()) {
            showExistDialog();
            return;
        }
        finishNormal();
    }

    /**
     * 正常退出
     */
    private void finishNormal() {
        stopAllFileSendingTask();
        App.getAppContext().getFileInfoMap().clear();
        this.finish();
    }

    /**
     * 停止所有的文件发送任务
     */
    private void stopAllFileSendingTask() {
        for (FileSender fileSender : mFileSenderList) {
            if (fileSender != null) {
                fileSender.stop();
            }
        }
    }

    /**
     * 判断是否有文件在传送
     */
    private boolean hasFileSending() {
        for (FileSender fileSender : mFileSenderList) {
            if (fileSender.isRunning()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 显示是否退出 对话框
     */
    private void showExistDialog() {
//        new AlertDialog.Builder(getContext())
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tip_now_has_task_is_running_exist_now))
                .setPositiveButton(getResources().getString(R.string.str_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishNormal();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.str_no), null)
                .create()
                .show();
    }
}
