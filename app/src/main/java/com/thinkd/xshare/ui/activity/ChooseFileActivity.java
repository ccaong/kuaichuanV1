package com.thinkd.xshare.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.base.BaseTransfer;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.EachFile;
import com.thinkd.xshare.dao.HeaderDesc;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.receiver.SeletedFileListChangedBroadcastReceiver;
import com.thinkd.xshare.ui.fragment.FileInfoFragment;
import com.thinkd.xshare.util.LogUtils;
import com.thinkd.xshare.util.NetUtils;
import com.thinkd.xshare.util.WifiMgr;
import com.thinkd.xshare.widget.ShowSelectedFileInfoDialog;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thinkd.xshare.base.App.clearFileInfo;
import static com.thinkd.xshare.ui.activity.ScanWiFiActivity.MSG_TO_FILE_SENDER_UI;
import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

public class ChooseFileActivity extends BaseActivity {


    /**
     * 获取文件的请求码
     */
    public static final int REQUEST_CODE_GET_FILE_INFOS = 200;
    public static final int REQUEST_PERMISSION = 231;

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    //
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.btn_selected)
    Button btnSelected;
    @Bind(R.id.view_pager)
    ViewPager viewPager;


    /**
     * 应用，图片，音频， 视频 文件Fragment
     */
    FileInfoFragment mCurrentFragment;
    FileInfoFragment mApkInfoFragment;
    FileInfoFragment mJpgInfoFragment;
    FileInfoFragment mMp4InfoFragment;

    /**
     * 选中文件列表的对话框
     */
    ShowSelectedFileInfoDialog mShowSelectedFileInfoDialog;

    /**
     * 更新文件列表的广播
     */
    SeletedFileListChangedBroadcastReceiver mSeletedFileListChangedBroadcastReceiver = null;

    private String mMsgId;

    int apkNum = 0, photoNum = 0, videoNum = 0;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TO_FILE_SENDER_UI) {
                Intent intent = new Intent(ChooseFileActivity.this, SendFileActivity.class);
                startActivity(intent);
            }
        }
    };

    String name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_file);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.select_file_title));
        tvTitle.setText("");
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        initData();
    }

//    private void requestPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//
//            List<String> permissions = new ArrayList<String>();
//            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            } else {
////              preferencesUtility.setString("storage", "true");
//            }
//
//            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
//
//            } else {
////              preferencesUtility.setString("storage", "true");
//            }
//
//            if (!permissions.isEmpty()) {
////              requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
//
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSION);
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        btnSelected.setEnabled(true);
        clearFileInfo();
        getSelectedView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION: {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        initData();
                        System.out.println("Permissions --> " + "Permission Granted: " + permissions[i]);
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        initData();
                        System.out.println("Permissions --> " + "Permission Denied: " + permissions[i]);
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mApkInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_APK);
        mJpgInfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_JPG);
        //TODO
