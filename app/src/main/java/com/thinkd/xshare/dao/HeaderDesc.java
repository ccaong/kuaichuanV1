package com.thinkd.xshare.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by altman29 on 2017/10/30.
 * e-mial:s1yuan_chen@163.com
 */
@Entity
public class HeaderDesc {

    @Id(autoincrement = true) // id自增长
    private Long userId;

    @Index(unique = true)
    private String msgId;    //msgId

    private String senderName;    //发送方Name

    private int senderAvatar;  //发送方头像

    private String ReceiveName;

    @Generated(hash = 489132658)
    public HeaderDesc(Long userId, String msgId, String senderName,
            int senderAvatar, String ReceiveName) {
        this.userId = userId;
        this.msgId = msgId;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.ReceiveName = ReceiveName;
    }

    @Generated(hash = 1023097973)
    public HeaderDesc() {
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMsgId() {
        return this.msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getSenderName() {
        return this.senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public int getSenderAvatar() {
        return this.senderAvatar;
    }

    public void setSenderAvatar(int senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public String getReceiveName() {
        return this.ReceiveName;
    }

    public void setReceiveName(String ReceiveName) {
        this.ReceiveName = ReceiveName;
    }


}
