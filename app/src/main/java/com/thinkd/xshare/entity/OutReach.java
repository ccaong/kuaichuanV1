package com.thinkd.xshare.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by altman29 on 2017/10/28.
 * e-mial:s1yuan_chen@163.com
 * 外联信息 eachone's ssid,avatarOd,mame,ipAddress;
 * <p>
 * ChooseAc->send click{send msg}
 * <p>
 * Share->msg receive{update List<OutReach>}
 * part1：完成传输列表正常显示
 * <p>
 * <p>
 * part2：下发
 * 服务端只有一个，客户端数量若干。
 * 每有新成员加入，通过某种方式，服务端更新全局维护的List<OutReach>,接着
 */

public class OutReach {

    /**
     * 外联设备的ssid
     */
    private String ssId;

    /**
     * 外联设备的Avatar
     */
    private int avatarId;

    /**
     * 外联用户名
     */
    private String name;



    /**
     * 外联地址
     */
    private String ipAddress;

    public OutReach() {
    }

    public OutReach(String ssId, int avatarId, String name, String ipAddress) {
        this.ssId = ssId;
        this.avatarId = avatarId;
        this.name = name;
        this.ipAddress = ipAddress;
    }

    public String getSsId() {
        return ssId;
    }

    public void setSsId(String ssId) {
        this.ssId = ssId;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public static String toJsonStr(OutReach outReach) {
        String jsonStr = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("ssId", outReach.getSsId());
            jsonObject.put("avatarId", outReach.getAvatarId());
            jsonObject.put("name", outReach.getName());
            jsonObject.put("ipAddress", outReach.getIpAddress());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static OutReach toObject(String jsonStr) {
        OutReach outReach = new OutReach();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            String ssId = jsonObject.getString("ssId");
            int ava = jsonObject.getInt("avatarId");
            String name = jsonObject.getString("name");
            String ipAddress = jsonObject.getString("ipAddress");

            outReach.setSsId(ssId);
            outReach.setAvatarId(ava);
            outReach.setName(name);
            outReach.setIpAddress(ipAddress);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outReach;
    }

    @Override
    public String toString() {
        return "OutReach{" +
                "ssId='" + ssId + '\'' +
                ", avatarId=" + avatarId +
                ", name='" + name + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}