//        mMp3InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP3);
        mMp4InfoFragment = FileInfoFragment.newInstance(FileInfo.TYPE_MP4);
        mCurrentFragment = mApkInfoFragment;

        //TODO
        String[] titles = getResources().getStringArray(R.array.array_res);
        viewPager.setAdapter(new ResPagerAdapter(getSupportFragmentManager(), titles));
        viewPager.setCurrentItem(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TODO
        viewPager.setOffscreenPageLimit(3);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

//        setSelectedViewStyle(false);

        mShowSelectedFileInfoDialog = new ShowSelectedFileInfoDialog(getContext());

        mSeletedFileListChangedBroadcastReceiver = new SeletedFileListChangedBroadcastReceiver() {
            @Override
            public void onSeletecdFileListChanged() {
                update();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SeletedFileListChangedBroadcastReceiver.ACTION_CHOOSE_FILE_LIST_CHANGED);
        registerReceiver(mSeletedFileListChangedBroadcastReceiver, intentFilter);
    }

    /**
     * 更新选中文件列表的状态
     */
    private void update() {
        if (mApkInfoFragment != null) {
            mApkInfoFragment.updateFileInfoAdapter();
        }
        if (mJpgInfoFragment != null) {
            mJpgInfoFragment.updateFileInfoAdapter();
        }
        //TODO
//        if (mMp3InfoFragment != null) mMp3InfoFragment.updateFileInfoAdapter();

        if (mMp4InfoFragment != null) {
            mMp4InfoFragment.updateFileInfoAdapter();
        }
        //更新已选中Button
        getSelectedView();
    }

    @OnClick({R.id.btn_selected})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_selected: {

                //传输文件
                if (!App.getAppContext().isFileInfoMapExist()) {
                    //不存在选中的文件
                    Toast.makeText(this, getResources().getString(R.string.tip_please_select_your_file), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<OutReach> reaches = App.getOutReaches();
                if (reaches != null && reaches.size() > 0) {
                    for (int i = 0; i < reaches.size(); i++) {
                        if (!reaches.get(i).getIpAddress().equals(WifiMgr.getInstance(ChooseFileActivity.this).getCurrentIpAddress())) {
                            LogUtils.e("ChooseFileAc", "reaches.getIp>>>" + reaches.get(i).getIpAddress());
                            sendFile(reaches.get(i).getIpAddress());
                        }
                    }
                    btnSelected.setEnabled(false);
                }
                break;
            }
        }
    }

    /**
     * 发送文件的方法
     */
    private void sendFile(String ipAddress) {
        LogUtils.e("ChooseFileAc", "current ip>>>" + WifiMgr.getInstance(ChooseFileActivity.this).getCurrentIpAddress());
        LogUtils.e("ChooseFileAc", "send to ip>>>" + ipAddress);
        mMsgId = UUID.randomUUID().toString();
        LogUtils.e("ChooseFileAc", "MsgId>>>" + mMsgId);

        Runnable mUdpServerRuannable = createSendMsgToServerRunnable(ipAddress);
        App.MAIN_EXECUTOR.execute(mUdpServerRuannable);
    }

    DatagramSocket mDatagramSocket;

    private Runnable createSendMsgToServerRunnable(final String serverIP) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    startFileSenderServer(serverIP, Constant.DEFAULT_SERVER_COM_PORT_1);
                } catch (Exception e) {
                    System.out.println("端口占用异常+main" + e.toString());
                    e.printStackTrace();
                }
            }
        };
    }

    private void startFileSenderServer(String targetIpAddr, int serverPort) throws Exception {
//        Thread.sleep(3*1000);
        // 确保Wifi连接上之后获取得到IP地址
        int count = 0;
        //(targetIpAddr为0.0.0.0，&&<规定的次数)
        while (targetIpAddr.equals(Constant.DEFAULT_UNKOWN_IP) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(1000);
            targetIpAddr = WifiMgr.getInstance(getContext()).getIpAddressFromHotspot();
            count++;
        }
        count = 0;

        while (!NetUtils.pingIpAddress(targetIpAddr) && count < Constant.DEFAULT_TRY_TIME) {
            Thread.sleep(500);
            count++;
        }
        //创建mDatagramSocket  说明使用的udp
        if (mDatagramSocket == null) {
            mDatagramSocket = new DatagramSocket(null);
            //启用SO_REUSEADDR 套接字选项。  关闭socket时，立即释放socket绑定端口，一遍端口重用，默认为false
            mDatagramSocket.setReuseAddress(true);
            //绑定固定的端口号
            mDatagramSocket.bind(new InetSocketAddress(0));
        }
        byte[] receiveData = new byte[Constant.SPEED_BYTE];
        byte[] sendData = null;
        //获得主机的ip地址
        InetAddress ipAddress = InetAddress.getByName(targetIpAddr);


        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        String ssid = (isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        String uniqueId = ssid + DEVICE_ID.substring(10, 14);


        //0.发送 即将发送的文件列表 到文件接收方
        sendFileInfoListToFileReceiverWithUdp(serverPort, ipAddress, uniqueId);


        //TODO Olivia 需要名称
//        String ssid = "bestgo_" + (TextUtils.isEmpty(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);

        //1.发送 文件接收方 初始化
        User user = DaoHelper.getUserBySsidd(uniqueId);
        String jsonStr = User.toJsonStr(user);
        sendData = (Constant.MSG_FILE_RECEIVER_INIT + mMsgId + jsonStr).getBytes(BaseTransfer.UTF_8);
        LogUtils.e("ChooseAc", "sendData>>>" + (Constant.MSG_FILE_RECEIVER_INIT + mMsgId + jsonStr));
        /**
         * 发了两次
         */
        //ChooseAc: sendData>>>MSG_FILE_RECEIVER_INIT014ade28-1430-44cc-93d1-6d4d889c480c{"userSsid":"shamu","userName":"Whiteman","userAvatar":2130903049,"ipAddress":"0.0.0.0"}
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, serverPort);
        //这句话执行完毕后    接收方进入列表
        mDatagramSocket.send(sendPacket);
        //2.接收 文件接收方 初始化 反馈
        while (true) {
//        //数据报
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            mDatagramSocket.receive(receivePacket);
            String response = new String(receivePacket.getData(), BaseTransfer.UTF_8).trim();
            if (response != null && response.equals(Constant.MSG_FILE_RECEIVER_INIT_SUCCESS)) {
                //这句话 执行结束 发送方 进入列表并开始传输
                mHandler.obtainMessage(MSG_TO_FILE_SENDER_UI).sendToTarget();
            }
        }
    }

    /**
     * 发送即将发送的文件列表到文件接收方
     *
     * @param serverPort
     * @param ipAddress
     * @throws IOException
     */
    private void sendFileInfoListToFileReceiverWithUdp(int serverPort, InetAddress ipAddress, String userId) throws IOException {
        //1.1将发送的List<FileInfo> 发送给 文件接收方

        Map<String, FileInfo> sendFileInfoMap = App.getAppContext().getFileInfoMap();

        List<Map.Entry<String, FileInfo>> fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(sendFileInfoMap.entrySet());
        //排序
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);

        String date = new SimpleDateFormat("HH-mm-ss")
                .format(new Date(System.currentTimeMillis()));

        String historyDate = new SimpleDateFormat("MM-dd-yyyy")
                .format(new Date(System.currentTimeMillis()));

        for (Map.Entry<String, FileInfo> entry : fileInfoMapList) {
            if (entry.getValue() != null) {
                FileInfo fileInfo = entry.getValue();
                //文件的数量
                if (fileInfo.getFileType() == FileInfo.TYPE_APK) {
                    apkNum++;
                } else if (fileInfo.getFileType() == FileInfo.TYPE_JPG) {
                    photoNum++;
                } else if (fileInfo.getFileType() == FileInfo.TYPE_MP4) {
                    videoNum++;
                }

                /**
                 * 标识每批文件唯一表示MsgId
                 */
                fileInfo.setMsgId(mMsgId);
                fileInfo.setDate(date);
                fileInfo.setHistoryDate(historyDate);
                String fileInfoStr = FileInfo.toJsonStr(fileInfo);

                //保存发送的数据
                Log.e("发送时的Fileinfo", fileInfo.getFilePath());
                String jsonStr = FileInfo.toJsonStr(fileInfo);
                EachFile eachFile = new EachFile(null, jsonStr);
                DaoHelper.inserts(eachFile);

                DatagramPacket sendFileInfoListPacket =
                        new DatagramPacket(fileInfoStr.getBytes(), fileInfoStr.getBytes().length, ipAddress, serverPort);

                /**
                 * 文件列表信息 在for循环里一条一条的文件信息发送给的。 应该整合在一次里面。需要一个Entity 作为介质存储传递。
                 */
                try {
                    mDatagramSocket.send(sendFileInfoListPacket);
                    sendFileInfoListPacket.setLength(fileInfoStr.getBytes().length);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        User user = DaoHelper.getUserBySsidd(userId);
        HeaderDesc headerDesc = new HeaderDesc(null, mMsgId, user.getUserName(), user.getUserAvatar(), name);
        DaoHelper.inserts(headerDesc);
        Firebase.getInstance(getApplicationContext()).logEvent("send按钮点击", apkNum + "app" + "_" + photoNum + "picture" + "_" + videoNum + "video");
    }

    /**
     * 获取选中文件的View
     *
     * @return
     */
    public View getSelectedView() {
        //获取SelectedView的时候 触发选择文件

        if (App.getAppContext().getFileInfoMap() != null && App.getAppContext().getFileInfoMap().size() > 0) {
            int size = App.getAppContext().getFileInfoMap().size();
            btnSelected.setText(getContext().getResources().getString(R.string.str_has_selected_detail, size));

        } else {
            btnSelected.setText(getContext().getResources().getString(R.string.str_has_selected));
        }
        return btnSelected;
    }

    /**
     * 设置选中View的样式
     *
     * @param isEnable
     */
    private void setSelectedViewStyle(boolean isEnable) {
        //设置选中的个数
        if (isEnable) {

            btnSelected.setEnabled(true);
            btnSelected.setBackgroundResource(R.drawable.selector_bottom_text_common);
            btnSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btnSelected.setEnabled(false);
            btnSelected.setBackgroundResource(R.drawable.shape_bottom_text_unenable);
            btnSelected.setTextColor(getResources().getColor(R.color.darker_gray));
        }
    }

    /**
     * 资源的PagerAdapter
     */
    class ResPagerAdapter extends FragmentPagerAdapter {
        String[] sTitleArray;

        public ResPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public ResPagerAdapter(FragmentManager fm, String[] sTitleArray) {
            this(fm);
            this.sTitleArray = sTitleArray;
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                //VIDEO
                mCurrentFragment = mMp4InfoFragment;
            } else if (position == 1) {
                //APPS
                mCurrentFragment = mApkInfoFragment;
            } else if (position == 2) {
                //PHOTOS
                mCurrentFragment = mJpgInfoFragment;
            }
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            return sTitleArray.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return sTitleArray[position];
        }
    }

    @Override
    protected void onDestroy() {
        if (mSeletedFileListChangedBroadcastReceiver != null) {
            unregisterReceiver(mSeletedFileListChangedBroadcastReceiver);
            mSeletedFileListChangedBroadcastReceiver = null;
        }
        super.onDestroy();
    }


}

