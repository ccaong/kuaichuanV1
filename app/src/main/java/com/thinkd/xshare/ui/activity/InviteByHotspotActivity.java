package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.receiver.WifiAPBroadcastReceiver;
import com.thinkd.xshare.server.AndroidDownloadServer;
//import com.thinkd.xshare.server.AndroidMicroServer;
import com.thinkd.xshare.util.ApMgr;
import com.thinkd.xshare.util.DensityUtil;
import com.thinkd.xshare.util.NetUtils;
import com.thinkd.xshare.util.WifiMgr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.REQUEST_CODE_WRITE_SETTINGS;
import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

//import com.thinkd.xshare.server.AndroidDownloadServer;

/**
 * Created by 百思移动 on 2017/10/21.
 */

public class InviteByHotspotActivity extends BaseActivity {

    //二维码相关
    private static final int QR_WIDTH = DensityUtil.dip2px(getContext(), 140);
    private static final int QR_HEIGHT = DensityUtil.dip2px(getContext(), 140);

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.iv_qr_code_url)
    ImageView ivQrCodeUrl;
    @Bind(R.id.tv_url)
    TextView tvUrl;
    @Bind(R.id.tv_hotspot_name)
    TextView tvHotspotName;

    WifiAPBroadcastReceiver mWifiAPBroadcastReceiver;
    boolean mIsInitialized = false;
//    AndroidMicroServer mAndroidMicroServer = null;
    AndroidDownloadServer mAndroidDownloadServer = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_by_hotspot);
        ButterKnife.bind(this);
        tvTitle.setText("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.hotspot));
        //设置二维码
        ivQrCodeUrl.setImageBitmap(createImage(tvUrl.getText().toString()));
        //修改系统设置
        initWithGetPermission(this);
        //读写sd的权限
        verifyStoragePermissions(this);

        //添加应用信息，在程序中获取App
        ApplicationInfo app = getApplicationInfo();
        String filePath = app.sourceDir;
        Log.e("在程序中获取app路径", filePath);
        //将获取到的apk保存成File类型
        File apkFile = new File(filePath);

        //在指定的文件夹下创建名为share.apk  的文件
        File shareFile = new File("/sdcard/Xshare.apk");
        try {
            //将apk复制到share.apk中
            doCopyFile(apkFile, shareFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //修改系统设置
    public void initWithGetPermission(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            //开启热点
            init();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
            } else {
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, REQUEST_CODE_WRITE_SETTINGS);
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将安装包复制到指定的目录下
     * @param srcFile
     * @param destFile
     * @param preserveFileDate
     * @throws IOException
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination \'" + destFile + "\' exists but is a directory");
        } else {
            FileInputStream fis = null;
            FileOutputStream fos = null;
            FileChannel input = null;
            FileChannel output = null;

            long srcLen;
            long dstLen;
            try {
                fis = new FileInputStream(srcFile);
                fos = new FileOutputStream(destFile);
                input = fis.getChannel();
                output = fos.getChannel();
                srcLen = input.size();
                dstLen = 0L;

                long bytesCopied;
                for (long count = 0L; dstLen < srcLen; dstLen += bytesCopied) {
                    long remain = srcLen - dstLen;
                    count = remain > 31457280L ? 31457280L : remain;
                    bytesCopied = output.transferFrom(input, dstLen, count);
                    if (bytesCopied == 0L) {
                        break;
                    }
                }
            } finally {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            }

            srcLen = srcFile.length();
            dstLen = destFile.length();
            if (srcLen != dstLen) {
                throw new IOException("Failed to copy full contents from \'" + srcFile + "\' to \'" + destFile + "\' Expected length: " + srcLen + " Actual: " + dstLen);
            } else {
                if (preserveFileDate) {
                    destFile.setLastModified(srcFile.lastModified());
                }

            }
        }
    }

    //开启热点
    private void init() {

        //1.初始化热点
        WifiMgr.getInstance(getContext()).disableWifi();
        if (ApMgr.isApOn(getContext())) {
            ApMgr.disableAp(getContext());
        }

        mWifiAPBroadcastReceiver = new WifiAPBroadcastReceiver() {
            @Override
            public void onWifiApEnabled() {
                Log.i(TAG, "======>>>onWifiApEnabled !!!");
                if (!mIsInitialized) {
                    try {
                        //开启热点之后，启动httpSever
                        App.MAIN_EXECUTOR.execute(createServer());
                        mIsInitialized = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        mIsInitialized = false;
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter(WifiAPBroadcastReceiver.ACTION_WIFI_AP_STATE_CHANGED);
        registerReceiver(mWifiAPBroadcastReceiver, filter);

        //打开热点
        ApMgr.isApOn(getContext()); // check Ap state :boolean
        String ssid = (isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE);
        ApMgr.configApState(getContext(), ssid); // change Ap state :boolean
        tvHotspotName.setText(ssid);
    }

    public Runnable createServer() throws Exception {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    // 确保热点开启之后获取得到IP地址
                    String hotspotIpAddr = WifiMgr.getInstance(getContext()).getHotspotLocalIpAddress();
                    int count = 0;
                    while (hotspotIpAddr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(1000);
                        hotspotIpAddr = WifiMgr.getInstance(getContext()).getIpAddressFromHotspot();
                        count++;
                    }
                    count = 0;
                    while (!NetUtils.pingIpAddress(hotspotIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
                        Thread.sleep(500);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                mAndroidDownloadServer = new AndroidDownloadServer(Constant.DEFAULT_MICRO_SERVER_PORT, InviteByHotspotActivity.this);
                try {
                    mAndroidDownloadServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeServer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeServer();
    }

    /**
     * 返回按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeServer();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 关闭httpServer，关闭热点
     */
    private void closeServer() {
//        if (mAndroidMicroServer != null) {
//            mAndroidMicroServer.stop();
//        }
        ApMgr.disableAp(getContext());
        App.getAppContext().getFileInfoMap().clear();

    }

    /**
     * 生成QR图
     * @param text
     * @return
     */
    private Bitmap createImage(String text) {
        try {

            if (TextUtils.isEmpty(text)) {
                return null;
            }
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
