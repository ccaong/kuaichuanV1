package com.thinkd.xshare.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

/**
 * Created by 百思移动 on 2017/10/19.
 * 头像修改界面
 * @author CCAONG
 */

public class ChooseAvatarActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

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

    int id = R.mipmap.avatar_1;
    String ssid = "";
    String uniqueId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_avatar);
        ButterKnife.bind(this);
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "头像修改页面");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.profile));

        //获取手机的唯一编码
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        //获取手机的设备id
        ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
        //将设备id和手机的唯一编码拼接成一个唯一的id
        uniqueId = ssid + DEVICE_ID.substring(10, 14);

        User user = selectDB();
        edName.setText(user.getUserName());
        ivAvatar.setImageResource(user.getUserAvatar());
        id = user.getUserAvatar();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!edName.getText().toString().equals("")) {
                    //保存用户更改的信息
                    updateDB();
                    updateAllUser();
                    Firebase.getInstance(getApplicationContext()).logEvent("头像", id);
                    finish();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.nav_toast), Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public User selectDB() {
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

    public void updateDB() {

        DaoHelper.updateUserInfo(uniqueId, edName.getText().toString(), id, "default");
    }

    public void updateAllUser() {
        DaoHelper.updateAllUserInfo(uniqueId, edName.getText().toString(), id);
    }


    @OnClick({R.id.toolbar, R.id.iv_avatar, R.id.iv_avatar_1, R.id.iv_avatar_2, R.id.iv_avatar_3, R.id.iv_avatar_4, R.id.iv_avatar_5, R.id.iv_avatar_6, R.id.iv_avatar_7, R.id.iv_avatar_8, R.id.iv_icon_1, R.id.iv_icon_2, R.id.iv_icon_3, R.id.iv_icon_4, R.id.iv_icon_5, R.id.iv_icon_6, R.id.iv_icon_7, R.id.iv_icon_8})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.toolbar:
                break;
            case R.id.iv_avatar:
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
        }
    }
}
