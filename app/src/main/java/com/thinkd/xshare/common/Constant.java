package com.thinkd.xshare.common;

import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.FileUtils;

import java.util.Comparator;
import java.util.Map;


/**
 * 常量类
 */
public class Constant {

    /**
     * 默认的Wifi SSID
     */
    public static final String DEFAULT_SSID = "XD_HOTSPOT";

    /**
     * ServerSocket 默认IP
     */
    public static final String DEFAULT_SERVER_IP = "192.168.43.1";

    /**
     * Wifi连接上时 未分配默认的Ip地址
     */
    public static final String DEFAULT_UNKOWN_IP = "0.0.0.0";

    /**
     * 最大尝试数
     */
    public static final int DEFAULT_TRY_TIME = 10;

    /**
     * 文件传输监听 默认端口
     */
    public static final int DEFAULT_SERVER_PORT = 8080;

    /**
     * UDP通信服务 默认端口
     */
    public static final int DEFAULT_SERVER_COM_PORT = 8099;

    public static final int DEFAULT_SERVER_COM_PORT_1 = 8091;

    public static final int DEFAULT_SERVER_COM_PORT_2 = 8095;

    /**
     * Android微型服务器 默认端口
     */
    public static final int DEFAULT_MICRO_SERVER_PORT = 3999;


    /**
     * wifi scan result key
     */
    public static final String KEY_SCAN_RESULT = "KEY_SCAN_RESULT";

    public static final String KEY_IP_PORT_INFO = "KEY_IP_PORT_INFO";


    /**
     * 网页传标识
     */
    public static final String KEY_WEB_TRANSFER_FLAG = "KEY_WEB_TRANSFER_FLAG";

    /**
     * 文件发送方 与 文件接收方 通信信息
     */
    public static final String MSG_WAITING_JOIN_INIT = "MSG_WAITING_JOIN_INIT";
    public static final String MSG_FILE_RECEIVER_INIT = "MSG_FILE_RECEIVER_INIT";
    public static final String MSG_FILE_RECEIVER_INIT_SUCCESS = "MSG_FILE_RECEIVER_INIT_SUCCESS";
    public static final String MSG_FILE_SENDER_START = "MSG_FILE_SENDER_START";
    public static final String MSG_INIT = "MSG_INIT";
    public static final String MSG_CREATE_GROUP_INIT = "MSG_CREATE_GROUP_INIT";
    public static final String MSG_JOIN_GROUP_NIT = "MSG_JOIN_GROUP_NIT";
    public static final String MSG_QUIT_GROUP = "MSG_QUIT_GROUP";

    //FileInfoMap 默认的Comparator
    public static final Comparator<Map.Entry<String, FileInfo>> DEFAULT_COMPARATOR = new Comparator<Map.Entry<String, FileInfo>>() {
        @Override
        public int compare(Map.Entry<String, FileInfo> o1, Map.Entry<String, FileInfo> o2) {
            if (o1.getValue().getFileType() > o2.getValue().getFileType()) {
                return 1;
            } else if (o1.getValue().getFileType() < o2.getValue().getFileType()) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    //    FileInfoMap 默认的Comparator2
    public static final Comparator<FileInfo> DEFAULT_COMPARATOR2 = new Comparator<FileInfo>() {
        @Override
        public int compare(FileInfo o1, FileInfo o2) {
            if (o1.getFileType() > o2.getFileType()) {
                return 1;
            } else if (o1.getFileType() < o2.getFileType()) {
                return -1;
            } else {
                return 0;
            }
        }
    };


    /**
     * asset 资源名称
     */
    public static final String NAME_FILE_TEMPLATE = "file.template";
    public static final String NAME_CLASSIFY_TEMPLATE = "classify.template";


    public static final int SPEED_BYTE = 1024 * 8;


    /**
     * music文件路径
     */
    public static final String MUSIC_FILE_PATH = FileUtils.getRootDirPath() + "mp3";


}

