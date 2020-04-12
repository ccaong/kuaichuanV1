package com.thinkd.xshare.base;

import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.thinkd.xshare.dao.DaoMaster;
import com.thinkd.xshare.dao.DaoSession;
import com.thinkd.xshare.entity.FileEntity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.ui.activity.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.fabric.sdk.android.Fabric;


/**
 * 全局的Application Context
 */
public class App extends MultiDexApplication {

    /**
     * 数据库操作对象
     */
    private static DaoSession daoSession;

    /**
     * 主要的线程池
     *
     */
    public static Executor MAIN_EXECUTOR = Executors.newFixedThreadPool(19);

    /**
     * 文件发送单线程
     */
    public static Executor FILE_SENDER_EXECUTOR = Executors.newSingleThreadExecutor();

    /**
     * 全局应用的上下文
     */
    private static App mAppContext;

    //待删除的文件集合
    private static Map<String, FileInfo> mDelFileInfoMap = new HashMap<String, FileInfo>(); //采用HashMap结构，文件地址--->>>FileInfo 映射结构，重复加入FileInfo


    //文件发送方
    private static Map<String, FileInfo> mFileInfoMap = new HashMap<String, FileInfo>(); //采用HashMap结构，文件地址--->>>FileInfo 映射结构，重复加入FileInfo


    //数据集
    private static List<FileEntity> mDatas = new ArrayList<>();

    //文件接收方
    private static Map<String, FileInfo> mReceiverFileInfoMap = new HashMap<String, FileInfo>();

    //本次接收到的文件
    private static List<FileInfo> mThisReceiverFileInfo;

    //接收到的文件
    private static List<List<FileInfo>> mThisReceiverFileInfoList;


    //外联信息
    private static List<OutReach> mOutReaches = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        initDatabase();
        mAppContext = this;
        AdAppHelper.FIREBASE = Firebase.getInstance(getApplicationContext());
        AdAppHelper.getInstance(getApplicationContext()).init();

