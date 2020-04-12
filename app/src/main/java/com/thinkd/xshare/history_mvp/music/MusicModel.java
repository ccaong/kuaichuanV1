package com.thinkd.xshare.history_mvp.music;

import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.entity.MusicEntity;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public class MusicModel implements MusicContract.Model {

    private ArrayList<MusicEntity> mDatas = new ArrayList<>();
    private Map<String, Integer> dateMap = new HashMap<>();

    @Override
    public Map<String, Integer> getCountMapByDate(ArrayList<MusicEntity> datas) {
        getDateMap(datas);
        return dateMap;
    }


    @Override
    public ArrayList<MusicEntity> loadData(String path) {
        File[] files = new File(path).listFiles();
        getByFileName(files);
        return mDatas;
    }


    private String getByFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    getByFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                    if (FileUtils.isMp3File(fileName)) {
                        File eachMp3 = new File(Constant.MUSIC_FILE_PATH + "/" + fileName);
                        String date = new SimpleDateFormat("yyyy-MM-dd")
                                .format(new Date(eachMp3.lastModified()));
                        MusicEntity musicEntity = new MusicEntity(date, eachMp3, false);
                        mDatas.add(musicEntity);
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    }
                }
            }
        }
        return str;
    }

    /**
     * 统计对应日期的个数
     */
    private void getDateMap(ArrayList<MusicEntity> mDatas) {
        List<String> sortList = new ArrayList<>();
        for (MusicEntity entity : mDatas) {
            sortList.add(entity.getDate());
        }
        Collections.sort(sortList);
        int count = 1;
        for (String str : sortList) {
            if (!dateMap.containsKey(str)) {
                count = 0;
                dateMap.put(str, count);
            }
            if (dateMap.containsKey(str)) {
                count++;
                dateMap.put(str, count);
            }
        }
    }
}
