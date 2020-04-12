package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.base.BaseTransfer;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.AllUser;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.util.ListUtils;
import com.thinkd.xshare.util.LogUtils;
import com.thinkd.xshare.util.NetUtils;
import com.thinkd.xshare.util.WifiMgr;
import com.thinkd.xshare.widget.RadarScanView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;


/**
 * Created by 百思移动 on 2017/9/21.
 * @author CCAONG
 */
@RuntimePermissions
public class ScanWiFiActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_ts)
    TextView tvTs;

    @Bind(R.id.radarView)
    RadarScanView radarView;

    @Bind(R.id.iv_user1)
    ImageView ivUser1;
    @Bind(R.id.iv_user2)
    ImageView ivUser2;
    @Bind(R.id.iv_user3)
    ImageView ivUser3;
    @Bind(R.id.iv_user4)
    ImageView ivUser4;
    @Bind(R.id.iv_user5)
    ImageView ivUser5;
    @Bind(R.id.iv_user6)
    ImageView ivUser6;
    @Bind(R.id.iv_user7)
    ImageView ivUser7;
    @Bind(R.id.iv_user8)
    ImageView ivUser8;
    @Bind(R.id.iv_user9)
    ImageView ivUser9;
    @Bind(R.id.iv_user10)
    ImageView ivUser10;

    @Bind(R.id.tv_wifi_name_1)
    TextView tvWifiName1;
    @Bind(R.id.ll_1)
    LinearLayout ll1;
    @Bind(R.id.tv_wifi_name_2)
    TextView tvWifiName2;
    @Bind(R.id.ll_2)
    LinearLayout ll2;
    @Bind(R.id.tv_wifi_name_3)
    TextView tvWifiName3;
    @Bind(R.id.ll_3)
    LinearLayout ll3;
    @Bind(R.id.tv_wifi_name_4)
    TextView tvWifiName4;
    @Bind(R.id.ll_4)
    LinearLayout ll4;
    @Bind(R.id.tv_wifi_name_5)
    TextView tvWifiName5;
    @Bind(R.id.ll_5)
    LinearLayout ll5;
    @Bind(R.id.tv_wifi_name_6)
    TextView tvWifiName6;
    @Bind(R.id.ll_6)
    LinearLayout ll6;
    @Bind(R.id.tv_wifi_name_7)
    TextView tvWifiName7;
    @Bind(R.id.ll_7)
    LinearLayout ll7;
    @Bind(R.id.tv_wifi_name_8)
    TextView tvWifiName8;
    @Bind(R.id.ll_8)
    LinearLayout ll8;
    @Bind(R.id.tv_wifi_name_9)
    TextView tvWifiName9;
    @Bind(R.id.ll_9)
    LinearLayout ll9;
    @Bind(R.id.tv_wifi_name_10)
    TextView tvWifiName10;
    @Bind(R.id.ll_10)
    LinearLayout ll10;

    //消息：跳转到文件发送列表UI
    public static final int MSG_TO_FILE_SENDER_UI = 0X88;
    //消息：更新扫描可连接Wifi网络的列表
    public static final int MSG_TO_SHOW_SCAN_RESULT = 0X99;

    private static final String CONNECTION_TIME = "CONNECTION_TIME";

    public static final String MY_BORDCAST_SCANWIFI = "MY_BORDCAST_SCANWIFI";

    //设备的ssid
    String ssid = null;
    //拼接得出的设备的唯一的id
    String uniqueId = null;

    //附近的wifi列表
    List<ScanResult> mScanResultList = new ArrayList<>();

    Runnable mUdpServerRuannable;

    private WifiManager mWifiManager;

    //关闭的socket的子线程
    closeSocketRunnable closeSocketRunnable = new closeSocketRunnable();

    //当前的显示出来的头像的个数
    int iconNum = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //每隔一秒刷新一次附近的wifi列表
            if (msg.what == MSG_TO_SHOW_SCAN_RESULT) {
                getOrUpdateWifiScanResult();
                mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 1000);
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanwifi);
        ButterKnife.bind(this);

        //开始扫描的动画
        radarView.startScan();

        //toolbar相关
        setSupportActionBar(toolbar);
        //显示toolbar中的返回按键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //将toolbar中间的标题设为空
        getSupportActionBar().setTitle("");
        //将toolbar的标题设为空
        tvTitle.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //关闭socket
        //每次进入到扫描页面，先关闭socket，防止socket被占用
        new Thread(closeSocketRunnable).start();
        //更新扫描的画面
        updateUI();
