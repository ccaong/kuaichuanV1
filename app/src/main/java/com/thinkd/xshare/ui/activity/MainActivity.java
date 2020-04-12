package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.util.ApMgr;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.WifiMgr;

import java.io.File;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity {

    private Button btCreate;
    private Button btJoin;
    private TextView tvInstructions;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "群组页面");

        btCreate = (Button) findViewById(R.id.bt_create);
        btJoin = (Button) findViewById(R.id.bt_join);
        tvInstructions = (TextView) findViewById(R.id.tv_instructions);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        //toolbar相关
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        //创建群组
        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.getInstance(getApplicationContext()).logEvent("群组页面", "创建按钮", "点击");
                //检查权限
                MainActivityPermissionsDispatcher.createGroupWithCheck(MainActivity.this);
            }
        });

        //加入群组
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase.getInstance(getApplicationContext()).logEvent("群组页面", "加入按钮", "点击");
                //检查权限
                MainActivityPermissionsDispatcher.joinGroupWithCheck(MainActivity.this);
            }
        });


        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 查看说明
                Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("群组页面", "使用指南", "点击");
            }
        });

    }

    /**
     * 创建群组
     * 1,关闭wifi
     * 2,跳转到开启热点活动
     */
    @NeedsPermission({Manifest.permission.ACCESS_WIFI_STATE})
    void createGroup() {
        boolean wifiDisable = !WifiMgr.getInstance(this).isWifiEnable();
        //wifi没有开启
        if (wifiDisable) {
            //跳转到开启热点
            pushActivity(WaitingJoinActivity.class);
        } else {
            WifiMgr.getInstance(MainActivity.this).closeWifi();
            pushActivity(WaitingJoinActivity.class);
        }
    }

    /**
     * 加入群组
     * 1,开启wifi
     * 2,跳转到扫描wifi界面
     */
    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void joinGroup() {
        //如果热点开着就关闭
        if (ApMgr.isApOn(this)) {
            ApMgr.disableAp(this);
        }
        boolean wifiEnable = WifiMgr.getInstance(this).isWifiEnable();
        if (wifiEnable) {
            pushActivity(ScanWiFiActivity.class);
        } else {
            WifiMgr.getInstance(MainActivity.this).openWifi();
            //跳转到扫描附近热点界面
            pushActivity(ScanWiFiActivity.class);
        }
    }


    /**
     * 解释
     * 创建群组
     * 注解在用于向用户解释为什么需要这个权限，只有当用户第一次请求被用户拒绝时，下次请求之前会调用
     * @param request
     */
    @OnShowRationale({Manifest.permission.ACCESS_WIFI_STATE})
    void showRationaleForCreateGroup(PermissionRequest request) {
        showRationaleDialog((R.string.permission_creategroup), request);
    }

    /**
     *  加入群组
     * 注解在用于向用户解释为什么需要这个权限，只有当用户第一次请求被用户拒绝时，下次请求之前会调用
     * @param request
     */
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForJoingGroup(final PermissionRequest request) {
        showRationaleDialog((R.string.permission_joingroup), request);
    }


    /**
     * 创建群组
     * 当用户拒绝了权限请求时，调用
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_WIFI_STATE})
    void onCreateGroupDenied() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }

    /**
     * 加入群组
     * 当用户拒绝了权限请求时，调用
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onJoinGroupDenied() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }





    /**
     * 创建群组
     * 再当用户选中了不在询问复选框后，并拒绝了权限请求时的方法，
     * 可以向用户解释为何申请这个权限
     */
    @OnNeverAskAgain({Manifest.permission.ACCESS_WIFI_STATE})
    void onCreateGroupNeverAskAgain() {
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();

    }

    /**
     * 加入群组
     * 再当用户选中了不在询问复选框后，并拒绝了权限请求时的方法，
     * 可以向用户解释为何申请这个权限
     */
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
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


    /**
     * 第三步？？？
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    /**
     * 退出当前界面
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
