package com.thinkd.xshare.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.ui.dialog.ImageLoadingDialog;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by altman29 on 2017/10/17.
 * e-mial:s1yuan_chen@163.com
 */

public class ImageShowerActivity extends Activity {

    @Bind(R.id.iv_shower) ImageView mIvShower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imageshower);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        final ImageLoadingDialog dialog = new ImageLoadingDialog(this);
        dialog.show();

        // x秒后关闭后dialog
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 1000 * new Random().nextInt(2));

        if (intent != null && intent.getStringExtra("path") != null) {
            String path = intent.getStringExtra("path");
            Glide
                    .with(this)
                    .load(new File(path))
                    .into(mIvShower);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

}