package com.thinkd.xshare.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 百思移动 on 2017/9/19.
 */

public class BaseActivity extends AppCompatActivity {

    /**
     * 写文件的请求码
     */
    public static final int  REQUEST_CODE_WRITE_FILE = 200;

    /**
     * 读取文件的请求码
     */
    public static final int  REQUEST_CODE_READ_FILE = 201;


    //所有加入群组的手机的ip的集合
    public static List<InetAddress> ipList = new ArrayList<>();
    public List<InetAddress> sendIpList = new ArrayList<>();


    /**
     * 打开GPS的请求码
     */
    public static final int  REQUEST_CODE_OPEN_GPS = 205;

    static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;
//        StatusBarUtils.setStatuBarAndBottomBarTranslucent(this);
        super.onCreate(savedInstanceState);
//        mContext=this;2
    }

    @Override
    protected void onStart() {
        super.onStart();

//        window.setStatusBarColor(R.color.primary_black);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
//            View statusBarView = new View(this);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(this));
//            //primary_black
//            statusBarView.setBackgroundColor(getResources().getColor(R.color.primary_black));
//            contentView.getChildAt(0).setFitsSystemWindows(true);
//            contentView.addView(statusBarView, lp);
//        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static Context getContext(){
        return mContext;
    }

    protected void pushActivity(Class<?> mClass) {
        startActivity(new Intent(mContext, mClass));
    }
}

