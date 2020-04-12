package com.thinkd.xshare.history_mvp.music;

import com.thinkd.xshare.entity.MusicEntity;

import java.util.Comparator;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public class MusicComparator implements Comparator<MusicEntity> {
    @Override
    public int compare(MusicEntity o1, MusicEntity o2) {
        if (o1.getFile().lastModified() > o2.getFile().lastModified())
            return -1;
        return 1;
    }
}