//        Boolean gps = isOpenGps(this);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            //>6.0,需要先打开位置服务
//            if (gps) {
//                //手机的位置服务处于打开状态，开启扫描，开启wifi
//                init();
//            } else {
//                // 去设置中打开位置服务
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle(getResources().getString(R.string.prompt_title))
//                        .setMessage(getResources().getString(R.string.dialog_msg))
//                        .setNegativeButton(getResources().getString(R.string.prompt_no), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Toast.makeText(ScanWiFiActivity.this, getResources().getString(R.string.join_toast), Toast.LENGTH_SHORT).show();
//                                finish();
//                                return;
//                            }
//                        }).setPositiveButton(getResources().getString(R.string.prompt_yes), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //去系统的设置界面，打开gps
//                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        // 设置完成后返回到原来的界面  
//                        startActivityForResult(intent, 0);
//                    }
//                }).show();
//            }
//        } else {
//            //版本<6.0，直接开启扫描和wifi
//            init();
//        }
    }

    /**
     * 判断手机GPS是否打开
     *
     * @param context
     * @return
     */
    public boolean isOpenGps(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return gps;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //进入页面时，先清空扫描的wifi的集合
        mScanResultList.clear();
        //将所有的头像都隐藏
        initView();
    }

    /**
     * 隐藏所有预埋的头像
     */
    private void initView() {
        ll1.setVisibility(View.INVISIBLE);
        ll2.setVisibility(View.INVISIBLE);
        ll3.setVisibility(View.INVISIBLE);
        ll4.setVisibility(View.INVISIBLE);
        ll5.setVisibility(View.INVISIBLE);
        ll6.setVisibility(View.INVISIBLE);
        ll7.setVisibility(View.INVISIBLE);
        ll8.setVisibility(View.INVISIBLE);
        ll9.setVisibility(View.INVISIBLE);
        ll10.setVisibility(View.INVISIBLE);
        //将所有的头像隐藏后，将显示头像的个数置位0
        iconNum = 0;
    }

    private void init() {

        if (!WifiMgr.getInstance(getContext()).isWifiEnable()) {
            //wifi未打开的情况
            WifiMgr.getInstance(getContext()).openWifi();
        }
        //Android 6.0 扫描wifi 需要开启定位
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 获取wifi连接需要定位权限,没有获取权限
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_WIFI_STATE,
                }, REQUEST_CODE_OPEN_GPS);
                return;
            }
        } else {
            //Android 6.0 以下的直接开启扫描
//            updateUI();
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_OPEN_GPS:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    updateUI();
//                } else {
////                    updateUI();
////                    Toast.makeText(this, getResources().getString(R.string.join_toast), Toast.LENGTH_SHORT).show();
////                    finish();
//                }
//                break;
//        }
//    }

    /**
     * 更新UI
     */
    private void updateUI() {
        getOrUpdateWifiScanResult();
        //2秒之后开始更新数据
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_TO_SHOW_SCAN_RESULT), 2000);
    }

    /**
     * 获取或者更新wifi扫描列表
     */
    private void getOrUpdateWifiScanResult() {
        WifiMgr.getInstance(getContext()).startScan();
        //获取当前所有扫描到的wifi
        mScanResultList = WifiMgr.getInstance(getContext()).getScanResultList();
        //获取没有密码的热点
        mScanResultList = ListUtils.filterWithNoPassword(mScanResultList);
        if (mScanResultList.size() == 0) {
            initView();
        }
        //当附近的wifi列表不为空且个数>显示出的头像个数时
        if (mScanResultList != null && iconNum < mScanResultList.size()) {
            try {
                //在数据库中根据wifi名查询用户
                List<AllUser> allUserList = DaoHelper.queryBySSId((mScanResultList.get(iconNum).SSID));
                //如果查询到的数据为空，这时候需要显示一个默认的头像
                if (allUserList.size() == 0) {
                    // 在随机的位置显示默认的头像和设备名
                    visibleAvatar(R.mipmap.avatar_1, mScanResultList.get(iconNum).SSID, mScanResultList.get(iconNum).SSID);
                    iconNum++;
                }
                for (int i1 = 0; i1 < allUserList.size(); i1++) {
                    AllUser user = allUserList.get(i1);
                    //在随机的位置显示指定的头像和用户名
                    visibleAvatar(user.getUserAvatar(), user.getUserSsid(), user.getUserName());
                    iconNum++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (iconNum > mScanResultList.size()) {
            //如果显示的头像个数大于扫描的wifi列表的中的数据个数，隐藏所有的头像，重新显示
            initView();
        }
    }


    /**
     * 在随机的位置显示头像
     * @param id
     * @param ssid
     * @param userNmae
     */
    private void visibleAvatar(int id, final String ssid, final String userNmae) {
        int ivId = (int) (Math.random() * 10);

        switch (ivId) {
            case 1:
                ll1.setVisibility(View.VISIBLE);
                ivUser1.setImageResource(id);
                tvWifiName1.setText(userNmae);
                ivUser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);

                    }
                });

                break;
            case 2:
                ll2.setVisibility(View.VISIBLE);
                ivUser2.setImageResource(id);
                tvWifiName2.setText(userNmae);
                ivUser2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });

                break;
            case 3:
                ll3.setVisibility(View.VISIBLE);
                ivUser3.setImageResource(id);
                tvWifiName3.setText(userNmae);
                ivUser3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });

                break;
            case 4:
                ll4.setVisibility(View.VISIBLE);
                ivUser4.setImageResource(id);
                tvWifiName4.setText(userNmae);
                ivUser4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });

                break;
            case 5:
                ll5.setVisibility(View.VISIBLE);
                ivUser5.setImageResource(id);
                tvWifiName5.setText(userNmae);
                ivUser5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });

                break;
            case 6:
                ll6.setVisibility(View.VISIBLE);
                ivUser6.setImageResource(id);
                tvWifiName6.setText(userNmae);
                ivUser6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);

                    }
                });

                break;
            case 7:
                ll7.setVisibility(View.VISIBLE);
                ivUser7.setImageResource(id);
                tvWifiName7.setText(userNmae);
                ivUser7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });


                break;
            case 8:
                ll8.setVisibility(View.VISIBLE);
                ivUser8.setImageResource(id);
                tvWifiName8.setText(userNmae);
                ivUser8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        connection(ssid, userNmae);
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);

                    }
                });

                break;
            case 9:
                ll9.setVisibility(View.VISIBLE);
                ivUser9.setImageResource(id);
                tvWifiName9.setText(userNmae);
                ivUser9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);

                    }
                });

                break;
            case 0:
                ll10.setVisibility(View.VISIBLE);
                ivUser10.setImageResource(id);
                tvWifiName10.setText(userNmae);
                ivUser10.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });
                break;
            default:
                ll1.setVisibility(View.VISIBLE);
                ivUser1.setImageResource(id);
                tvWifiName1.setText(userNmae);
                ivUser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ScanWiFiActivityPermissionsDispatcher.connectionWithCheck(ScanWiFiActivity.this,ssid,userNmae);
