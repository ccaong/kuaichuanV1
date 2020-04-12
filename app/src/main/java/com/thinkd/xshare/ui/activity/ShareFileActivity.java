package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.ShareReceiverAdapter;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.base.BaseTransfer;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.common.FileReceiver;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.EachFile;
import com.thinkd.xshare.dao.HeaderDesc;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.entity.IpPortInfo;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.receiver.WifiAPBroadcastReceiver;
import com.thinkd.xshare.util.ApMgr;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.LogUtils;
import com.thinkd.xshare.util.NetUtils;
import com.thinkd.xshare.util.WifiMgr;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by 百思移动 on 2017/9/22.
 */
@RuntimePermissions
public class ShareFileActivity extends BaseActivity {

    private static final String CLIENT_NAME = "CLIENT_NAME";
    private static final String SERVER_NAME = "SERVER_NAME";

    private static final int MSG_QUIT_GROUP = 1029;
    private static final int RECEIVE_QUIT_MSG = 1392;
    private static final int RECEIVE_FLAG = 1253;
    private static final int HIDE_TOAST = 2512;
    Runnable mUdpServerRuannable;
    boolean mIsInitialized = false;


    public List<String> ssidList = new ArrayList<String>();
    @Bind(R.id.tv_unit_has_send)
    TextView tvUnitHasSend;
    @Bind(R.id.tv_unit_has_time)
    TextView tvUnitHasTime;
    private String mSenderJson;
    private DatagramSocket mQuitMsgSocket;

    /**
     * 进度条 已传 耗时等UI组件
     */

    @Bind(R.id.pb_total)
    ProgressBar pb_total;
    @Bind(R.id.tv_value_storage)
    TextView tv_value_storage;
    @Bind(R.id.tv_unit_storage)
    TextView tv_unit_storage;
    @Bind(R.id.tv_value_time)
    TextView tv_value_time;
    @Bind(R.id.tv_unit_time)
    TextView tv_unit_time;
    @Bind(R.id.progress_view)
    FrameLayout mProgressView;
    @Bind(R.id.tv_toast)
    TextView mTvToast;
    @Bind(R.id.layout_toast)
    RelativeLayout mLayoutToast;

    @Bind(R.id.rv_sharecontent)
    RecyclerView mRecyclerView;

    ShareReceiverAdapter mAdapter;


