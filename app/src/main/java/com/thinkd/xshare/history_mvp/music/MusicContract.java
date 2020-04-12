package com.thinkd.xshare.history_mvp.music;

import com.thinkd.xshare.entity.MusicEntity;
import com.thinkd.xshare.history_mvp.BaseModel;
import com.thinkd.xshare.history_mvp.BasePresenter;
import com.thinkd.xshare.history_mvp.BaseView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public interface MusicContract {

    interface View extends BaseView<Presenter> {

        /**
         * 展示数据
         * 供setAdapter使用
         */
        void showResult(ArrayList<MusicEntity> musicEntities);

        /**
         * 设置每个item同一日期个数
         */
        void setDateCount(String itemDateTitle);

        /**
         * 打开音乐
         *
         * @param path
         */
        void openFile(String path);

        /**
         * 刷新
         */
        void notifyDataChanged();

        /**
         * 点击toolbar右侧编辑
         */
        void edit();

        /**
         * 点击toolbar左侧推出编辑
         */
        void exitEdit();

        /**
         * 点击toolbar二层 右侧全选
         */
        void selectAll();

        /**
         * 点击toolbar二层 右侧 反选
         */
        void deSelectAll();

        /**
         * 根据实时选择文件个数 更新title
         */
        void updateTitle();


        void showBottom();

        /**
         * 删除选中文件
         */
        void deleteFile();

        /**
         * 点击一个日期，全选当前日期下所有文件
         */
        void selectByDate();

        /**
         * 点击一个日期，若处于全选状态，则反选
         */
        void deSelectByDate();

        /**
         * 每次选中操作，更新所有显示效果(再全选状态下，单选后，另其他uncheck)
         */
        void updateStatusWhenCheckStuff();

    }

    interface Presenter extends BasePresenter {

        /**
         * 从文件流中读取的最初未经处理的数据源
         */
        void loadData(String path);


        /**
         * 根据日期，获取每个日期下对应个数
         *
         */
        void getCountMapByDate(ArrayList<MusicEntity> data,String key);

    }

    interface Model extends BaseModel {
        //获取数据源
        ArrayList<MusicEntity> loadData(String path);

        //获取datecount map
        Map<String, Integer> getCountMapByDate(ArrayList<MusicEntity> data);
    }
}
