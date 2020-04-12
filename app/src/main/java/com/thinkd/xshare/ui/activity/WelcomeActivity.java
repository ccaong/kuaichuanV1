package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.AllUser;
import com.thinkd.xshare.dao.DaoHelper;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.GET_ACCOUNTS;
import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

/**
 * Created by 百思移动 on 2017/10/19.
 *
 * @author CCAONG
 */
@RuntimePermissions
public class WelcomeActivity extends BaseActivity {

    private final long SPLASH_LENGTH = 5000;
    Handler handler = new Handler();
    String ssid;
    String uniqueId;
    private String mDEVICE_id;

    private Account[] mAccounts;
    String googleName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //请求全屏广告
        if (AdAppHelper.getInstance(this).getCustomCtrlValue("jinru_quanping", "1").equals("1")) {
            AdAppHelper.getInstance(getApplicationContext()).loadNewInterstitial(0);
        }
        //请求退出全屏
        if (AdAppHelper.getInstance(this).getCustomCtrlValue("tuichu_quanping", "1").equals("1")) {
            AdAppHelper.getInstance(getApplicationContext()).loadNewInterstitial(1);
        }

        //请求native广告
        AdAppHelper.getInstance(getApplicationContext()).loadNewNative();
        //请求banner广告
        AdAppHelper.getInstance(getApplicationContext()).loadNewBanner();

        //获取手机的设备id
        ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);

        WelcomeActivityPermissionsDispatcher.getDeviceIdWithCheck(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        WelcomeActivityPermissionsDispatcher.getGoogleNameWithCheck(this);

        //check
    }

    /**
     * 获取手机中的Google账号
     */
    @NeedsPermission({Manifest.permission.GET_ACCOUNTS})
    void getGoogleName(){
        //获取当前手机里已经登录的Google账号
//        mAccounts = AccountManager.get(this).getAccounts();
        mAccounts = AccountManager.get(this).getAccountsByType("com.google");
        if(mAccounts.length>0){
            googleName = mAccounts[0].name;
        }
    }


    @OnShowRationale({Manifest.permission.GET_ACCOUNTS})
    void showRationaleForgetGoogleName(PermissionRequest request) {
        showRationaleDialog((R.string.permission_get_google_name), request);
    }

//    @OnPermissionDenied({Manifest.permission.GET_ACCOUNTS})
//    void showRationaleForgetGoogleNameenied() {
//        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
//    }



    @NeedsPermission(Manifest.permission.READ_PHONE_STATE)
    void getDeviceId() {
        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        mDEVICE_id = tm.getDeviceId();
        gotoNextPage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WelcomeActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.READ_PHONE_STATE)
    void showRationaleForGetDeviceId(PermissionRequest request) {
        showRationaleDialog((R.string.permission_deviceid), request);
    }

    @OnPermissionDenied(Manifest.permission.READ_PHONE_STATE)
    void onGetDeviceIdDenied() {
        finish();
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.READ_PHONE_STATE)
    void onGetDeviceIdNeverAskAgain() {
        finish();
        Toast.makeText(this, R.string.permission_denied_toast, Toast.LENGTH_LONG).show();
    }

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();

                    }
                })
                .setNegativeButton(R.string.refuse, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    private void gotoNextPage() {
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid + mDEVICE_id.substring(10, 14);
        AllUser user = DaoHelper.querySingleBySSId(uniqueId);

        //根据ssid查询数据，如数据库中没有信息，就跳转到设置头像页面
        if (user == null) {
            //如果没有查询到信息就进入到设置个人信息的页面
            handler.postDelayed(new Runnable() {
                //使用handler的postDelayed实现延时跳转
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, SetUserInformation.class);
                    if(googleName!=null){
                        googleName = googleName.substring(0,googleName.lastIndexOf("@"));
                    }
                    intent.putExtra("googleName",googleName);
                    startActivity(intent);
                    finish();
                }
                //5秒后跳转至设置个人信息的界面
            }, SPLASH_LENGTH);
        } else {
            handler.postDelayed(new Runnable() {
                //使用handler的postDelayed实现延时跳转
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                }
                //5秒后跳转至应用主界面MainActivity
            }, SPLASH_LENGTH);
        }
    }
}
