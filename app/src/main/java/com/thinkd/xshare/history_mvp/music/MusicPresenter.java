package com.thinkd.xshare.history_mvp.music;

import android.content.Context;

import com.thinkd.xshare.entity.MusicEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public class MusicPresenter implements MusicContract.Presenter {

    private MusicContract.View mView;
    private MusicContract.Model mModel;
    private Context mContext;

    private ArrayList<MusicEntity> mData;//元数据

    public MusicPresenter(Context context, MusicContract.View view) {
        mView = view;
        mContext = context;
        this.mView.setPresenter(this);//绑定
        mData = new ArrayList<>();
    }

    @Override
    public void start() {
    }

    @Override
    public void loadData(String path) {
        mData = mModel.loadData(path);
        Collections.sort(mData, new MusicComparator());
        mView.showResult(mData);//view展示数据
    }

    @Override
    public void getCountMapByDate(ArrayList<MusicEntity> data, String key) {
        Map<String, Integer> dateMap = mModel.getCountMapByDate(data);
        mView.setDateCount(" (" + dateMap.get(key) + ")");//返回日期对应的个数
    }
}
