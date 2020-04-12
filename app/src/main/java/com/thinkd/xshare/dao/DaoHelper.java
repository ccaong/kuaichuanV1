package com.thinkd.xshare.dao;

import com.thinkd.xshare.base.App;

import java.util.List;

/**
 * Created by 百思移动 on 2017/10/20.
 */

public class DaoHelper {

    //增加
    public static void insert(User user) {
        App.getDaoSession().getUserDao().insert(user);
    }

    //增加（有返回值）
    public static long inserts(User user) {
        return App.getDaoSession().getUserDao().insert(user);
    }

    //根据ssid查找信息
    public static List<User> queryById(String key) {
        return App.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.UserSsid.eq(key)).list();
    }

    /**
     * 单个
     *
     * @param key
     * @return
     */
    public static User getUserBySsidd(String key) {
        User user = App.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.UserSsid.eq(key)).build().unique();
        return user;
    }

    //更新(ssid,用户名，用户头像id)
    public static void updateUserInfo(String ssid, String edName, int id, String ipAddress) {
        User user = App.getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.UserSsid.eq(ssid)).build().unique();
        if (user != null) {
            user.setUserName(edName);
            user.setUserAvatar(id);
            user.setIpAddress(ipAddress);
            App.getDaoSession().getUserDao().update(user);
        }
    }

    public static void update(User bean) {
        App.getDaoInstance().getUserDao().update(bean);
    }

    ///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>HeaderDesc  已存

    public static long inserts(HeaderDesc headerDesc) {
        return App.getDaoInstance().getHeaderDescDao().insert(headerDesc);
    }

    public static HeaderDesc queryByMsgId(String msgId) {
        return App.getDaoInstance().getHeaderDescDao().queryBuilder().where(HeaderDescDao.Properties.MsgId.eq(msgId)).build().unique();
    }


    ///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>EachFile  已存
    public static long inserts(EachFile file) {
        return App.getDaoInstance().getEachFileDao().insert(file);
    }

    public static List<EachFile> queryAll() {
        return App.getDaoInstance().getEachFileDao().queryBuilder().build().list();
    }


    ///>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>存储所有用户信息的表
    //增加
    public static void insert(AllUser allUser) {
        App.getDaoSession().getAllUserDao().insert(allUser);
    }

    //增加（有返回值）
    public static long inserts(AllUser allUser) {
        return App.getDaoSession().getAllUserDao().insert(allUser);
    }

    //修改信息用户
    public static void updateAllUserInfo(String ssid, String edName, int id) {
        AllUser alluser = App.getDaoSession().getAllUserDao().queryBuilder().where(AllUserDao.Properties.UserSsid.eq(ssid)).build().unique();
        if (alluser != null) {
            alluser.setUserName(edName);
            alluser.setUserAvatar(id);
            App.getDaoSession().getAllUserDao().update(alluser);
        }
    }

    //根据ssid查找信息
    public static List<AllUser> queryBySSId(String key) {
        return App.getDaoSession().getAllUserDao().queryBuilder().where(AllUserDao.Properties.UserSsid.eq(key)).list();
    }

    public static AllUser querySingleBySSId(String key) {
        return App.getDaoInstance().getAllUserDao().queryBuilder().where(AllUserDao.Properties.UserSsid.eq(key)).build().unique();
    }


}