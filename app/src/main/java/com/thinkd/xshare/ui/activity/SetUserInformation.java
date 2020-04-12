package com.thinkd.xshare.ui.activity;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.AllUser;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.util.LogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.GET_ACCOUNTS;
import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

/**
 * Created by 百思移动 on 2017/10/20.
 * 用户第一次进入App，设置用户信息
 */
@RuntimePermissions
public class SetUserInformation extends BaseActivity {
    //    @Bind(R.id.toolbar)
//    Toolbar toolbar;
    @Bind(R.id.iv_avatar)
    ImageView ivAvatar;
    @Bind(R.id.ed_name)
    EditText edName;
    @Bind(R.id.iv_avatar_1)
    ImageView ivAvatar1;
    @Bind(R.id.iv_avatar_2)
    ImageView ivAvatar2;
    @Bind(R.id.iv_avatar_3)
    ImageView ivAvatar3;
    @Bind(R.id.iv_avatar_4)
    ImageView ivAvatar4;
    @Bind(R.id.iv_avatar_5)
    ImageView ivAvatar5;
    @Bind(R.id.iv_avatar_6)
    ImageView ivAvatar6;
    @Bind(R.id.iv_avatar_7)
    ImageView ivAvatar7;
    @Bind(R.id.iv_avatar_8)
    ImageView ivAvatar8;
    @Bind(R.id.iv_icon_1)
    ImageView ivIcon1;
    @Bind(R.id.iv_icon_2)
    ImageView ivIcon2;
    @Bind(R.id.iv_icon_3)
    ImageView ivIcon3;
    @Bind(R.id.iv_icon_4)
    ImageView ivIcon4;
    @Bind(R.id.iv_icon_5)
    ImageView ivIcon5;
    @Bind(R.id.iv_icon_6)
    ImageView ivIcon6;
    @Bind(R.id.iv_icon_7)
    ImageView ivIcon7;
    @Bind(R.id.iv_icon_8)
    ImageView ivIcon8;
    @Bind(R.id.btn_next)
    Button btnNext;

