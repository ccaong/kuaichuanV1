package com.thinkd.xshare.entity;

import java.io.File;

/**
 * Created by altman29 on 2017/10/18.
 * e-mial:s1yuan_chen@163.com
 */

public class MusicEntity {

    private String date;//2017-10-12

    private File file;

    private boolean isCheck;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public MusicEntity() {

    }

    public MusicEntity(String date, File file, boolean isCheck) {

        this.date = date;
        this.file = file;
        this.isCheck = isCheck;
    }

}
