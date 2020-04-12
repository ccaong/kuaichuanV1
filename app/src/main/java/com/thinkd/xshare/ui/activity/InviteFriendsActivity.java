package com.thinkd.xshare.ui.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.DensityUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.util.Hashtable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 百思移动 on 2017/10/21.
 */

public class InviteFriendsActivity extends BaseActivity {

    private static final int QR_WIDTH = DensityUtil.dip2px(getContext(), 250);
    private static final int QR_HEIGHT = DensityUtil.dip2px(getContext(), 250);
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.btn_bluetooth)
    Button btnBluetooth;
    @Bind(R.id.btn_hotspot)
    Button btnHotspot;
    @Bind(R.id.qr_code)
    ImageView qrCode;
    @Bind(R.id.btn_more)
    Button btnMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_frient);
        ButterKnife.bind(this);
        tvTitle.setText("");
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "邀请好友页面");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.invite_title));

        //TODO 设置Google Play地址
//        qrCode.setImageBitmap(createImage("http://www.baidu.com"));

    }

    // 生成QR图
    private Bitmap createImage(String text) {
        try {

            if (TextUtils.isEmpty(text)) {
                return null;
            }
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix martix = writer.encode(text, BarcodeFormat.QR_CODE,
                    QR_WIDTH, QR_HEIGHT);

            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,
                    Bitmap.Config.ARGB_8888);

            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    @OnClick({R.id.btn_bluetooth, R.id.btn_hotspot, R.id.btn_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_bluetooth:
                shareBluetooth();
                Firebase.getInstance(getApplicationContext()).logEvent("邀请好友页面", "bluetooth","点击");
                break;
            case R.id.btn_hotspot:
                Intent intent = new Intent(InviteFriendsActivity.this, InviteByHotspotActivity.class);
                startActivity(intent);
                Firebase.getInstance(getApplicationContext()).logEvent("邀请好友页面", "hotspot","点击");
                break;
            case R.id.btn_more:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.setType("text/plain");
                intent1.putExtra(Intent.EXTRA_SUBJECT, "XShare");
                intent1.putExtra(Intent.EXTRA_TEXT, "Http://goo.gl/S6fZAi");
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent1, "XShare"));
                Firebase.getInstance(getApplicationContext()).logEvent("邀请好友页面", "more","点击");
                break;
        }
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

    private void shareBluetooth() {
        ApplicationInfo app = getApplicationInfo();
        String filePath = app.sourceDir;
        Log.e("蓝牙 获取app路径", filePath);

        File apkFile = new File(filePath);
        int size = (int) apkFile.length();
        Log.e("蓝牙 获取app大小", "" + size);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");

        intent.setPackage("com.android.bluetooth");

        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
        Intent chooser = Intent.createChooser(intent, "Share app");
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chooser);
    }

}