    int id = R.mipmap.avatar_1;
    String ssid = null;
    String uniqueId = null;
    private Account[] mAccounts;
    String googleName = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_information);
        ButterKnife.bind(this);

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid+DEVICE_ID.substring(10,14);
        Intent intent = getIntent();
        intent.getStringExtra("googleName");
        if(intent.getStringExtra("googleName")!=null){
            googleName = intent.getStringExtra("googleName");
        }else{
//            SetUserInformationPermissionsDispatcher.getGoogleNameWithCheck(SetUserInformation.this);
        }
        if(googleName!=null){
            edName.setText(googleName);
        }else{
            edName.setText(uniqueId);
        }
    }

    /**
     * 获取手机中的Google账号
     */
    @NeedsPermission({GET_ACCOUNTS})
    void getGoogleName(){
        //获取当前手机里已经登录的Google账号
        mAccounts = AccountManager.get(this).getAccounts();
//        mAccounts = AccountManager.get(this).getAccountsByType("com.google");
        for(Account a:mAccounts){
            googleName = a.name;
            Log.i("NAME",a.type+">>>>>>"+googleName);
        }
    }

    @OnShowRationale({GET_ACCOUNTS})
    void showRationaleForgetGoogleName(PermissionRequest request) {
        showRationaleDialog((R.string.permission_get_google_name), request);
    }

    @OnPermissionDenied({Manifest.permission.GET_ACCOUNTS})
    void showRationaleForgetGoogleNameenied() {
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

    @OnClick({R.id.toolbar, R.id.iv_avatar, R.id.ed_name, R.id.iv_avatar_1, R.id.iv_avatar_2, R.id.iv_avatar_3, R.id.iv_avatar_4, R.id.iv_avatar_5, R.id.iv_avatar_6, R.id.iv_avatar_7, R.id.iv_avatar_8, R.id.iv_icon_1, R.id.iv_icon_2, R.id.iv_icon_3, R.id.iv_icon_4, R.id.iv_icon_5, R.id.iv_icon_6, R.id.iv_icon_7, R.id.iv_icon_8, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.iv_avatar:
                break;
            case R.id.ed_name:
                break;
            case R.id.iv_avatar_1:
                ivAvatar.setImageResource(R.mipmap.avatar_1);
                id = R.mipmap.avatar_1;
                break;
            case R.id.iv_avatar_2:
                ivAvatar.setImageResource(R.mipmap.avatar_2);
                id = R.mipmap.avatar_2;
                break;
            case R.id.iv_avatar_3:
                ivAvatar.setImageResource(R.mipmap.avatar_3);
                id = R.mipmap.avatar_3;
                break;
            case R.id.iv_avatar_4:
                ivAvatar.setImageResource(R.mipmap.avatar_4);
                id = R.mipmap.avatar_4;
                break;
            case R.id.iv_avatar_5:
                ivAvatar.setImageResource(R.mipmap.avatar_5);
                id = R.mipmap.avatar_5;
                break;
            case R.id.iv_avatar_6:
                ivAvatar.setImageResource(R.mipmap.avatar_6);
                id = R.mipmap.avatar_6;
                break;
            case R.id.iv_avatar_7:
                ivAvatar.setImageResource(R.mipmap.avatar_7);
                id = R.mipmap.avatar_7;
                break;
            case R.id.iv_avatar_8:
                ivAvatar.setImageResource(R.mipmap.avatar_8);
                id = R.mipmap.avatar_8;
                break;
            case R.id.iv_icon_1:
                ivAvatar.setImageResource(R.mipmap.avatar1);
                id = R.mipmap.avatar1;
                break;
            case R.id.iv_icon_2:
                ivAvatar.setImageResource(R.mipmap.avatar2);
                id = R.mipmap.avatar2;
                break;
            case R.id.iv_icon_3:
                ivAvatar.setImageResource(R.mipmap.avatar3);
                id = R.mipmap.avatar3;
                break;
            case R.id.iv_icon_4:
                ivAvatar.setImageResource(R.mipmap.avatar4);
                id = R.mipmap.avatar4;
                break;
            case R.id.iv_icon_5:
                ivAvatar.setImageResource(R.mipmap.avatar5);
                id = R.mipmap.avatar5;
                break;
            case R.id.iv_icon_6:
                ivAvatar.setImageResource(R.mipmap.avatar6);
                id = R.mipmap.avatar6;
                break;
            case R.id.iv_icon_7:
                ivAvatar.setImageResource(R.mipmap.avatar7);
                id = R.mipmap.avatar7;
                break;
            case R.id.iv_icon_8:
                ivAvatar.setImageResource(R.mipmap.avatar8);
                id = R.mipmap.avatar8;
                break;
            case R.id.btn_next:
                //保存更改的信息

                if(edName.getText().toString().equals("")){
                    //提示用户不符合要求
                    Toast.makeText(this, getResources().getString(R.string.nav_toast), Toast.LENGTH_SHORT).show();
                }else{
                    //保存数据
                    insertInformation();
                    insertUserInformation();
                }
                break;
        }
    }

    //向数据库中添加信息

    private void insertUserInformation(){
        String userName = edName.getText().toString();
        try{
            User stu = new User(null, uniqueId, userName, id,"default");
//            long end = userDao.insert(stu);
            long end = DaoHelper.inserts(stu);
            String msg = "";
            if (end > 0) {
                msg = "新增成功~";
                //添加信息成功后，跳转到主页
                Intent intent = new Intent(SetUserInformation.this, FirstActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "新增失败2", Toast.LENGTH_SHORT).show();
                msg = "新增失败~";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void insertInformation(){
        String userName = edName.getText().toString();
        try{
            AllUser user = new AllUser(null, uniqueId, userName, id);
//            long end = userDao.insert(stu);
            long end = DaoHelper.inserts(user);
            String msg = "";
            if (end > 0) {
                msg = "新增成功~";
                //添加信息成功后，跳转到主页
            } else {
                Toast.makeText(this, "新增失败1", Toast.LENGTH_SHORT).show();
                msg = "新增失败~";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
