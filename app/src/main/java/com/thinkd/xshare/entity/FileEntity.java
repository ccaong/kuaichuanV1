package com.thinkd.xshare.entity;

import java.util.List;

/**
 * Created by altman29 on 2017/10/21.
 * e-mial:s1yuan_chen@163.com
 */

public class FileEntity {

    private String msgId;

    private String date;

    private String historyDate;

    public String getHistoryDate() {
        return historyDate;
    }

    public void setHistoryDate(String historyDate) {
        this.historyDate = historyDate;
    }

    private List<FileInfo> imgList;
    private List<FileInfo> apkList;
    private List<FileInfo> videoList;


    public FileEntity() {
    }

    public FileEntity(String msgId, String date, List<FileInfo> imgList, List<FileInfo> apkList, List<FileInfo> videoList) {
        this.msgId = msgId;
        this.date = date;
        this.imgList = imgList;
        this.apkList = apkList;
        this.videoList = videoList;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getMsgId() {
        return msgId;
    }


    public List<FileInfo> getImgList() {
        return imgList;
    }

    public void setImgList(List<FileInfo> imgList) {
        this.imgList = imgList;
    }

    public List<FileInfo> getApkList() {
        return apkList;
    }

    public void setApkList(List<FileInfo> apkList) {
        this.apkList = apkList;
    }

    public List<FileInfo> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<FileInfo> videoList) {
        this.videoList = videoList;
    }

}