    @Bind(R.id.bt_sendfile)
    Button bt_sendfile;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_title)
    TextView tvTitle;

    FileInfo mCurFileInfo;

    IpPortInfo mIpPortInfo;

    Runnable mReceiverServer;
    Thread mRecievierServerThread;


    long mTotalLen = 0;     //所有总文件的进度
    long mCurOffset = 0;    //每次传送的偏移量
    long mLastUpdateLen = 0; //每个文件传送onProgress() 之前的进度
    String[] mStorageArray = null;


    long mTotalTime = 0;
    long mCurTimeOffset = 0;
    long mLastUpdateTime = 0;
    String[] mTimeArray = null;

    int mHasSendedFileCount = 0;

    public static final int MSG_FILE_RECEIVER_INIT_SUCCESS = 0X4444;
    public static final int MSG_ADD_FILE_INFO = 0X5555;
    public static final int MSG_UPDATE_FILE_INFO = 0X6666;

    public static final int MSG_FILE_RECEIVER_UPDATE_UI = 11111;
    public static final int MSG_FILE_SEND_UPDATE_UI = 22222;
    private String connectionTime;

    public static final int MSG_TO_FILE_RECEIVER_UI = 0X88;

    private static final String CONNECTION_TIME = "CONNECTION_TIME";

    WifiAPBroadcastReceiver mWifiAPBroadcastReceiver;

    closeSocketRunnable closeSocketRunnable = new closeSocketRunnable();

    boolean isOnline = true;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_FILE_RECEIVER_INIT_SUCCESS) {
                sendMsgToFileSender(mIpPortInfo);
            } else if (msg.what == MSG_ADD_FILE_INFO) {
                //收到一个文件
                FileInfo fileInfo = (FileInfo) msg.obj;


            } else if (msg.what == MSG_UPDATE_FILE_INFO) {
                //ADD FileInfo 到 Adapter
                //更新进度
                updateTotalProgressView();
                if (mAdapter != null) mAdapter.update();
            } else if (msg.what == MSG_FILE_RECEIVER_UPDATE_UI) {
//                IpPortInfo ipPortInfo = (IpPortInfo) msg.obj;
                HashMap<String, IpPortInfo> map = (HashMap<String, IpPortInfo>) msg.obj;
                for (Map.Entry<String, IpPortInfo> entry : map.entrySet()) {
                    fileReceiverUi(entry.getKey(), entry.getValue());
                }


            } else if (msg.what == MSG_QUIT_GROUP) {
                //退出群组
                //断开当前的网络连接
                WifiMgr.getInstance(ShareFileActivity.this).disconnectCurrentNetwork();
                //关闭当前的热点
                ApMgr.disableAp(ShareFileActivity.this);
                Log.e("shareFileAC", "退出群组");

                //跳转到主页面
                Intent intent = new Intent(ShareFileActivity.this, FirstActivity.class);
                startActivity(intent);
                //关闭socket在子线程中close
                new Thread(closeSocketRunnable).start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        closeSocket();
//                    }
//                });
                //结束当前页面
                ShareFileActivity.this.finish();

            } else if (msg.what == RECEIVE_QUIT_MSG) {
                //收到对方退出的消息
                LogUtils.e("shareFileAC", "对方已断开连接");
                //clear List<OutReach>
                mLayoutToast.setVisibility(View.VISIBLE);
                mTvToast.setText(mServerName + mClientName + getString(R.string.offline));
                isOnline = false;
                bt_sendfile.setText("quit group");
//                bt_sendfile.setBackgroundColor(Color.GRAY);
//                bt_sendfile.setEnabled(false);
                //改变toolbar的颜色
//                toolbar.setBackgroundColor(Color.GRAY);
                //改变进度条的颜色
//                pb_total.setBackgroundColor(Color.GRAY);
//                App.clearOutReach();
            } else if (msg.what == RECEIVE_FLAG) {
                mProgressView.setVisibility(View.VISIBLE);
            } else if (msg.what == HIDE_TOAST) {
                mLayoutToast.setVisibility(View.GONE);
            }
        }
    };

    private String mClientName;
    private String mServerName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_file);
        ButterKnife.bind(this);

        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "传输页面");

        Intent intent = getIntent();
        //获取发送的时间
        connectionTime = intent.getStringExtra(CONNECTION_TIME);
        //当前时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String thisTime = formatter.format(curDate);
        try {
            //将当前时间转换成int型
            long iThisTime = Long.parseLong(thisTime);
            //将点击连接时的时间转换成int型
            long iConnectionTime = Long.parseLong(connectionTime);
            //计算连接用时
            long time = iThisTime - iConnectionTime;
            //当连接时跨越分钟时，减去40秒，确保连接时间准确
            if (time > 40) {
                time = time - 40;
            }
            Firebase.getInstance(getApplicationContext()).logEvent("传输页面", time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


        if (intent != null) {
            if (intent.getStringExtra(CLIENT_NAME) != null) {
                //服务端经由Waiting页面进入传输页面
                //客户端名称
                mClientName = intent.getStringExtra(CLIENT_NAME);
                mLayoutToast.setVisibility(View.VISIBLE);
                mTvToast.setText(mClientName + getString(R.string.online));

            } else if (intent.getStringExtra(SERVER_NAME) != null) {
                //客户端经由JoningAc(Rocket)页面进入传输页面
                //服务端名字
                mServerName = intent.getStringExtra(SERVER_NAME);
                mLayoutToast.setVisibility(View.VISIBLE);
                mTvToast.setText(mServerName + getString(R.string.online));
            }
        }


        //点击发送文件的按钮
        bt_sendfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOnline) {
                    //好友在线，点击进入选择文件界面
                    ShareFileActivityPermissionsDispatcher.gotoChooseAcWithCheck(ShareFileActivity.this);
                } else {
                    //当前没有好友在线，点击直接退出
                    quitGroup();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_SETTINGS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .WRITE_SETTINGS}, WaitingJoinActivity.REQUEST_CODE_WRITE_SETTINGS);
        } else {
            init();
        }

        mHandler.sendEmptyMessageDelayed(HIDE_TOAST, 5000);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.close);
        getSupportActionBar().setTitle("");

        initWithGetPermission(this);
        mIsInitialized = false;

    }


    /**
     * 检查进入文件选择页面所需权限。
     */
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void gotoChooseAc() {
        Intent intent = new Intent(ShareFileActivity.this, ChooseFileActivity.class);
        if (mClientName != null) {
            intent.putExtra("name", mClientName);
        } else {
            intent.putExtra("name", mServerName);
        }
        startActivity(intent);
//        pushActivity(ChooseFileActivity.class);
    }

    /**
     * 解释
     *
     * @param request
     */
    @OnShowRationale({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showRationaleForJoingGroup(final PermissionRequest request) {
        showRationaleDialog((R.string.permission_gotochoose), request);
    }

    /**
     * 当申请所需权限被拒绝
     */
    @OnPermissionDenied({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onJoinGroupDenied() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拒绝权限申请，不不在询问
     */
    @OnNeverAskAgain({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void onJoinGroupNeverAskAgain() {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // 判断是否正在传输文件，提示是否确认退出
                //当前若没有好友在线，直接退出群组
                quitGroup();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 退出群组
     */
    public void quitGroup() {

        //好友在线，提示是否退出，发送离线消息
        if (isOnline) {
            showDialog();
        } else {
            //直接退出group
            App.clearOutReach();
            App.clearReceiverFileInfoMap();

            new Thread(closeSocketRunnable).start();

            Intent intent = new Intent(ShareFileActivity.this, FirstActivity.class);
            startActivity(intent);
            //关闭热点
            ApMgr.disableAp(ShareFileActivity.this);
            finish();
        }

    }

    /**
     * 提示是否退出连接？
     */
    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder
                .setMessage(getResources().getString(R.string.str_dialog_msg))
                .setNegativeButton(getResources().getString(R.string.prompt_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                }).setPositiveButton(getResources().getString(R.string.prompt_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭socket，然后退出
                /**
                 * 发送退出消息，清空双方List<OutReach>
                 *     再创建一个UDP发送
                 */
                sendQuitMsg();
                Log.e("shareFileAC", "发送退出消息1");

            }
        }).show();
    }

    /**
     * 向对方设备发送离线信息
     */
    private void sendQuitMsg() {
        //need ip
        List<OutReach> reaches = App.getOutReaches();
        String ipAddress = "";
        for (OutReach reach : reaches) {
            LogUtils.e("SendQuitMsg", "所有Ip>>>" + reach.getIpAddress());
            String address = reach.getIpAddress();
            if (address != WifiMgr.getInstance(ShareFileActivity.this).getCurrentIpAddress()) {
                ipAddress = address;
            }
        }
        LogUtils.e("SendQuitMsg", "对方ip>>>" + ipAddress);
        Runnable quitMsgRunnable = createSendQuitMsgRunnable(ipAddress);
        App.MAIN_EXECUTOR.execute(quitMsgRunnable);
    }

    private Runnable createSendQuitMsgRunnable(final String address) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Log.e("shareFileAC", "发送退出消息第一步1");
                    startQuitMsgSend(address, Constant.DEFAULT_SERVER_COM_PORT_2);
                } catch (Exception e) {
                    Log.e("shareFileAC", "发送退出消息出现异常" + e.toString());
                    //在子线程中关闭socket
                    new Thread(closeSocketRunnable).start();
                    Intent intent = new Intent(ShareFileActivity.this, FirstActivity.class);
                    startActivity(intent);
                    ApMgr.disableAp(ShareFileActivity.this);
                    finish();
                    e.printStackTrace();
                }
            }
        };
    }

    private void startQuitMsgSend(String address, int port) throws Exception {
        if (mQuitMsgSocket == null) {
            mQuitMsgSocket = new DatagramSocket(null);
            mQuitMsgSocket.setReuseAddress(true);//启用，关闭Socket时，立即释放socket绑定端口，默认为false
            mQuitMsgSocket.bind(new InetSocketAddress(0));//0代表分配一个没有被占用的端口
        }
        byte[] sendData = null;
        //获取目标设备
        InetAddress ipInetAddress = InetAddress.getByName(address);
        sendData = (Constant.MSG_QUIT_GROUP + "").getBytes(BaseTransfer.UTF_8);
        LogUtils.e("quitmsgSend>>>>>", Constant.MSG_QUIT_GROUP + "");
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, ipInetAddress, port);
        mQuitMsgSocket.send(packet);
        Log.e("shareFileAC", "退出消息已发送2");
        App.clearOutReach();
        App.clearReceiverFileInfoMap();
        Log.e("shareFileAC", "清空列表3");
        mHandler.sendEmptyMessage(MSG_QUIT_GROUP);
        Log.e("shareFileAC", "发送hanlder4");
    }

    /**
     * 初始化并且获取权限
     *
     * @param context
     */
    public void initWithGetPermission(Activity context) {
        boolean permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission = Settings.System.canWrite(context);
        } else {
            //检查权限
            permission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
        }
        if (permission) {
            //do your code
            init();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //权限，对手机设置进行修改
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, WaitingJoinActivity.REQUEST_CODE_WRITE_SETTINGS);
            } else {

                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_SETTINGS}, WaitingJoinActivity.REQUEST_CODE_WRITE_SETTINGS);
            }
        }
    }

    private void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mIsInitialized) {
            mUdpServerRuannable = createSendMsgToFileSenderRunnable();
            App.MAIN_EXECUTOR.execute(mUdpServerRuannable);
            mIsInitialized = true;
        }


        /**
         * 打开  在下方runnable可以收到quit_msg; 不打开   都无法收到 ;
         */
        Log.e("shareFileAC", "接收退出消息1");
        Runnable quitMsgReceive = createQuitMsgReceiveRunnable();
        App.MAIN_EXECUTOR.execute(quitMsgReceive);

    }

    private Runnable createQuitMsgReceiveRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {

                    Log.e("shareFileAC", "接收退出消息2");
                    startReceiveQuitMsg(Constant.DEFAULT_SERVER_COM_PORT_2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("shareFileAC", "接收退出消息出现异常" + e.toString());
                }
            }
        };
    }

    /**
     * 接收QuitMsg
     *
     * @param port
     * @throws Exception
     */
    private void startReceiveQuitMsg(int port) throws Exception {
        LogUtils.e("shareFileAC", "startReceiveQuitMsg run....");
        if (mQuitMsgSocket == null || mQuitMsgSocket.isBound()) {
            mQuitMsgSocket = new DatagramSocket(null);
            mQuitMsgSocket.setReuseAddress(true);
            mQuitMsgSocket.bind(new InetSocketAddress(port));
        }
        while (true) {
            LogUtils.e("shareFileAC", "准备接收退出消息");
            byte[] receiveData = new byte[Constant.SPEED_BYTE];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            LogUtils.e("shareFileAC", "newDatagramPacket");
            if (mQuitMsgSocket == null) {
                mQuitMsgSocket = new DatagramSocket(null);
                mQuitMsgSocket.setReuseAddress(true);
                mQuitMsgSocket.bind(new InetSocketAddress(port));
                LogUtils.e("shareFileAC", "测试mQuitMsgSocket是否被创建");
            }
            LogUtils.e("shareFileAC", "socketReceiver");
            mQuitMsgSocket.receive(receivePacket);
            LogUtils.e("shareFileAC", "测试是否执行");
            String msg = new String(receivePacket.getData()).trim();
            if (msg != null && msg.startsWith(Constant.MSG_QUIT_GROUP)) {
                LogUtils.e("shareFileAC", "退出消息正确");
                mHandler.sendEmptyMessage(RECEIVE_QUIT_MSG);
            }
        }
    }

    /**
     * 成功进入 文件接收列表UI 调用的finishNormal()
     */
    private void finishNormal() {
        if (mWifiAPBroadcastReceiver != null) {
            unregisterReceiver(mWifiAPBroadcastReceiver);
            mWifiAPBroadcastReceiver = null;
        }
        closeSocket();
        this.finish();
    }

    /**
     * 创建发送UDP消息到 文件发送方 的服务线程
     */
    private Runnable createSendMsgToFileSenderRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
