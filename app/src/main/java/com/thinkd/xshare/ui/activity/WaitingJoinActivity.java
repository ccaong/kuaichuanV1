package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.base.BaseTransfer;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.AllUser;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.receiver.WifiAPBroadcastReceiver;
import com.thinkd.xshare.util.ApMgr;
import com.thinkd.xshare.util.GprstrafficManager;
import com.thinkd.xshare.util.LogUtils;
import com.thinkd.xshare.util.WifiMgr;
import com.thinkd.xshare.widget.WaveView;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by 百思移动 on 2017/9/22.
 */

public class WaitingJoinActivity extends BaseActivity {



    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_prompt)
    TextView tvPrompt;
    @Bind(R.id.wave)
    WaveView wave;
    @Bind(R.id.head)
    ImageView head;
    @Bind(R.id.tv_my_name)
    TextView tvMyName;
    @Bind(R.id.tv_device_name)
    TextView tvDeviceName;




    /**
     * 与 文件发送方 通信的 线程
     */
    Runnable mUdpServerRuannable;
    WifiAPBroadcastReceiver mWifiAPBroadcastReceiver;
    //在子线程中关闭socket
    closeSocketRunnable closeSocketRunnable = new closeSocketRunnable();

    private static final String CLIENT_NAME = "CLIENT_NAME";
    public static final int REQUEST_CODE_WRITE_SETTINGS = 7879;



    boolean mIsInitialized = false;

    private boolean mReceiverTag = false;
    String ssid =null;
    String uniqueId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver_waiting);
        ButterKnife.bind(this);

        //扩散的动画
        wave.start();
        wave.setImageRadius(100);

        //toolbar相关
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvTitle.setText("");

        //关闭wifi
        WifiMgr.getInstance(WaitingJoinActivity.this).closeWifi();

        //检查GPRS是否开启
//        initGprs();

        //初始化权限
