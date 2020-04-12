package com.thinkd.xshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by altman29 on 2017/10/30.
 * e-mial:s1yuan_chen@163.com
 */

@Entity
public class EachFile {

    @Id(autoincrement = true)
    private Long id;

    private String fileJson;

    @Generated(hash = 1260433690)
    public EachFile(Long id, String fileJson) {
        this.id = id;
        this.fileJson = fileJson;
    }

    @Generated(hash = 324131514)
    public EachFile() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileJson() {
        return this.fileJson;
    }

    public void setFileJson(String fileJson) {
        this.fileJson = fileJson;
    }
}
