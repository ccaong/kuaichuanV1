package com.thinkd.xshare.adapter.bean;

import com.thinkd.xshare.entity.FileInfo;

import java.util.List;

/**
 * Created by 百思移动 on 2017/10/26.
 */

public class FileInfoBean {
    public String fileDate;
    public List<FileInfo> fileInfoList;
    public boolean isChecked;

    public FileInfoBean(){

    }


    public FileInfoBean(String fileDate, List<FileInfo> fileInfoList, boolean isChecked) {
        this.fileDate = fileDate;
        this.fileInfoList = fileInfoList;
        this.isChecked = isChecked;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }

    public List<FileInfo> getFileInfoList() {
        return fileInfoList;
    }

    public void setFileInfoList(List<FileInfo> fileInfoList) {
        this.fileInfoList = fileInfoList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