        FacebookSdk.sdkInitialize(getApplicationContext());  //初始化facebook sdk
        AppEventsLogger.activateApp(this);   //开启facebook应用分析

    }

    private void getStuDao() {

        Log.e("getStuDao()", "run.......");

        //创建数据
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "kuaichuanuser.db", null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        if (daoMaster == null) {
            Log.e("daoMaster", "daoMaster is null...");
        } else {
            Log.e("daoMaster", "daoMaster not null...");
        }
        daoSession = daoMaster.newSession();

    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * 获取全局的AppContext
     * synchronized
     *
     * @return
     */
    public static App getAppContext() {
        return mAppContext;
    }

//    public static FileInfo getAppFileInfo(){
//        ApplicationInfo app=getApplicationInfo();
//        String filePath=app.sourceDir;
//        FileInfo fileInfo = new FileInfo();
//
//        return fileInfo;
//    }


    //==========================================================================
    //==========================================================================
    //OutReach

    /**
     * 获取全局中的outreach
     *
     * @return
     */
    public static List<OutReach> getOutReaches() {
        return mOutReaches;
    }

    /**
     * 添加一个outreach到List
     */
    public static void addOutReach(OutReach outReach) {
        mOutReaches.add(outReach);
    }

    /**
     * 删除操作 断线 清楚该outreach
     */
    public static void delOutReach(OutReach outReach) {
        mOutReaches.remove(outReach);
    }

    public static void clearOutReach() {
        if (mOutReaches != null && mOutReaches.size() > 0) {
            mOutReaches.clear();
        }
    }

    /**
     * 待删除的集合中是否存在FileInfo
     *
     * @param fileInfo
     * @return
     */
    public static boolean isDelExist(FileInfo fileInfo) {
        if (mDelFileInfoMap == null) {
            return false;
        }
        return mDelFileInfoMap.containsKey(fileInfo.getFilePath());
    }

    /**
     * 添加一个待删除的文件到集合中
     */
    public static void addFileInfoToDel(FileInfo fileInfo) {
        if (!mDelFileInfoMap.containsKey(fileInfo.getFilePath())) {
            mDelFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
        }
    }

    /**
     * 删除一个FileInfo
     *
     * @param fileInfo
     */
    public static void delFileInfoToDel(FileInfo fileInfo) {
        if (mDelFileInfoMap.containsKey(fileInfo.getFilePath())) {
            mDelFileInfoMap.remove(fileInfo.getFilePath());
        }
    }

    /**
     * 清空这个集合
     */
    public static void clearFileInfoToDel() {
        if (mDelFileInfoMap != null) {
            mDelFileInfoMap.clear();
        }
    }

    /**
     * 获取全局变量中的FileInfoMap
     *
     * @return
     */
    public static Map<String, FileInfo> getDelFileInfoMap() {
        return mDelFileInfoMap;
    }

    //==========================================================================
    //==========================================================================
    //发送方

    /**
     * 添加一个FileInfo
     *
     * @param fileInfo
     */
    public static void addFileInfo(FileInfo fileInfo) {
        if (!mFileInfoMap.containsKey(fileInfo.getFilePath())) {
            mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
        }
    }

    /**
     * 更新FileInfo
     *
     * @param fileInfo
     */
    public static void updateFileInfo(FileInfo fileInfo) {
        mFileInfoMap.put(fileInfo.getFilePath(), fileInfo);
    }

    /**
     * 删除一个FileInfo
     *
     * @param fileInfo
     */
    public static void delFileInfo(FileInfo fileInfo) {
        if (mFileInfoMap.containsKey(fileInfo.getFilePath())) {
            mFileInfoMap.remove(fileInfo.getFilePath());
        }
    }

    /**
     * 清空集合
     */
    public static void clearFileInfo() {
        mFileInfoMap.clear();

    }

    /**
     * 是否存在FileInfo
     *
     * @param fileInfo
     * @return
     */
    public static boolean isExist(FileInfo fileInfo) {
        if (mFileInfoMap == null) {
            return false;
        }
        return mFileInfoMap.containsKey(fileInfo.getFilePath());
    }


    /**
     * 判断文件集合是否有元素
     *
     * @return 有返回true， 反之
     */
    public static boolean isFileInfoMapExist() {
        if (mFileInfoMap == null || mFileInfoMap.size() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取全局变量中的FileInfoMap
     *
     * @return
     */
    public static Map<String, FileInfo> getFileInfoMap() {
        return mFileInfoMap;
    }

    /**
     * 获取即将发送文件列表的总长度
     *
     * @return
     */
    public static long getAllSendFileInfoSize() {
        long total = 0;
        for (FileInfo fileInfo : mFileInfoMap.values()) {
            if (fileInfo != null) {
                total = total + fileInfo.getSize();
            }
        }
        return total;
    }

    //==========================================================================
    //==========================================================================


    //==========================================================================
    //==========================================================================
    //发送方

    /**
     * 添加一个FileInfo
     *
     * @param fileInfo
     */

    public static void addReceiverFileInfo(FileInfo fileInfo) {
        if (!mReceiverFileInfoMap.containsKey(fileInfo.getFilePath() + fileInfo.getMsgId())) {
            mReceiverFileInfoMap.put(fileInfo.getFilePath() + fileInfo.getMsgId(), fileInfo);
        }
    }

    /**
     * /**
     * <p>
     * 更新FileInfo
     *
     * @param fileInfo
     */

    public static void updateReceiverFileInfo(FileInfo fileInfo) {
        mReceiverFileInfoMap.put(fileInfo.getFilePath() + fileInfo.getMsgId(), fileInfo);
    }

    /**
     * 更新ThisFileInfo
     *
     * @param fileInfo
     */
    public static void updateThisReceiverFileInfo(FileInfo fileInfo) {

        if (!mThisReceiverFileInfo.contains(fileInfo)) {
            mThisReceiverFileInfo.add(fileInfo);
        }
    }


    /**
     * 删除一个FileInfo
     *
     * @param fileInfo
     */
    public static void delReceiverFileInfo(FileInfo fileInfo) {
        if (mReceiverFileInfoMap.containsKey(fileInfo.getFilePath() + fileInfo.getMsgId())) {
            mReceiverFileInfoMap.remove(fileInfo.getFilePath() + fileInfo.getMsgId());
        }
    }

    /**
     * 是否存在FileInfo
     *
     * @param fileInfo
     * @return
     */
    public static boolean isReceiverInfoExist(FileInfo fileInfo) {
        if (mReceiverFileInfoMap == null) {
            return false;
        }
        return mReceiverFileInfoMap.containsKey(fileInfo.getFilePath());
    }

    /**
     * 判断文件集合是否有元素
     *
     * @return 有返回true， 反之
     */
    public static boolean isReceiverFileInfoMapExist() {
        if (mReceiverFileInfoMap == null || mReceiverFileInfoMap.size() <= 0) {
            return false;
        }
        return true;
    }

    /**
     * 获取全局变量中的FileInfoMap
     *
     * @return
     */
    public static Map<String, FileInfo> getReceiverFileInfoMap() {
        return mReceiverFileInfoMap;
    }


    /**
     * 清除map
     */
    public static void clearReceiverFileInfoMap() {
        if (mReceiverFileInfoMap != null) {
            mReceiverFileInfoMap.clear();
        }
    }

    /**
     * 获取即将接收文件列表的总长度
     *
     * @return
     */
    public static long getAllReceiverFileInfoSize() {
        long total = 0;
        for (FileInfo fileInfo : mReceiverFileInfoMap.values()) {
            if (fileInfo != null) {
                total = total + fileInfo.getSize();
            }
        }
        return total;
    }

    //==========================================================================
    //==========================================================================


    public static List<List<FileInfo>> getThisReceiverFileInfoMapList() {
        return mThisReceiverFileInfoList;
    }


    //==========================================================================
    //==========================================================================
    //数据库相关

    /**
     * 配置数据库
     */
    private void initDatabase() {
        //创建数据库kuaichuancc.db
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "kuaichuancc.db", null);
        //获取可写数据库
        SQLiteDatabase db = helper.getWritableDatabase();
        //获取数据库对象
        DaoMaster daoMaster = new DaoMaster(db);
        //获取Dao管理对象
        daoSession = daoMaster.newSession();

    }

    /**
     * 获取Dao管理对象
     */
    public static DaoSession getDaoInstance() {
        return daoSession;
    }


    /**
     * 添加数据到新数据集
     */
    public static void addToList(FileEntity fileEntity) {
        mDatas.add(fileEntity);
    }


    /**
     * 更新   ->添加
     *
     * @param fileEntity
     */
    public static void updateList(FileEntity fileEntity) {
        addToList(fileEntity);
    }

    /**
     * 获取当前数据集
     */
    public static List<FileEntity> getListData() {
        return mDatas;
    }
}