//                    closeSocket();
                    startFileReceiverServer(Constant.DEFAULT_SERVER_COM_PORT_1);
                } catch (Exception e) {
                    System.out.println(e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * 开启 文件接收方 通信服务 (必须在子线程执行)
     *
     * @param serverPort
     * @throws Exception
     */

    private void startFileReceiverServer(int serverPort) throws Exception {

        int count = 0;
        String localAddress = WifiMgr.getInstance(getContext()).getHotspotLocalIpAddress();
        while (localAddress.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(1000);
            localAddress = WifiMgr.getInstance(getContext()).getHotspotLocalIpAddress();
            count++;

        }

//        mDatagramSocket = new DatagramSocket(serverPort);

        if (mDatagramSocket == null || !mDatagramSocket.isBound()) {
            mDatagramSocket = new DatagramSocket(null);
            mDatagramSocket.setReuseAddress(true);
            mDatagramSocket.bind(new InetSocketAddress(serverPort));
        }

        byte[] sendData = null;
        while (true) {
            byte[] receiveData = new byte[Constant.SPEED_BYTE];
            //1.接收 文件发送方的消息
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            if (mDatagramSocket == null) {
                mDatagramSocket = new DatagramSocket(null);
                mDatagramSocket.setReuseAddress(true);
                mDatagramSocket.bind(new InetSocketAddress(serverPort));
            }

            mDatagramSocket.receive(receivePacket);
            String msg = new String(receivePacket.getData()).trim();
            //获得地址
            InetAddress inetAddress = receivePacket.getAddress();
            //获得端口号
            int port = receivePacket.getPort();

            //接受数据和发送方的ssid
            if (msg != null && msg.startsWith(Constant.MSG_FILE_RECEIVER_INIT)) {
                /**
                 * 接收到数据时在显示ProgressBar
                 */
                mHandler.sendEmptyMessage(RECEIVE_FLAG);

                // 进入文件接收列表界面 (文件接收列表界面需要 通知 文件发送方发送 文件开始传输UDP通知)
                IpPortInfo ipPortInfo = new IpPortInfo(inetAddress, port);

                //msgId
                String msgId = msg.substring(22, 58);
                LogUtils.e("ShareFileAc接收端", "MsgId>>>" + msgId);
                mSenderJson = msg.substring(22 + 36);//长度应该 + MsgId之后

                /**
                 * 启动TCP通信  完成文件的传输
                 */
                Message message = Message.obtain(mHandler);
                message.what = MSG_FILE_RECEIVER_UPDATE_UI;
                HashMap<String, IpPortInfo> hashMap = new HashMap<>();
                hashMap.put(msgId, ipPortInfo);
                message.obj = hashMap;
                mHandler.sendMessage(message);
//                mHandler.obtainMessage(MSG_FILE_RECEIVER_UPDATE_UI, ipPortInfo).sendToTarget();
//                fileReceiverUi(ipPortInfo);

            } else if (msg != null && msg.startsWith(Constant.MSG_QUIT_GROUP)) {
                LogUtils.e("quitMsgReceive", "receiveHere........");
//                App.clearOutReach();
//                App.clearReceiverFileInfoMap();
            } else { //接收发送方的 文件列表
                if (msg != null) {
                    // 解析文件的列表 数据
                    parseFileInfo(msg);
                }
            }
        }
    }

    /**
     * 解析FileInfo
     *
     * @param msg
     */
    private void parseFileInfo(String msg) {
        FileInfo fileInfo = FileInfo.toObject(msg);
        if (fileInfo != null && fileInfo.getFilePath() != null) {
            //解析完毕添加到全局成员变量中
            App.getAppContext().addReceiverFileInfo(fileInfo);
        }
    }

    public List<FileInfo> mThisFile = new ArrayList<>();

    private void initDatas(FileInfo fileInfo) {

//        mThisFile  = new ArrayList<>();
        if (!mThisFile.contains(fileInfo)) {
            mThisFile.add(fileInfo);
        }
    }


    /**
     * 在子线程中关闭socket
     */
    class closeSocketRunnable implements Runnable {
        @Override
        public void run() {
            closeSocket();
        }
    }

    /**
     * 创建发送UDP消息,分发ip
     *
     * @param serverIP
     */
//    private Runnable createSendMsgToServerRunnable(final String serverIP) {
//
//        return new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    startipSenderServer(serverIP, Constant.DEFAULT_SERVER_COM_PORT_1);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }

    /**
     * 开启 文件发送方 通信服务 (必须在子线程执行)
     *
     * @param targetIpAddr
     * @param serverPort
     * @throws Exception
     */
    DatagramSocket mDatagramSocket;

//    private void startipSenderServer(String targetIpAddr, int serverPort) throws Exception {
//
//        int count = 0;
//        while (targetIpAddr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
//            Thread.sleep(1000);
//            targetIpAddr = WifiMgr.getInstance(getContext()).getIpAddressFromHotspot();
//            count++;
//        }
//
//        count = 0;
//        while (!NetUtils.pingIpAddress(targetIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
//            Thread.sleep(500);
//            count++;
//        }
//
//        if (mDatagramSocket == null) {
//            mDatagramSocket = new DatagramSocket(null);
//            //启用SO_REUSEADDR 套接字选项。
//            mDatagramSocket.setReuseAddress(true);
//            //绑定固定的端口号
//            mDatagramSocket.bind(new InetSocketAddress(serverPort));
//        }
//
//        byte[] sendData = null;
//        InetAddress ipAddress = InetAddress.getByName(targetIpAddr);
//
//
//        sendData = ("IPLIST" + getIpList()).getBytes(BaseTransfer.UTF_8);
//        DatagramPacket sendPacket =
//                new DatagramPacket(sendData, sendData.length, ipAddress, serverPort);
//        mDatagramSocket.send(sendPacket);
//    }

    //将ipList中的数据转换成一个字符串
//    StringBuffer sb = new StringBuffer();

//    private String getIpList() throws UnsupportedEncodingException {
//        if (ipList.size() != 0) {
//            //通过socket发送这个集合给所有的手机
//            for (InetAddress e : ipList) {
//                sb.append(e.toString() + ",");
//            }
//        }
//        return sb.toString();
//    }

    @Override
    public void onBackPressed() {
        quitGroup();
    }

    private void closeSocket() {

        if (mQuitMsgSocket != null) {
            mQuitMsgSocket.disconnect();
            mQuitMsgSocket.close();
            Log.e("shareFileAC", "收发退出消息的socket已经被close");
            mQuitMsgSocket = null;
        }
        if (mDatagramSocket != null) {
            mDatagramSocket.disconnect();
            mDatagramSocket.close();
            mDatagramSocket = null;
        }
    }

    /**
     * 以下为文件接收ＵＩ的相关代码
     */
    private void fileReceiverUi(String msgId, IpPortInfo ipPortInfo) {
        /**
         * TODO
         * 保存 MsgId？ ，senderName，senderAvatar，receiverName
         * 到数据库
         * String ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
         * String currentName = DaoHelper.getUserBySsidd(ssid).getUserName();
         */
        if (mSenderJson.length() > 0) {
            /*
            为了显示发送者信息
             */
            //获取手机的唯一编码
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String DEVICE_ID = tm.getDeviceId();
            //获取手机的设备id
            String ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
            //将设备id和手机的唯一编码拼接成一个唯一的id
            String uniqueId = ssid + DEVICE_ID.substring(10, 14);

            //获取自己的的名字
            String currentName = DaoHelper.getUserBySsidd(uniqueId).getUserName();

            User user = User.toObject(mSenderJson);
            LogUtils.e("=================================");
            LogUtils.e("ShareAc", "MsgId>>>" + msgId);
            LogUtils.e("ShareAc", "senderName>>>" + user.getUserName());
            LogUtils.e("ShareAc", "senderAvatar>>>" + user.getUserAvatar());
            LogUtils.e("ShareAc", "ReceiveName>>>" + currentName);
            LogUtils.e("=================================");

        /*
        保存到数据库
            10-30 15:51:54.034 9611-9611/com.bestgo.kuaichuancc E/ShareAc: MsgId>>>154da7a8-8937-4a23-a626-f6fd82125466
            10-30 15:51:54.034 9611-9611/com.bestgo.kuaichuancc E/ShareAc: senderName>>>Whiteman
            10-30 15:51:54.034 9611-9611/com.bestgo.kuaichuancc E/ShareAc: senderAvatar>>>2130903049
            10-30 15:51:54.034 9611-9611/com.bestgo.kuaichuancc E/ShareAc: ReceiveName>>>Blackman
         */
        /*
        第一个进入msgId 不唯一
        android.database.sqlite.SQLiteConstraintException: column MSG_ID is not unique (code 19)
         */
            //(自增长，msgId， 发送方用户名，发送方头像，接收方用户名)
            HeaderDesc headerDesc = new HeaderDesc(null, msgId, user.getUserName(), user.getUserAvatar(), currentName);
            DaoHelper.inserts(headerDesc);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShareReceiverAdapter(getContext(), mSenderJson);
        mRecyclerView.setAdapter(mAdapter);
        mIpPortInfo = ipPortInfo;

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_FILE);
        } else {
            initServer(); //启动接收服务

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_WRITE_FILE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initServer();
                //启动接收服务
            } else {
                Toast.makeText(this, getResources().getString(R.string.share_toast), Toast.LENGTH_SHORT).show();
                ApMgr.disableAp(ShareFileActivity.this);
                finish();
            }
            return;
        } else {
            ShareFileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ShareFileActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    ServerSocket serverSocket;

    /**
     * 开启文件接收端服务
     */
    private void initServer() {
        try {
            if (serverSocket == null) {
                serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(Constant.DEFAULT_SERVER_PORT));
            }
            mReceiverServer = new ServerReceiverRunnable(serverSocket);
            mRecievierServerThread = new Thread(mReceiverServer);
            mRecievierServerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新进度 和 耗时的 View
     */

    private void updateTotalProgressView() {
        try {
            //设置传送的总容量大小
            mStorageArray = FileUtils.getFileSizeArrayStr(mTotalLen);
            tv_value_storage.setText(mStorageArray[0]);
            tv_unit_storage.setText(mStorageArray[1]);
            //设置传送的时间情况
            mTimeArray = FileUtils.getTimeByArrayStr(mTotalTime);
            tv_value_time.setText(mTimeArray[0]);
            tv_unit_time.setText(mTimeArray[1]);

            //设置传送的进度条情况
            if (mHasSendedFileCount == App.getAppContext().getReceiverFileInfoMap().size()) {
                pb_total.setProgress(0);

                tv_value_storage.setTextColor(getResources().getColor(R.color.white));
                tv_value_time.setTextColor(getResources().getColor(R.color.white));

                return;
            }

            // TODO: 2017/5/2

            long total = App.getAppContext().getAllReceiverFileInfoSize();
            total = 0;

            int percent = (int) (mTotalLen * 100 / total);
            pb_total.setProgress(percent);
            if (total == mTotalLen) {
                pb_total.setProgress(0);
                tv_value_storage.setTextColor(getResources().getColor(R.color.white));
                tv_value_time.setTextColor(getResources().getColor(R.color.white));
            }
        } catch (Exception e) {
            //convert storage array has some problem
        }
    }

    public void sendMsgToFileSender(final IpPortInfo ipPortInfo) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sendFileReceiverInitSuccessMsgToFileSender(ipPortInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

//    DatagramSocket mDatagramSocket;

    public void sendFileReceiverInitSuccessMsgToFileSender(IpPortInfo ipPortInfo) throws Exception {
        if (mDatagramSocket == null) {
            mDatagramSocket = new DatagramSocket(null);
            mDatagramSocket.setReuseAddress(true);
            mDatagramSocket.bind(new InetSocketAddress(ipPortInfo.getPort() + 1));
        }
//        mDatagramSocket = new DatagramSocket(ipPortInfo.getPort() + 1);
        byte[] receiveData = new byte[Constant.SPEED_BYTE];
        byte[] sendData = null;
        InetAddress ipAddress = ipPortInfo.getInetAddress();
        //1.发送 文件接收方 初始化
        sendData = Constant.MSG_FILE_RECEIVER_INIT_SUCCESS.getBytes(BaseTransfer.UTF_8);
        DatagramPacket sendPacket =
                new DatagramPacket(sendData, sendData.length, ipAddress, ipPortInfo.getPort());
        mDatagramSocket.send(sendPacket);
        sendPacket.setLength(sendData.length);
//        closeSocket();
        sendPacket = null;
    }

    class ServerReceiverRunnable implements Runnable {
        ServerSocket serverSocket;
        private Socket socket;

        public ServerReceiverRunnable(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {

                while (!Thread.currentThread().isInterrupted()) {
                    //发送初始化成功的消息
                    mHandler.obtainMessage(MSG_FILE_RECEIVER_INIT_SUCCESS).sendToTarget();

                    if (serverSocket.isBound()) {

                    } else {
                        serverSocket = new ServerSocket();
                        serverSocket.setReuseAddress(true);
                        serverSocket.bind(new InetSocketAddress(Constant.DEFAULT_SERVER_PORT));
                    }
                    socket = serverSocket.accept();

                    FileReceiver fileReceiver = new FileReceiver(socket);

                    fileReceiver.setOnReceiveListener(new FileReceiver.OnReceiveListener() {
                        @Override
                        public void onStart() {
                            mLastUpdateLen = 0;
                            mLastUpdateTime = System.currentTimeMillis();
                        }

                        @Override
                        public void onGetFileInfo(FileInfo fileInfo) {
                            //提示收到一个文件
                            mHandler.obtainMessage(MSG_ADD_FILE_INFO, fileInfo).sendToTarget();
                            mCurFileInfo = fileInfo;

                            App.getAppContext().addReceiverFileInfo(mCurFileInfo);
                            Log.e("解析得到的fileInfo名和路径", fileInfo.getName() + fileInfo.getFilePath());

                            initDatas(fileInfo);
                            mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                        }

                        @Override
                        public void onGetScreenshot(Bitmap bitmap) {
                        }

                        @Override
                        public void onProgress(long progress, long total) {
                            //=====更新进度 流量 时间视图 start ====//
                            mCurOffset = progress - mLastUpdateLen > 0 ? progress - mLastUpdateLen : 0;
                            mTotalLen = mTotalLen + mCurOffset;
                            mLastUpdateLen = progress;
                            mCurTimeOffset = System.currentTimeMillis() - mLastUpdateTime > 0 ? System.currentTimeMillis() - mLastUpdateTime : 0;
                            mTotalTime = mTotalTime + mCurTimeOffset;
                            mLastUpdateTime = System.currentTimeMillis();

                            //=====更新进度 流量 时间视图 end ====//

                            mCurFileInfo.setProcceed(progress);

                            App.getAppContext().updateReceiverFileInfo(mCurFileInfo);

                            mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                        }

                        @Override
                        public void onSuccess(FileInfo fileInfo) {
                            //=====更新进度 流量 时间视图 start ====//
                            mHasSendedFileCount++;
                            mTotalLen = mTotalLen + (fileInfo.getSize() - mLastUpdateLen);
                            mLastUpdateLen = 0;
                            mLastUpdateTime = System.currentTimeMillis();
                            //=====更新进度 流量 时间视图 end ====//

                            fileInfo.setResult(FileInfo.FLAG_SUCCESS);

                            App.getAppContext().updateReceiverFileInfo(fileInfo);
                            /**
                             * 保存每个文件路径到数据库
                             */
                            String jsonStr = FileInfo.toJsonStr(fileInfo);
                            LogUtils.e("ShareFileAc", "date>>>" + fileInfo.getDate());
                            LogUtils.e("ShareFileAc", "jsonStr>>>" + jsonStr);
                            //\/data\/app\/com.lenovo.anyshare-1.apk
                            Log.e("接收时的Fileinfo", fileInfo.getFilePath());
                            EachFile file = new EachFile(null, jsonStr);
                            //将接收到的数据保存到数据库中，以便在历史界面查询
                            DaoHelper.inserts(file);
                            //更新进度
                            initDatas(fileInfo);

                            mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                        }

                        @Override
                        public void onFailure(Throwable t, FileInfo fileInfo) {
                            mHasSendedFileCount++;//统计发送文件
                            fileInfo.setResult(FileInfo.FLAG_FAILURE);
                            App.getAppContext().updateFileInfo(fileInfo);
                            mHandler.sendEmptyMessage(MSG_UPDATE_FILE_INFO);
                        }
                    });
                    App.getAppContext().MAIN_EXECUTOR.execute(fileReceiver);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                try {
                    if (socket != null)
                        socket.close();

                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
            if (mDatagramSocket != null) {
                mDatagramSocket.close();
                mDatagramSocket.disconnect();
                mDatagramSocket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isNullOrBlank(String str) {
        return str == null || str.equals("");
    }

}
