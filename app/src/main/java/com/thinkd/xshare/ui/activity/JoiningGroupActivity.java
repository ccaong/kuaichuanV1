
package com.thinkd.xshare.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bestgo.adsplugin.ads.activity.ShowAdFilter;
import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.entity.OutReach;
import com.thinkd.xshare.util.LogUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 百思移动 on 2017/10/19.
 */

public class JoiningGroupActivity extends BaseActivity implements ShowAdFilter{

    private static final String SERVER_NAME = "SERVER_NAME";
    private static final String CONNECTION_TIME = "CONNECTION_TIME";
    @Bind(R.id.iv_rocket)
    ImageView ivRocket;
    @Bind(R.id.iv_rocker_bg)
    ImageView ivRockerBg;
    @Bind(R.id.connection_name)
    TextView tv_ConnectionName;

    public String MY_BORDCAST_SCANWIFI = "MY_BORDCAST_SCANWIFI";

    public static final int MSG_TO_START_NEXT_ACTIVITY = 0X99;
    public static final int MSG_TO_TRY_AGAIN = 0X98;

    public int i = 0;
    private String connectionTime;
    private IntentFilter intentFilter;
    private FileUpdateReceiver fileUpdateReceiver;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TO_START_NEXT_ACTIVITY) {
                List<OutReach> reaches = App.getOutReaches();
                String serverName = "";
                for (OutReach outReach : reaches) {
                    if (outReach.getIpAddress().equals(Constant.DEFAULT_SERVER_IP)) {
                        serverName = outReach.getName();
                    }
                }
                Intent intent = new Intent(JoiningGroupActivity.this, ShareFileActivity.class);
                intent.putExtra(SERVER_NAME, serverName);
                intent.putExtra(CONNECTION_TIME,connectionTime);
                startActivity(intent);
                checktime = false;
                finish();
            } else if (msg.what == MSG_TO_TRY_AGAIN) {
                i++;
                if (i >= 5) {
                    ivRocket.setVisibility(View.GONE);
                } else {
                    handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_TRY_AGAIN), 1000);
                }
            }
        }
    };
    private Runnable mRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joining_group);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        connectionTime = intent.getStringExtra(CONNECTION_TIME);

        tv_ConnectionName.setText(name);
        startTranslateAnimation();
        intentFilter = new IntentFilter();
        intentFilter.addAction(MY_BORDCAST_SCANWIFI);
        fileUpdateReceiver = new FileUpdateReceiver();
        registerReceiver(fileUpdateReceiver, intentFilter);

//        handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_TRY_AGAIN),1000);

    }
    /**
     * 不展示广告
     * @return
     */
    @Override
    public boolean allowShowAd() {
        return false;
    }

    boolean checktime = true;
    @Override
    protected void onResume() {
        super.onResume();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                if (checktime){
                    Firebase.getInstance(JoiningGroupActivity.this).logEvent("WiFi扫描页面", "超过10秒");
                    LogUtils.e("JoinGroupAc", ">>>>10s");
                }
                if(!checktime){
                    Firebase.getInstance(JoiningGroupActivity.this).logEvent("WiFi扫描页面", "加入群组成功");
                    LogUtils.e("JoinGroupAc", "加入群组成功");
                }
            }
        };
        handler.postDelayed(mRunnable, 10000);
    }

    class FileUpdateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            startAgain();

            handler.sendMessageDelayed(handler.obtainMessage(MSG_TO_START_NEXT_ACTIVITY), 500);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Intent intent = new Intent(JoiningGroupActivity.this, ShareFileActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            },TIME);
        }
    }

    private void startTranslateAnimation() {
/** 
      * 进行位移动画，标准步骤 
      * 1. 创建位移动画对象 
      * 构造函数 TranslateAnimation(float fromXDelta, float toXDelta, float fromYDelta, float toYDelta) 
      * 参数含义：相对于原图位置   fromXDelta X轴起点相对于原图偏移  toXDelta X轴终点相对于原图偏移 
      * fromYDelta Y轴起点相对于原图偏移  toYDelta Y轴终点相对于原图偏移 
      * 2. 设置动画终点是否保持 setFillAfter ： true 动画结束后留在终点  false:动画结束后返回起点 
      */
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -350.0f);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(1000);
        ivRocket.startAnimation(translateAnimation);
    }

    private void startAgain() {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, 0.0f, -350.0f, -1500.0f);
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(800);
        ivRocket.startAnimation(translateAnimation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(fileUpdateReceiver);
        if (mRunnable != null) {
            handler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mRunnable != null) {
            handler.removeCallbacks(mRunnable);
        }
    }
}