//        initWithGetPermission(this);

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid+DEVICE_ID.substring(10,14);

        //查询自己的信息，然后设置名字和头像
        User user = selectUser(uniqueId);

        head.setImageResource(user.getUserAvatar());
        tvMyName.setText(user.getUserName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsInitialized = false;
        init();
        //初始化权限
//        initWithGetPermission(this);
    }


    private User selectUser(String ssid) {
        User user = new User();
        try {
            List<User> userList = DaoHelper.queryById(ssid);
            for (int i = 0; i < userList.size(); i++) {
                user = userList.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                finishThisActivity();
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //初始化权限
    public void initWithGetPermission(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            //开启热点界面并开启热点
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


    /**
     * 初始化界面
     * 开启热点
     */
    private void init() {
        WifiMgr.getInstance(WaitingJoinActivity.this).disableWifi();
        if (ApMgr.isApOn(WaitingJoinActivity.this)) {
            ApMgr.disableAp(WaitingJoinActivity.this);
        }
        mWifiAPBroadcastReceiver = new WifiAPBroadcastReceiver() {
            @Override
            public void onWifiApEnabled() {
                if (!mIsInitialized) {
                    LogUtils.e("WaitingJoinAc", "step1");
                    mUdpServerRuannable = createSendMsgToJoinRunnable();
                    App.MAIN_EXECUTOR.execute(mUdpServerRuannable);
                    mIsInitialized = true;
                }
            }
        };
        register();
        ApMgr.isApOn(WaitingJoinActivity.this);
        ApMgr.configApState(WaitingJoinActivity.this, uniqueId);
        //显示的热点名
        tvDeviceName.setText(getResources().getString(R.string.current) + uniqueId);
    }

    private void register() {
        if (!mReceiverTag) {
            mReceiverTag = true;
            //监听状态改变
            IntentFilter filter = new IntentFilter(WifiAPBroadcastReceiver.ACTION_WIFI_AP_STATE_CHANGED);
            registerReceiver(mWifiAPBroadcastReceiver, filter);
        }
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.equals("");
    }

    /**
     * 创建发送UDP消息到 等待加入方 的服务线程
     */
    private Runnable createSendMsgToJoinRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    LogUtils.e("WaitingJoinAc", "step2");
                    startWaitingJoinServer(Constant.DEFAULT_SERVER_COM_PORT);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("WaitingJoinAc","出现异常："+e.toString());
                }
            }
        };
    }

    /**
     * 开启 文件接收方 通信服务 (必须在子线程执行)
     * @param serverPort
     * @throws Exception
     */
    DatagramSocket mDatagramSocket;

    private void startWaitingJoinServer(int serverPort) throws Exception {
        LogUtils.e("WaitingJoinAc", "step3");

        //网络连接上，无法获取IP的问题
        int count = 0;
        String localAddress = WifiMgr.getInstance(getContext()).getHotspotLocalIpAddress();
        while (localAddress.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(1000);
            localAddress = WifiMgr.getInstance(getContext()).getHotspotLocalIpAddress();
            count++;
        }

        mDatagramSocket = new DatagramSocket(serverPort);
        byte[] receiveData = new byte[1024];
        byte[] sendData = null;
        while (true) {
            LogUtils.e("WaitingJoinAc", "等待接收消息");
            //1.接收 文件发送方的消息
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String msg = new String(receivePacket.getData()).trim();

            //获取客户端的ip
            InetAddress inetAddress = receivePacket.getAddress();
            //获取客户端的端口号
            int port = receivePacket.getPort();
            LogUtils.e("WaitingJoinAc", "收到的消息>>>" + msg);

            //判断接收到的消息
            if (msg != null && msg.startsWith(Constant.MSG_CREATE_GROUP_INIT)) {
                LogUtils.e("WaitingJoinAc", "接收到正确的消息");

                /**
                 * （可以向所有的客户端发送消息，客户端在跳转后socket也不能关闭，否则无法接收到信息）
                 *  循环向所有已知Ip客户端发送OutReach信息
                 */
                //添加一个（客户端的信息）
                String userJson = msg.substring(21);
                LogUtils.e("WaitingAc", "userJson>>>" + userJson);
                User user = User.toObject(userJson);
                OutReach outReach = new OutReach(user.getUserSsid(), user.getUserAvatar(), user.getUserName(), user.getIpAddress());
                App.addOutReach(outReach);
                //所有已知（将所有已知的outreach拼接成字符串）
                String strReaches = "";
                List<OutReach> reaches = App.getOutReaches();
                for (OutReach reach : reaches) {
                    strReaches += OutReach.toJsonStr(reach) + "_,_";
                }
                //包含自身
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId();
                //获取手机的设备id
                ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
                //将设备id和手机的唯一编码拼接成一个唯一的id
                uniqueId = ssid+DEVICE_ID.substring(10,14);
                //查询到自己（服务端）的详细信息
                User userBySsidd = DaoHelper.getUserBySsidd(uniqueId);
                userBySsidd.setIpAddress(Constant.DEFAULT_SERVER_IP);
                //在数据库中更新自己的信息
                DaoHelper.update(userBySsidd);
                OutReach current = new OutReach(userBySsidd.getUserSsid(), userBySsidd.getUserAvatar(), userBySsidd.getUserName(), userBySsidd.getIpAddress());
                LogUtils.e("WaitingAc", "current>>>" + current.toString());
                //将自己的信息和所有已知的信息拼接成一个字符串
                strReaches += OutReach.toJsonStr(current) + "_,_";
                LogUtils.e("WaitingAc", "strReaches>>>" + strReaches);

                /**
                 * 把所有已知OutReach包括自己返回
                 */
                LogUtils.e("WaitingJoinAc", "step5");
                //reaches是所有的客户端的信息的集合
                for (int i = 0; i < reaches.size(); i++) {
                    OutReach reach = reaches.get(i);
                    if (!"192.168.43.1".equals(reach.getIpAddress())) {
                        InetAddress address = null;
                        if ("default".equals(reach.getIpAddress())) {
                            //刚刚连接的客户端的ip
                            address = inetAddress;
                        } else {
                            address = InetAddress.getByName(reach.getIpAddress());
                        }
                        LogUtils.e("WaitingJoinAc", "准备发送消息");

                        //将所有的客户端和服务端的信息发送给所有的客户端
                        sendData = (Constant.MSG_JOIN_GROUP_NIT + strReaches).getBytes(BaseTransfer.UTF_8);
                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, port);
                        //这句话执行完毕后    加入方跳转
                        mDatagramSocket.send(sendPacket);
                        LogUtils.e("WaitingJoinAc", "消息发送完毕");
                    }
                }
                for (int i = 0; i < reaches.size(); i++) {
                    OutReach reach = reaches.get(i);
                    if (!"192.168.43.1".equals(reach.getIpAddress())) {
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

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mWifiAPBroadcastReceiver != null) {
//                            unregisterReceiver(mWifiAPBroadcastReceiver);
//                        }
//                    }
//                });

                String clientName = "";
                for (OutReach reach : reaches) {
                    if (!reach.getIpAddress().equals(Constant.DEFAULT_SERVER_IP)) {
                        clientName = reach.getName();
                    }
                }
                //接收到消息，跳转到下一个界面
                Intent intent = new Intent(WaitingJoinActivity.this, ShareFileActivity.class);
                intent.putExtra(CLIENT_NAME, clientName);
                //接收到消息，跳转到下一个界面
                startActivity(intent);

                //关闭socket，不允许多人连接
                finishThisActivity();
//                new Thread(closeSocketRunnable).start();
                finish();

            }
        }
    }

    //将ipList中的数据转换成一个字符串
    StringBuffer sb = new StringBuffer();

    private String getIpList() {

        List<OutReach> reaches = App.getOutReaches();
        for (OutReach outReach : reaches) {
            sb.append(outReach.getIpAddress() + ",");
        }
        return sb.toString();
    }

    private void initGprs() {
        boolean mobileDataState = GprstrafficManager.getMobileDataState(this);
        String text = "";
        if (SDK_INT <= 19) {
            text = "关闭";
        } else {
            text = "去设置中关闭";
        }
        if (mobileDataState) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setMessage("您的移动流量在打开的情况下，对方可以使用您的移动流量上网，建议关闭移动网络")
                    .setNegativeButton("我不在乎", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            return;
                        }
                    }).setPositiveButton(text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (SDK_INT <= 19) {
                        GprstrafficManager.setMobileData(WaitingJoinActivity.this, false);
                    } else {
                        startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));

                    }
                }
            }).show();
        }
    }



    //在子线程中关闭socket
    class closeSocketRunnable implements Runnable{
        @Override
        public void run() {
            closeSocket();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishThisActivity();
//        new Thread(closeSocketRunnable).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishThisActivity();
//        unRegister();
    }

    /**
     * 关闭UDP Socket 流
     */
    private void closeSocket() {
        if (mDatagramSocket != null) {
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }

    //解除注册的广播
    private void unRegister() {
        try{
            if (mReceiverTag) {
                mReceiverTag = false;
                if(mWifiAPBroadcastReceiver != null){
                    this.unregisterReceiver(mWifiAPBroadcastReceiver);
                }
            }
        }catch (Exception e){
         e.printStackTrace();
        }

    }

    //退出当前的Activity
    private void finishThisActivity(){
        unRegister();
        new Thread(closeSocketRunnable).start();
    }

}
