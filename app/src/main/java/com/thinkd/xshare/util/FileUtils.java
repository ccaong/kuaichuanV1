package com.thinkd.xshare.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.bean.FileInfoBean;
import com.thinkd.xshare.entity.FileInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


/**
 * create by 百思移动
 *
 * @author 百思移动
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 自定义文件类型
     */
    public static final int TYPE_APK = 1;
    public static final int TYPE_JPEG = 2;
    public static final int TYPE_MP3 = 3;
    public static final int TYPE_MP4 = 4;

    /**
     * 默认的根目录
     */
    public static final String DEFAULT_ROOT_PATH = "/mnt/download/kuaichuan/";

    /**
     * 默认的缩略图目录
     */
    public static final String DEFAULT_SCREENSHOT_PATH = "/mnt/kc_screenshot/";

    /**
     * 小数的格式化
     */
    public static final DecimalFormat FORMAT = new DecimalFormat("####.##");
    public static final DecimalFormat FORMAT_ONE = new DecimalFormat("####.#");


    /**
     * 转换日期格式
     */
    public static String conversionTime(Context context, String time) {
        String mTime = null;
        //获取系统的时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        Date yesDate = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24);
        String todayTime = formatter.format(curDate);
        String yesTime = formatter.format(yesDate);
        if (todayTime.equals(time)) {
            mTime = context.getResources().getString(R.string.time_today);
        } else if (yesTime.equals(time)) {
            mTime = context.getResources().getString(R.string.time_yesterday);
        } else {
            String yyyy = time.substring(0, 4);
            String MM = time.substring(5, 7);
            String dd = time.substring(8, 10);

            mTime = yyyy + "." + MM + "." + dd;
//            mTime = time;
//            String MM = time.substring(5,7);
//            String dd = time.substring(8,10);
//            switch(MM){
//                case "01":
//                    mTime = "JAN "+dd;
//                    break;
//                case "02":
//                    mTime = "FEB "+dd;
//                    break;
//                case "03":
//                    mTime = "MAR "+dd;
//                    break;
//                case "04":
//                    mTime = "APR "+dd;
//                    break;
//                case "05":
//                    mTime = "MAY "+dd;
//                    break;
//                case "06":
//                    mTime = "JUN "+dd;
//                    break;
//                case "07":
//                    mTime = "JUL "+dd;
//                    break;
//                case "08":
//                    mTime = "AUG "+dd;
//                    break;
//                case "09":
//                    mTime = "SEP "+dd;
//                    break;
//                case "10":
//                    mTime = "OCT "+dd;
//                    break;
//                case "11":
//                    mTime = "NOV "+dd;
//                    break;
//                case "12":
//                    mTime = "DEC "+dd;
//                    break;
//            }
        }

        return mTime;
    }

    /**
     * 存储卡获取 指定文件
     *
     * @param context
     * @param extension
     * @return
     */
    public static List<FileInfo> getSpecificTypeFiles(Context context, String[] extension) {
        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();

        //内存卡文件的Uri
//        new File("/sdcard/").listFiles();
        Uri fileUri = MediaStore.Files.getContentUri("external");
        //筛选列，这里只筛选了：文件路径和含后缀的文件名
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE
        };

        //构造筛
        // ``选条件语句
        String selection = "";
        for (int i = 0; i < extension.length; i++) {
            if (i != 0) {
                selection = selection + " OR ";
            }
            selection = selection + MediaStore.Files.FileColumns.DATA + " LIKE '%" + extension[i] + "'";
        }
        //按时间降序条件
        String sortOrder = MediaStore.Files.FileColumns.DATE_ADDED;

        Cursor cursor = context.getContentResolver().query(fileUri, projection, selection, null, sortOrder);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String strDate = null;
                try {
                    String data = cursor.getString(0);
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFilePath(data);

                    //获得文件最后修改的时间
                    File file1 = new File(data);
                    Date date = new Date(file1.lastModified());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    strDate = sdf.format(date);
                    fileInfo.setDate(strDate);

                    long size = 0;
                    try {
                        File file = new File(data);
                        size = file.length();
                        fileInfo.setSize(size);
                    } catch (Exception e) {

                    }
                    fileInfoList.add(fileInfo);
                } catch (Exception e) {
                    Log.i("FileUtils", "------>>>" + e.getMessage());
                }
            }
        }
        Log.i(TAG, "getSize ===>>> " + fileInfoList.size());
        return fileInfoList;
    }

    //按照时间给list分组
    public static List<FileInfoBean> groupByTimeToList(List<FileInfo> list) {
        List<FileInfoBean> mFileInfoBeanList = new ArrayList<>();

        TreeMap<String, FileInfoBean> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                String[] sdate1 = o1.split("-");
                String[] sdate2 = o2.split("-");
                int sdate1hour = Integer.parseInt(sdate1[0]);
                int sdate2hour = Integer.parseInt(sdate2[0]);
                if (sdate1hour > sdate2hour) {
                    return -1;
                } else if (sdate1hour < sdate2hour) {
                    return 1;
                } else {
                    int sdate1minute = Integer.parseInt(sdate1[1]);
                    int sdate2minute = Integer.parseInt(sdate2[1]);
                    if (sdate1minute > sdate2minute) {
                        return -1;
                    } else if (sdate1minute < sdate2minute) {
                        return 1;
                    } else {
                        int sdate1second = Integer.parseInt(sdate1[2]);
                        int sdate2second = Integer.parseInt(sdate2[2]);
                        if (sdate1second > sdate2second) {
                            return -1;
                        } else if (sdate1second < sdate2second) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
        });
        for (int i = 0; i < list.size(); i++) {
            FileInfo fileInfo = list.get(i);
            FileInfoBean bean = map.get(fileInfo.getDate());
            if (bean == null) {
                bean = new FileInfoBean();
                bean.setChecked(false);
                bean.setFileDate(fileInfo.getDate());
                bean.setFileInfoList(new ArrayList<FileInfo>());
                map.put(fileInfo.getDate(), bean);
            }
            bean.getFileInfoList().add(fileInfo);

        }

        for (Map.Entry<String, FileInfoBean> entry : map.entrySet()) {
            String key = entry.getKey();
            int countOneDay = entry.getValue().getFileInfoList().size();

            mFileInfoBeanList.add(entry.getValue());

        }
        return mFileInfoBeanList;
    }

    /**
     * 查找指定文件名的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static FileInfo getFileInfo(Context context, String fileName) {
        List<FileInfo> fileInfoList = getSpecificTypeFiles(context, new String[]{fileName});

        if (fileInfoList == null || fileInfoList.size() == 0) {
            return null;
        }
        return fileInfoList.get(0);
    }

    /**
     * 转化完整信息的FileInfo
     *
     * @param context
     * @param fileInfoList
     * @param type
     * @return
     */
    public static List<FileInfo> getDetailFileInfos(Context context, List<FileInfo> fileInfoList, int type) {

        if (fileInfoList == null || fileInfoList.size() <= 0) {
            return fileInfoList;
        }
        for (FileInfo fileInfo : fileInfoList) {
            if (fileInfo != null) {

                //设置文件大小
                fileInfo.setSizeDesc(getFileSize(fileInfo.getSize()));
                if (type == FileInfo.TYPE_APK) {
                    //设置apk的图标
                    fileInfo.setBitmap(FileUtils.drawableToBitmap(FileUtils.getApkThumbnail(context, fileInfo.getFilePath())));
                } else if (type == FileInfo.TYPE_MP4) {
                    //设置文件名
                    fileInfo.setName(getFileName(fileInfo.getFilePath()));
                    //设置视频缩略图
                    fileInfo.setBitmap(FileUtils.getScreenshotBitmap(context, fileInfo.getFilePath(), FileInfo.TYPE_MP4));
                } else if (type == FileInfo.TYPE_MP3) {
                    //设置文件名
                    fileInfo.setName(getFileName(fileInfo.getFilePath()));//mp3不需要缩略图

                } else if (type == FileInfo.TYPE_JPG) {
                    //设置文件名
                    fileInfo.setName(getFileName(fileInfo.getFilePath()));//由Glide图片加载框架加载
                }
                //设置文件的类型
                fileInfo.setFileType(type);
            }
        }
        return fileInfoList;
    }

    /**
     * 根据文件路径获取文件的名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (filePath == null || filePath.equals("")) return "";
        return filePath.substring(filePath.lastIndexOf("/") + 1);
    }

    /**
     * 获取文件的根目录
     *
     * @return
     */
    public static String getRootDirPath() {
        String path = DEFAULT_ROOT_PATH;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/kuaichuan/";
        }
        return path;
    }

    /**
     * 获取文件缩略图目录
     *
     * @return
     */
    public static String getScreenShotDirPath() {
        String path = DEFAULT_SCREENSHOT_PATH;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/kc_screenshot/";
        }
        return path;
    }

    /**
     * 获取指定的文件夹路径
     *
     * @param type @@See FileInfo.java
     * @return
     */
    public static String getSpecifyDirPath(int type) {
        String dirPath = getRootDirPath();

        switch (type) {
            case FileInfo.TYPE_APK: {
                dirPath = dirPath + "apk/";
                break;
            }
            case FileInfo.TYPE_JPG: {
                dirPath = dirPath + "jpg/";
                break;
            }
            case FileInfo.TYPE_MP3: {
                dirPath = dirPath + "mp3/";
                break;
            }
            case FileInfo.TYPE_MP4: {
                dirPath = dirPath + "mp4/";
                break;
            }
            default:
                dirPath = dirPath + "other/";
                break;
        }

        return dirPath;
    }

    /**
     * 生成本地文件路径
     *
     * @param fileInfo
     * @return
     */
    public static File gerateLocalFile(FileInfo fileInfo) {
        String fileName = null;
        if (fileInfo.getFileType() == FileInfo.TYPE_APK) {
            fileName = fileInfo.getName() + ".apk";
        } else {
            fileName = fileInfo.getName();
        }
//        else if (fileInfo.getFileType() == FileInfo.TYPE_JPG) {
//            fileName = fileInfo.getName() + ".jpg";
//        } else if (fileInfo.getFileType() == FileInfo.TYPE_MP4) {
//            fileName = fileInfo.getName() + ".mp4";
//        } else {
//            fileName = fileInfo.getName();
//        }

        String dirPath = getRootDirPath();

        if (fileName.lastIndexOf(FileInfo.EXTEND_APK) > 0) {
            dirPath = getSpecifyDirPath(FileInfo.TYPE_APK);
        } else if (fileName.lastIndexOf(FileInfo.EXTEND_JPG) > 0) {
            dirPath = getSpecifyDirPath(FileInfo.TYPE_JPG);
        } else if (fileName.lastIndexOf(FileInfo.EXTEND_MP3) > 0) {
            dirPath = getSpecifyDirPath(FileInfo.TYPE_MP3);
        } else if (fileName.lastIndexOf(FileInfo.EXTEND_MP4) > 0) {
            dirPath = getSpecifyDirPath(FileInfo.TYPE_MP4);
        } else {
            dirPath = getSpecifyDirPath(-1);
        }

//        String dirPath =  Environment.getExternalStorageDirectory() + "/kuaichuan/";

        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirFile, fileName);
        return file;
    }


    /**
     * 根据传入的byte数量转换为对应的byte, Kbyte, Mbyte, Gbyte单位的字符串
     *
     * @param size byte数量
     * @return
     */
    public static String getFileSize(long size) {
        if (size < 0) { //小于0字节则返回0
            return "0B";
        }

        double value = 0f;
        if ((size / 1024) < 1) { //0 ` 1024 byte
            return size + "B";
        } else if ((size / (1024 * 1024)) < 1) {//0 ` 1024 kbyte

            value = size / 1024f;
            return FORMAT.format(value) + "KB";
        } else if (size / (1024 * 1024 * 1024) < 1) {                  //0 ` 1024 mbyte
            value = (size * 100 / (1024 * 1024)) / 100f;
            return FORMAT.format(value) + "MB";
        } else {                  //0 ` 1024 mbyte
            value = (size * 100L / (1024L * 1024L * 1024L)) / 100f;
            return FORMAT.format(value) + "GB";
        }
    }


    /**
     * 转换为流量数组
     * String[0] 为数值
     * String[1] 为单位
     * 1024 ===》》》 1 k
     *
     * @param size
     * @return
     */
    public static String[] getFileSizeArrayStr(long size) {
        String[] result = new String[2];
        if (size < 0) { //小于0字节则返回0
            result[0] = "0";
            result[1] = "B";
            return result;
        }

        double value = 0f;
        if ((size / 1024) < 1) { //0 ` 1024 byte
            result[0] = FORMAT_ONE.format(size);
            result[1] = "B";
//            return  size + "B";
        } else if ((size / (1024 * 1024)) < 1) {//0 ` 1024 kbyte
            value = size / 1024f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "KB";
//            return  FORMAT.format(value) + "KB";
        } else if (size / (1024 * 1024 * 1024) < 1) {                  //0 ` 1024 mbyte
            value = (size * 100 / (1024 * 1024)) / 100f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "MB";
//            return  FORMAT.format(value) + "MB";
        } else {                  //0 ` 1024 mbyte
            value = (size * 100L / (1024L * 1024L * 1024L)) / 100f;
            result[0] = FORMAT_ONE.format(value);
            result[1] = "GB";
//            return  FORMAT.format(value) + "GB";
        }

        return result;
    }

    /**
     * 转换为时间数组
     * String[0] 为数值
     * String[1] 为单位
     * 61 ===》》》 1.05秒
     *
     * @param second
     * @return
     */
    public static String[] getTimeByArrayStr(long second) {
        String[] result = new String[2];
        if (second < 0) { //小于0字节则返回0
            result[0] = "0";
            result[1] = "SEC";
            return result;
        }

        double value = 0.0f;
        if (second / (60f * 1000f) < 1) { //秒
            result[0] = String.valueOf(second / 1000);
            result[1] = "SEC";
//            return  size + "B";
        } else if ((second / (60f * 60f * 1000f)) < 1) {//分
            value = second / (60f * 1000f);
            result[0] = FORMAT_ONE.format(value);
            result[1] = "MIN";
//            return  FORMAT.format(value) + "KB";
        } else {                              //时
            value = second / (60f * 60f * 1000f);
            result[0] = FORMAT_ONE.format(value);
            result[1] = "时";
        }

        return result;
    }


    /**
     * 判断文件是否为Apk安装文件
     *
     * @param filePath
     * @return
     */
    public static boolean isApkFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_APK) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否为图片
     *
     * @param filePath
     * @return
     */
    public static boolean isJpgFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_JPG) > 0 || filePath.lastIndexOf(FileInfo.EXTEND_JPEG) > 0 || filePath.lastIndexOf(FileInfo.EXTEND_PNG) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否为PNG
     *
     * @param filePath
     * @return
     */
    public static boolean isPngFile(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_PNG) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否为Mp3
     *
     * @param filePath
     * @return
     */
    public static boolean isMp3File(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_MP3) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否为Mp4
     *
     * @param filePath
     * @return
     */
    public static boolean isMp4File(String filePath) {
        if (filePath == null || filePath.equals("")) {
            return false;
        }
        if (filePath.lastIndexOf(FileInfo.EXTEND_MP4) > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取缩略图的Bitmap
     *
     * @param filePath
     * @param type
     * @return
     */
    public static Bitmap getScreenshotBitmap(Context context, String filePath, int type) {
        Bitmap bitmap = null;
        switch (type) {
            case TYPE_APK: {
                Drawable drawable = getApkThumbnail(context, filePath);
                if (drawable != null) {
                    bitmap = drawableToBitmap(drawable);
                } else {
//                    bitmap = drawableToBitmap()
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                }
                break;
            }
            case TYPE_JPEG: {
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)));
                } catch (FileNotFoundException e) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.nophoto);
                }
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
            }
            case TYPE_MP3: {
                /*
                try {
                    bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(filePath)));
                } catch (FileNotFoundException e) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_mp3);
                }
                */
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon_mp3);
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
            }
            case TYPE_MP4: {
                try {
                    bitmap = ScreenshotUtils.createVideoThumbnail(filePath);
                } catch (Exception e) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.novideo);
                }
                bitmap = ScreenshotUtils.extractThumbnail(bitmap, 100, 100);
                break;
            }
        }

        return bitmap;
    }

    /**
     * 获取Apk文件的Log图标
     *
     * @param context
     * @param apk_path
     * @return
     */
    public static Drawable getApkThumbnail(Context context, String apk_path) {
        if (context == null) {
            return null;
        }

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            /**获取apk的图标 */
            appInfo.sourceDir = apk_path;
            appInfo.publicSourceDir = apk_path;
            if (appInfo != null) {
                Drawable apk_icon = appInfo.loadIcon(pm);
                return apk_icon;
            }
        } catch (Exception e) {

        }

        return null;
    }

    /**
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取专辑封面
     */
    public static Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
    }


    //----------------------------
    //1.压缩功能， 压缩到指定大小
    //2.Drawable --->>> Bitmap
    //3.Bitmap   --->>> byte[]
    //4.Bitmap   --->>> 生成图片


    /**
     * Drawable转Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //建立对应的Bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);

        return bitmap;
    }


    /**
     * Bitmap转ByteArray
     *
     * @param bitmap
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bitmap 写入到SD卡
     *
     * @param bitmap
     * @param resPath
     * @return
     */
    public static boolean bitmapToSDCard(Bitmap bitmap, String resPath) {
        if (bitmap == null) {
            return false;
        }
        File resFile = new File(resPath);
        try {
            FileOutputStream fos = new FileOutputStream(resFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Bitmap压缩到指定的千字节数（比方说图片要压缩成32K，则传32）
     *
     * @param srcBitmap
     * @param maxKByteCount 比方说图片要压缩成32K，则传32
     * @return
     */
    public static Bitmap compressBitmap(Bitmap srcBitmap, int maxKByteCount) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            int option = 98;
            while (baos.toByteArray().length / 1024 >= maxKByteCount && option > 0) {
                baos.reset();
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 2;
            }
        } catch (Exception e) {

        }
//        bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(bais, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    /**
     * 压缩图片到指定的文件去————注意，图片尺寸没变，变的只是文件大小（图片的位深度改变了）
     *
     * @param srcBitmap
     * @param maxKByteCount 最大千字节数（比方说图片要压缩成32K，则传32）
     * @param targetPath    目标图片地址
     * @throws IOException
     */
    public static boolean compressBitmap(Bitmap srcBitmap, int maxKByteCount, String targetPath) {
        boolean result = false;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int option = 98;
            while (baos.toByteArray().length / 1024 >= maxKByteCount && option > 0) {
                baos.reset();
                srcBitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                option -= 2;
            }
            byte[] bitmapByte = baos.toByteArray();

            File targetFile = new File(targetPath);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(targetFile);
            fos.write(bitmapByte);

            result = true;

            try {
                fos.close();
                baos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!srcBitmap.isRecycled()) {
                srcBitmap.recycle();
                srcBitmap = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //----------------------------


    /**
     * 获取接收到文件数量
     *
     * @return
     */
    public static int getReceiveFileCount() {
        int count = 0;
        File rootDir = new File(getRootDirPath());
        if (rootDir != null) {
            count = getFileCount(rootDir);
        }
        return count;
    }

    /**
     * 获取指定文件夹下面的文件数
     *
     * @param rootDir
     * @return
     */
    public static int getFileCount(File rootDir) {
        int count = 0;
        if (rootDir != null && rootDir.exists()) {
            for (File file : rootDir.listFiles()) {
                if (file.isDirectory()) {
                    count = count + getFileCount(file);
                } else {
                    count++;
                }
            }
        }
        return count;
    }


    /**
     * 获取接收到全部的文件大小
     *
     * @return
     */
    public static String getReceiveFileListTotalLength() {
        long total = 0;
        File rootDir = new File(getRootDirPath());
        if (rootDir != null) {
            total = getFileLength(rootDir);
        }
        return getFileSize(total);
    }

    /**
     * 递归获取指定文件夹的大小
     *
     * @param rootDir
     * @return
     */
    public static long getFileLength(File rootDir) {
        long len = 0;
        if (rootDir != null && rootDir.exists()) {
            for (File f : rootDir.listFiles()) {
                if (f.isDirectory()) {
                    len = len + getFileLength(f);
                } else {
                    len = len + f.length();
                }
            }
        }
        return len;
    }

    /**
     * 打开文件
     *
     * @param context
     * @param filePath
     */
    public static void openFile(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
//        Uri uri = Uri.parse(filePath);
        Uri uri = Uri.fromFile(new File(filePath));
        if (FileUtils.isJpgFile(filePath)) {//图片格式
//            "image/*"
            intent.setDataAndType(uri, "image/*");
        } else if (FileUtils.isMp3File(filePath)) {//音乐格式
//            "audio/*"
            intent.setDataAndType(uri, "audio/*");
        } else if (FileUtils.isMp4File(filePath)) {//视屏格式
//            "video/*"
            intent.setDataAndType(uri, "video/*");
        }
        context.startActivity(intent);
    }


    /**
     * 远程的FilePath获取本地的FilePath
     *
     * @param remoteFilePath
     * @return
     */
    public static String getLocalFilePath(String remoteFilePath) {
        String localFilePath = "";
        if (FileUtils.isApkFile(remoteFilePath)) {//APK应用格式
            localFilePath = getSpecifyDirPath(FileInfo.TYPE_APK) + getFileName(remoteFilePath);
        } else if (FileUtils.isJpgFile(remoteFilePath)) {//图片格式
            localFilePath = getSpecifyDirPath(FileInfo.TYPE_JPG) + getFileName(remoteFilePath);
        } else if (FileUtils.isMp3File(remoteFilePath)) {//音乐格式
            localFilePath = getSpecifyDirPath(FileInfo.TYPE_MP3) + getFileName(remoteFilePath);
        } else if (FileUtils.isMp4File(remoteFilePath)) {//视屏格式
            localFilePath = getSpecifyDirPath(FileInfo.TYPE_MP4) + getFileName(remoteFilePath);
        }
        return localFilePath;
    }


    /**
     * 判断文件的缩略图是否存在
     *
     * @param fileName
     * @return
     */
    public static boolean isExistScreenShot(String fileName) {
        File file = new File(FileUtils.getScreenShotDirPath() + fileName);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 获取文件缩略图的路径
     *
     * @param fileName
     * @return
     */
    public static String getScreenShotFilePath(String fileName) {
        File dirFile = new File(FileUtils.getScreenShotDirPath());
        if (!dirFile.exists()) dirFile.mkdirs();

        if (isMp3File(fileName)) {
            return FileUtils.getScreenShotDirPath() + "mp3.png";
        }
        return FileUtils.getScreenShotDirPath() + fileName.replace(".", "_") + ".png";
    }


    /**
     * 自动生成缩略图
     *
     * @param context
     * @param filePath
     * @return
     */
    public synchronized static void autoCreateScreenShot(Context context, String filePath) throws IOException {
        String fileName = FileUtils.getFileName(filePath);

        File screenshotFile = null;
        Bitmap screenshotBitmap = null;
        FileOutputStream fos = null;

        //check the screenshot image file exist in disk? if exist return the file, or create the screen image file
        if (FileUtils.isApkFile(filePath)) {//apk 缩略图处理
            if (!FileUtils.isExistScreenShot(fileName)) {
                screenshotFile = new File(getScreenShotFilePath(fileName));
                if (!screenshotFile.exists()) screenshotFile.createNewFile();
                fos = new FileOutputStream(screenshotFile);
                screenshotBitmap = ApkUtils.drawableToBitmap(ApkUtils.getApkThumbnail(context, filePath));
                screenshotBitmap = ScreenshotUtils.extractThumbnail(screenshotBitmap, 96, 96);
                screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
        } else if (FileUtils.isJpgFile(filePath)) {//jpg 缩略图处理
            if (!FileUtils.isExistScreenShot(fileName)) {
                screenshotFile = new File(getScreenShotFilePath(fileName));
                if (!screenshotFile.exists()) screenshotFile.createNewFile();
                fos = new FileOutputStream(screenshotFile);
                screenshotBitmap = FileUtils.getScreenshotBitmap(context, filePath, FileInfo.TYPE_JPG);
                screenshotBitmap = ScreenshotUtils.extractThumbnail(screenshotBitmap, 96, 96);
                screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }

        } else if (FileUtils.isMp3File(filePath)) {//mp3 缩略图处理
            //DO NOTHING mp3文件可以没有缩略图 可指定
            screenshotFile = new File(FileUtils.getScreenShotDirPath() + "mp3.png");
            if (!screenshotFile.exists()) screenshotFile.createNewFile();
            fos = new FileOutputStream(screenshotFile);
            screenshotBitmap = FileUtils.getScreenshotBitmap(context, filePath, FileInfo.TYPE_MP3);
            screenshotBitmap = ScreenshotUtils.extractThumbnail(screenshotBitmap, 96, 96);
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } else if (FileUtils.isMp4File(filePath)) {//MP4 缩略图处理
            if (!FileUtils.isExistScreenShot(fileName)) {
                screenshotFile = new File(getScreenShotFilePath(fileName));
                if (!screenshotFile.exists()) screenshotFile.createNewFile();
                fos = new FileOutputStream(screenshotFile);
                screenshotBitmap = FileUtils.getScreenshotBitmap(context, filePath, FileInfo.TYPE_MP4);
                screenshotBitmap = ScreenshotUtils.extractThumbnail(screenshotBitmap, 96, 96);
                screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
        } else if (FileUtils.isMp4File(filePath)) {//MP4 缩略图处理
            screenshotFile = new File(FileUtils.getScreenShotDirPath() + "logo.png");
            if (!screenshotFile.exists()) screenshotFile.createNewFile();
            fos = new FileOutputStream(screenshotFile);
            screenshotBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
            screenshotBitmap = ScreenshotUtils.extractThumbnail(screenshotBitmap, 96, 96);
            screenshotBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }

        if (screenshotBitmap != null) {
            screenshotBitmap.recycle();
        }

        if (fos != null) {
            fos.close();
            fos = null;
        }
    }


    /****
     * 计算文件大小
     *
     * @param length
     * @return
     */
    public static String showLongFileSzie(Long length) {
        if (length >= 1048576) {
            return (length / 1048576) + "MB";
        } else if (length >= 1024) {
            return (length / 1024) + "KB";
        } else if (length < 1024) {
            return length + "B";
        } else {
            return "0KB";
        }
    }


    public static void main(String[] args) {
        System.out.println("Test getTimeByArrayStr(59 * 1000)----->>>" + getTimeByArrayStr(59 * 1000)[0]
                + " , " + getTimeByArrayStr(59 * 1000)[1]);

        System.out.println("Test getTimeByArrayStr(59 * 1000)----->>>" + getTimeByArrayStr(59 * 1001)[0]
                + " , " + getTimeByArrayStr(59 * 1001)[1]);

        System.out.println("Test getTimeByArrayStr(59 * 1000)----->>>" + getTimeByArrayStr(59 * 1000 * 100)[0]
                + " , " + getTimeByArrayStr(59 * 1000 * 100)[1]);
    }

}