//                        connection(ssid, userNmae);
                    }
                });
                break;
        }
    }

    /**
     * 加入指定的wifi
     * @param ssid
     * @param name
     *
     * 申请加入群组的权限
     */
    @NeedsPermission({Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void connection(String ssid, String name) {
        //刷新附近的wifi列表
        mHandler.removeMessages(MSG_TO_SHOW_SCAN_RESULT);
        //打开wifi
        WifiMgr.getInstance(getContext()).openWifi();

        Intent intent = new Intent(ScanWiFiActivity.this, JoiningGroupActivity.class);
        //获取当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String connectionTime = formatter.format(curDate);
        intent.putExtra(CONNECTION_TIME,connectionTime);
        intent.putExtra("name", name);

        //进入动画界面
        startActivity(intent);

        //加入到指定的wifi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //系统版本大于6.0，需要额外的方法加入到指定的wifi
            wifiConnect(ssid);
        } else {
            //直接加入指定的wifi
            WifiMgr.getInstance(getContext())
                    .addNetwork(WifiMgr.createWifiCfg(ssid, null, WifiMgr.WIFICIPHER_NOPASS));
        }
        //在线程池中开启创建发送消息到创建方的服务

        Log.e("","step1");
        mUdpServerRuannable = createSendMsgToServerRunnable(WifiMgr.getInstance(getContext()).getIpAddressFromHotspot());
        LogUtils.e("ScanWiFiAc", "step1");
        App.MAIN_EXECUTOR.execute(mUdpServerRuannable);
    }

    /**
     * android 6.0连接到指定的wifi
     * @param ssid
     */
    private void wifiConnect(String ssid) {
        // 连接到外网
        WifiConfiguration mWifiConfiguration;
        mWifiManager = (WifiManager) ScanWiFiActivity
                .getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String SSID = ssid;

        //检测指定SSID的WifiConfiguration 是否存在
        WifiConfiguration tempConfig = isExsits(SSID);
        if (tempConfig == null) {
            //创建一个新的WifiConfiguration ，CreateWifiInfo()需要自己实现
            mWifiConfiguration = WifiMgr.createWifiCfg(SSID, null, WifiMgr.WIFICIPHER_NOPASS);
            int wcgID = mWifiManager.addNetwork(mWifiConfiguration);
            boolean b = mWifiManager.enableNetwork(wcgID, true);
        } else {
            //发现指定WiFi，并且这个WiFi以前连接成功过
            mWifiConfiguration = tempConfig;
            boolean b = mWifiManager.enableNetwork(mWifiConfiguration.networkId, true);
        }
    }

    /**
     * 判断曾经连接过得WiFi中是否存在指定SSID的WifiConfiguration
     *
     * @param SSID
     * @return WifiConfiguration
     */
    public WifiConfiguration isExsits(String SSID) {

        List<WifiConfiguration> existingConfigs = mWifiManager
                .getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * TODO 注解在用于向用户解释为什么需要这个权限，只有当用户第一次请求被用户拒绝时，下次请求之前会调用
     * @param request
     */
    @OnShowRationale({Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void show(PermissionRequest request){
        showRationaleDialog((R.string.permission_creategroup), request);
    }

    /**
     * TODO 当用户拒绝了权限请求时，调用
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onGroupDenied() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * TODO 再当用户选中了不在询问复选框后，并拒绝了权限请求时的方法，
     * 可以向用户解释为何申请这个权限
     */
    @OnNeverAskAgain({Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onGroupNeverAskAgain() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(getResources().getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.refuse), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    /**
     * 第三步
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ScanWiFiActivityPermissionsDispatcher.onRequestPermissionsResult(this,requestCode,grantResults);
    }

    /**
     * 创建发送UDP消息到 创建group 的服务线程
     * @param serverIP
     */
    private Runnable createSendMsgToServerRunnable(final String serverIP) {

        return new Runnable() {
            @Override
            public void run() {
                try {
                    closeSocket();
                    LogUtils.e("ScanWiFiAc", "step2");
                    startFileSenderServer(serverIP, Constant.DEFAULT_SERVER_COM_PORT);
                } catch (Exception e) {
                    LogUtils.e("ScanWiFiAc", "出现异常"+e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 开启 文件发送方 通信服务 (必须在子线程执行)
     *
     * @param targetIpAddr
     * @param serverPort
     * @throws Exception
     */
    DatagramSocket mDatagramSocket;

    private void startFileSenderServer(String targetIpAddr, int serverPort) throws Exception {
        // 确保Wifi连接上之后获取得到IP地址
        LogUtils.e("ScanWiFiAc", "进入startFileSenderServer");
        int count = 0;
        while (targetIpAddr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(1000);
            targetIpAddr = WifiMgr.getInstance(getContext()).getIpAddressFromHotspot();
            count++;
        }

        // 即使获取到连接的热点wifi的IP地址也是无法连接网络
        count = 0;
        while (!NetUtils.pingIpAddress(targetIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(500);
            count++;
        }
        /*
        TODO java.net.BindException: bind failed: EADDRINUSE (Address already in use)
        because 传输界面返回没有正常结束或者关闭
         */
        LogUtils.e("ScanWiFiAc", "new DatagramSocket");
        mDatagramSocket = new DatagramSocket(serverPort);
        LogUtils.e("ScanWiFiAc", "new DatagramSocket成功");
        byte[] receiveData = new byte[1024];
        byte[] sendData = null;
        InetAddress ipAddress = InetAddress.getByName(targetIpAddr);
        /**
         * 把User就是自己的信息发过去
         * 在下面paresIpList中接收对面的User
         */

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid + DEVICE_ID.substring(10, 14);

        String myip = WifiMgr.getInstance(getContext()).getCurrentIpAddress();
        //在数据库中查找到自己的所有信息
        User user = DaoHelper.getUserBySsidd(uniqueId);
        //将自己的ip也set到user对象中
        user.setIpAddress(myip);
        //把自己的信息数据保存到数据库中
        DaoHelper.update(user);
        //将user对象装换成String类型
        String userJson = User.toJsonStr(user);
        LogUtils.e("ScanWiFiAc", "userJson>>>" + userJson);
        //1.21

        //将信息发送给服务端
        sendData = (Constant.MSG_CREATE_GROUP_INIT + userJson).getBytes(BaseTransfer.UTF_8);
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, ipAddress, serverPort);
        mDatagramSocket.send(sendPacket);
        LogUtils.e("ScanWiFiAc", "step3");


        boolean flag = true;
        //2.接收服务端的反馈
        while (flag) {
            LogUtils.e("ScanWiFiAc", "等待接收消息");
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), BaseTransfer.UTF_8).trim();
            LogUtils.e("ScanWiFiAc", "收到的消息"+response);
            if (response != null && response.startsWith(Constant.MSG_JOIN_GROUP_NIT)) {

                LogUtils.e("ScanWiFiAc", "接收到正确的消息");
                //接收字符串，转换字符串，转换成ip，添加到列表中
                parseIplist(response);

                //接收到服务端的反馈，发送广播，通知客户端进入到shareActivity中
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //发送广播，通知进入文件分享界面
                        Intent i = new Intent(MY_BORDCAST_SCANWIFI);
                        sendBroadcast(i);
                        LogUtils.e("ScanWiFiAc", "发送广播，进入下一个界面");
                    }
                });

                //关闭socket
                new Thread(closeSocketRunnable).start();
                flag = false;
                //结束当前页
                finish();
            }
        }
    }

    /**
     * 解析ipList
     *
     * @throws UnknownHostException
     */
    private void parseIplist(String s) throws UnknownHostException {
        String siplist = s.substring(18);
        String[] strs = siplist.split("_,_");
//        List<String> list = new ArrayList<String>();
        LogUtils.e("ScanParseIplist", strs.length + "");
        for (int i = 0; i < strs.length; i++) {
            LogUtils.e("ScanParseIplist", strs[i]);
            App.addOutReach(OutReach.toObject(strs[i]));
        }
        List<OutReach> reaches = App.getOutReaches();
        //保存服务端的信息到数据库
        for (int i = 0; i < reaches.size(); i++) {
            OutReach reach = reaches.get(i);
            if ("192.168.43.1".equals(reach.getIpAddress())) {
                List<AllUser> allUserList = DaoHelper.queryBySSId(reach.getSsId());
                if (allUserList.size() == 0) {
                    //新增一条数据
                    try {
                        AllUser allUser = new AllUser(null, reach.getSsId(), reach.getName(), reach.getAvatarId());
                        long end = DaoHelper.inserts(allUser);
                        if (end > 0) {
                            Log.e("数据库添加", "成功");
                        } else {
                            Log.e("数据库添加", "失败");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    //更新数据
                    DaoHelper.updateAllUserInfo(reach.getSsId(), reach.getName(), reach.getAvatarId());
                }
            }
        }

//        for (int i = 0; i < strs.length; i++) {
//            list.add(strs[i]);
//        }
//        for (String i : list) {
//            ipList.add(InetAddress.getByName(i.substring(1)));
////            App.addOutReach(i.substring(1));
//        }
    }

    //在子线程中关闭socket
    class closeSocketRunnable implements Runnable{
        @Override
        public void run() {
            closeSocket();
        }
    }

    /**
     * 关闭UDP Socket 流
     */
    private void closeSocket() {
        if (mDatagramSocket != null) {
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
            Log.e("mDatagramSocket","mDatagramSocket已经被关闭");
        }
    }

    //点击toolbar中的返回按钮，finish当前页面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
