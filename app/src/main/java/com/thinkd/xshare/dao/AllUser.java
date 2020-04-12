package com.thinkd.xshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by 百思移动 on 2017/11/2.
 */

@Entity
public class AllUser {
    @Id(autoincrement = true) // id自增长
    private Long userId;

    @Index(unique = true) // 唯一性
    private String userSsid;    //手机的设备id

    private String userName;    //用户名

    private int userAvatar;  //用户头像

    @Generated(hash = 1953774232)
    public AllUser(Long userId, String userSsid, String userName, int userAvatar) {
        this.userId = userId;
        this.userSsid = userSsid;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    @Generated(hash = 1272676293)
    public AllUser() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserSsid() {
        return this.userSsid;
    }

    public void setUserSsid(String userSsid) {
        this.userSsid = userSsid;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAvatar() {
        return this.userAvatar;
    }

    public void setUserAvatar(int userAvatar) {
        this.userAvatar = userAvatar;
    }

}
