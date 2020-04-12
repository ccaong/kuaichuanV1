package com.thinkd.xshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 百思移动 on 2017/10/19.
 */

@Entity
public class User {
    @Id(autoincrement = true) // id自增长
    private Long userId;

    @Index(unique = true) // 唯一性

    private String userSsid;    //手机的设备id

    private String userName;    //用户名

    private int userAvatar;  //用户头像

    private String ipAddress;

    @Generated(hash = 516287653)
    public User(Long userId, String userSsid, String userName, int userAvatar,
                String ipAddress) {
        this.userId = userId;
        this.userSsid = userSsid;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.ipAddress = ipAddress;
    }

    @Generated(hash = 586692638)
    public User() {
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

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public static String toJsonStr(User user) {
        String jsonStr = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userSsid", user.getUserSsid());
            jsonObject.put("userName", user.getUserName());
            jsonObject.put("userAvatar", user.getUserAvatar());
            jsonObject.put("ipAddress", user.getIpAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static User toObject(String jsonStr) {
        User user = new User();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String ssid = (String) jsonObject.get("userSsid");
            String name = (String) jsonObject.get("userName");
            Integer avatar = (Integer) jsonObject.get("userAvatar");
            String address = (String) jsonObject.get("ipAddress");

            user.setUserSsid(ssid);
            user.setUserName(name);
            user.setUserAvatar(avatar);
            user.setIpAddress(address);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userSsid='" + userSsid + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar=" + userAvatar +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
