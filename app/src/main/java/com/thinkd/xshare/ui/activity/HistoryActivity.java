package com.thinkd.xshare.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.HistoryAdapter;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.EachFile;
import com.thinkd.xshare.entity.FileEntity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.widget.RoundProgressbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
<<<<<<< HEAD
 * @author 百思移动
 * 接收历史数据有待优化，
 * 发送历史的数据无法打开
 * 环形view的数据显示不正确
=======
 * 暂时只显示接收历史记录
 * 接收历史数据有待优化，显示有待优化。缺环形View实现。
>>>>>>> parent of b466ce9... history分支
 */
public class HistoryActivity extends AppCompatActivity {


    @Bind(R.id.tv_title) TextView mTvTitle;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.rv_historycontent) RecyclerView mRvHistorycontent;
    @Bind(R.id.fl_guanggao)
    FrameLayout flGuanggao;

    @Bind(R.id.roundprogressbar)RoundProgressbar mProgressBar;
    @Bind(R.id.ll_head)LinearLayout linearLayoutHeader;

    private List<FileInfo> mListFileInfo;
    private List<FileEntity> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        //native广告
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        AdAppHelper.getInstance(getApplicationContext()).getNative(0, flGuanggao, layoutParams);
        //Toolbar相关
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.history_title));
        mTvTitle.setText("");

        initData();
        convertData();
        initRv();
        mProgressBar.setProgress(75);

    }

    /**
     * 设置RecyclerView
     */
    private void initRv() {
        //new Adapter(mContext,mDatas);
        HistoryAdapter adapter = new HistoryAdapter(this, mDatas);
        mRvHistorycontent.setLayoutManager(new LinearLayoutManager(this));
        mRvHistorycontent.setAdapter(adapter);
    }

    /**
     * 转换数据
     */
    private void convertData() {
        //List<FileInfo> -> List<FileEntity>
        mDatas = new ArrayList<>();
        HashMap<String, FileEntity> map = new HashMap<>();
        for (int i = 0; i < mListFileInfo.size(); i++) {
            FileInfo fileInfo = mListFileInfo.get(i);

            FileEntity fileEntity = map.get(fileInfo.getMsgId());
            if (fileEntity == null) {
                fileEntity = new FileEntity();
                fileEntity.setMsgId(fileInfo.getMsgId());
                fileEntity.setDate(fileInfo.getDate());
                fileEntity.setHistoryDate(fileInfo.getHistoryDate());
                fileEntity.setImgList(new ArrayList<FileInfo>());
                fileEntity.setApkList(new ArrayList<FileInfo>());
                fileEntity.setVideoList(new ArrayList<FileInfo>());

//                fileEntity.setSendOrReceive(fileInfo.getSendOrReceive());

                map.put(fileInfo.getMsgId(), fileEntity);
            }
            switch (fileInfo.getFileType()) {
                case FileInfo.TYPE_JPG:
                    fileEntity.getImgList().add(fileInfo);
                    break;
                case FileInfo.TYPE_APK:
                    fileEntity.getApkList().add(fileInfo);
                    break;
                case FileInfo.TYPE_MP4:
                    fileEntity.getVideoList().add(fileInfo);
                    break;
            }
        }

        System.out.println("map size>>>>" + map.size());

        for (Map.Entry<String, FileEntity> entry : map.entrySet()) {
            FileEntity fileEntity = entry.getValue();
            mDatas.add(fileEntity);
        }
        if (mDatas != null && mDatas.size() > 0) {
            //sort
            Collections.sort(mDatas, new Comparator<FileEntity>() {
                @Override
                public int compare(FileEntity o1, FileEntity o2) {
                    //11-23-20
                    String[] sdate1 = o1.getDate().split("-");
                    String[] sdate2 = o2.getDate().split("-");
                    int sdate1hour = Integer.parseInt(sdate1[0]);
                    int sdate2hour = Integer.parseInt(sdate2[0]);
                    if (sdate1hour > sdate2hour) {
                        return -1;
                    } else if (sdate1hour < sdate2hour) {
                        return 1;
                    } else {
                        int sdate1minute = Integer.parseInt(sdate1[1]);
                        int sdate2minute = Integer.parseInt(sdate2[1]);
                        if (sdate1minute > sdate2minute) {
                            return -1;
                        } else if (sdate1minute < sdate2minute) {
                            return 1;
                        } else {
                            int sdate1second = Integer.parseInt(sdate1[2]);
                            int sdate2second = Integer.parseInt(sdate2[2]);
                            if (sdate1second > sdate2second) {
                                return -1;
                            } else if (sdate1second < sdate2second) {
                                return 1;
                            } else {
                                return 0;
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 从数据库中读取每一文件数据
     */
    private void initData() {
        //List<FileInfo>
        mListFileInfo = new ArrayList<>();
        List<EachFile> listFiles = DaoHelper.queryAll();
        if (listFiles.size() != 0) {
            for (EachFile eachFile : listFiles) {
                mListFileInfo.add(FileInfo.toObject(eachFile.getFileJson()));
            }
        } else {
            linearLayoutHeader.setVisibility(View.GONE);
            Toast.makeText(this, getResources().getString(R.string.history_toast

            ), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
