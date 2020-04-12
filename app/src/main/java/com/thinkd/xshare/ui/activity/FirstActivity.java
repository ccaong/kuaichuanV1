package com.thinkd.xshare.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.ui.activity.history.AppHistoryActivity;
import com.thinkd.xshare.util.ApMgr;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.WifiMgr;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

/**
 * Created by 百思移动 on 2017/10/10.
 * 主页
 * @author 百思移动
 */


public class FirstActivity extends BaseActivity {

    @Bind(R.id.tv_app_num)
    TextView mTvAppNum;
    @Bind(R.id.tv_photo_num)
    TextView mTvPhotoNum;
    @Bind(R.id.tv_video_num)
    TextView mTvVideoNum;
    @Bind(R.id.rl_app)
    RelativeLayout mRlApp;
    @Bind(R.id.rl_photo)
    RelativeLayout mRlPhoto;
    @Bind(R.id.rl_video)
    RelativeLayout mRlVideo;

    @Bind(R.id.iv_app)
    ImageView ivApp;
    @Bind(R.id.tv_app)
    TextView tvApp;
    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.tv_photo)
    TextView tvPhoto;
    @Bind(R.id.iv_video)
    ImageView ivVideo;
    @Bind(R.id.tv_video)
    TextView tvVideo;

    /**
     * 五个下级所需 count
     */
    private int jpgConut = 0, apkCount = 0, mp3Count = 0, mp4Count = 0, otherCount = 0;

    private NavigationView navigationView;

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.connect_button)
    Button ibtnShare;
    @Bind(R.id.circle_big_image_view)
    ImageView mBigCircleImageView;
    @Bind(R.id.circle_mid_image_view)
    ImageView mMiddleCircleImageView;


    @Bind(R.id.fl_ad)
    FrameLayout frameLayout;

    public static final int MSG_BTN_START = 0X91;
    String ssid = null;
    String uniqueId = null;
    Animation animation;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_BTN_START) {
                //开始动画
                startAnimation();
            }
            handler.sendMessageDelayed(handler.obtainMessage(MSG_BTN_START), 9000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        //展示全屏广告
        if (AdAppHelper.getInstance(this).getCustomCtrlValue("jinru_quanping", "1").equals("1")) {
            AdAppHelper.getInstance(getApplicationContext()).showFullAd(0);
        }

        Firebase.getInstance(getApplicationContext()).logEvent("主页面", "自定义日活");
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "主页面");

        //获取banner广告
        View banner = AdAppHelper.getInstance(getApplicationContext()).getBanner();
        //展示banner广告
        frameLayout.removeAllViews();
        frameLayout.addView(banner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle.setText("");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        initData();

        ibtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                Firebase.getInstance(getApplicationContext()).logEvent("主页面", "share按钮", "点击");
                Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        View headerView = navView.getHeaderView(0);
        ImageView ivHeaderIcon = (ImageView) headerView.findViewById(R.id.iv_header_icon);
        TextView tvName = (TextView) headerView.findViewById(R.id.tv_healder_name);

        ivHeaderIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(FirstActivity.this, ChooseAvatarActivity.class);
                startActivity(intent);
            }
        });
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(FirstActivity.this, ChooseAvatarActivity.class);
                startActivity(intent);
            }
        });

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_history) {
                    pushActivity(HistoryActivity.class);
                    Firebase.getInstance(getApplicationContext()).logEvent("侧边栏", "历史按钮", "点击");

                } else if (id == R.id.nav_share) {
                    Firebase.getInstance(getApplicationContext()).logEvent("侧边栏", "分享按钮", "点击");

                    Intent intent = new Intent(FirstActivity.this, InviteFriendsActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_feedback) {
                    Firebase.getInstance(getApplicationContext()).logEvent("侧边栏", "反馈按钮", "点击");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSdCIdCzWPJ6dzUHyFvaiSEP332NUpICnK0eOz01nvPLn2Sx6Q/viewform"));
                    startActivity(intent);
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
    }

    private void startAnimation() {
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_share);
        animation.setDuration(500);
        animation.setRepeatCount(1);
        mBigCircleImageView.startAnimation(animation);
        animation = AnimationUtils.loadAnimation(getContext(), R.anim.btn_share);
        animation.setDuration(450);
        animation.setRepeatCount(1);
        mMiddleCircleImageView.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivApp.setVisibility(View.VISIBLE);
        ivPhoto.setVisibility(View.VISIBLE);
        ivVideo.setVisibility(View.VISIBLE);
        handler.obtainMessage(MSG_BTN_START).sendToTarget();
        jpgConut = 0;
        apkCount = 0;
        mp3Count = 0;
        mp4Count = 0;
        otherCount = 0;
        initData();


        View headerView = navView.getHeaderView(0);
        ImageView ivHeaderIcon = (ImageView) headerView.findViewById(R.id.iv_header_icon);
        TextView tvName = (TextView) headerView.findViewById(R.id.tv_healder_name);

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        ssid = (isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid + DEVICE_ID.substring(10, 14);
        //根据uniqueId去查找用户信息
        User user = selectUser(uniqueId);

        //如果查询到结果不为空，就设置头像和名字
        if (uniqueId.equals(user.getUserSsid())) {
            //设置头像
            ivHeaderIcon.setImageResource(user.getUserAvatar());
            //设置姓名
            user.getUserName();
            tvName.setText(user.getUserName());
        } else {
            tvName.setText(uniqueId);
        }
    }

    private User selectUser(String uniqueId) {
        User user = new User();
        try {
            List<User> userList = DaoHelper.queryById(uniqueId);
            for (int i = 0; i < userList.size(); i++) {
                user = userList.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    @OnClick(R.id.rl_app)
    public void gotoAppPage() {
        ivApp.setVisibility(View.INVISIBLE);
        Firebase.getInstance(getApplicationContext()).logEvent("主页面", "app按钮", "点击");
        pushActivity(AppHistoryActivity.class);
    }

    @OnClick(R.id.rl_photo)
    public void gotoPhotoPage() {
        ivPhoto.setVisibility(View.INVISIBLE);
        Firebase.getInstance(getApplicationContext()).logEvent("主页面", "photo按钮", "点击");
        pushActivity(PhotoHistoryActivity.class);
    }

    @OnClick(R.id.rl_video)
    public void gotoMp3Page() {
        ivVideo.setVisibility(View.INVISIBLE);
        Firebase.getInstance(getApplicationContext()).logEvent("主页面", "video按钮", "点击");
        pushActivity(VideoHistoryActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //展开侧边栏
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.invite:
                //邀请好友
                Firebase.getInstance(getApplicationContext()).logEvent("邀请好友页面", "invite", "点击");
                Intent intent = new Intent(FirstActivity.this, InviteFriendsActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return true;
    }

    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //调用自定义的exit方法
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //双击退出
    private long checkTime = 0;

    private void exit() {
        if ((System.currentTimeMillis() - checkTime) > 2000) {
            Toast.makeText(FirstActivity.this, getResources().getString(R.string.exitApp), Toast.LENGTH_SHORT).show();
            checkTime = System.currentTimeMillis();
        } else {
            //关闭热点和wifi
            ApMgr.disableAp(FirstActivity.this);
            WifiMgr.getInstance(FirstActivity.this).openWifi();
            //展示全屏广告
            if (AdAppHelper.getInstance(this).getCustomCtrlValue("tuichu_quanping", "1").equals("1")) {
                AdAppHelper.getInstance(getApplicationContext()).showFullAd(1);
            }
            this.finish();
        }
    }

    /**
     * 获取不同类别文件及数量
     */
    private void initData() {
        File[] files = new File(FileUtils.getRootDirPath()).listFiles();
        getFileName(files);
        setNums();
    }

    private void setNums() {
        mTvAppNum.setText(apkCount + "");
        mTvPhotoNum.setText(jpgConut + "");
        mTvVideoNum.setText(mp4Count + "");
    }

    /**
     * 根据文件获取不同种类信息
     */
    private String getFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    getFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                    if (FileUtils.isJpgFile(fileName)) {
                        if (file.length() > 0) {
                            jpgConut++;
                        }
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    } else if (fileName.endsWith(".apk")) {
                        if (file.length() > 0) {
                            apkCount++;
                        }
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    } else if (fileName.endsWith(".mp3")) {
                        if (file.length() > 0) {
                            mp3Count++;
                        }
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    } else if (FileUtils.isMp4File(fileName)) {
                        if (file.length() > 0) {
                            mp4Count++;
                        }
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    } else {
                        otherCount++;
                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    }
                }
            }
        }
        return str;
    }
}
