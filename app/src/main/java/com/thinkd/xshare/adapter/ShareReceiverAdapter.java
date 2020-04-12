package com.thinkd.xshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.FileEntity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.thinkd.xshare.ui.activity.WaitingJoinActivity.isNullOrBlank;

/**
 * Created by altman29 on 2017/10/19.
 * e-mial:s1yuan_chen@163.com
 */

public class ShareReceiverAdapter extends RecyclerView.Adapter<ShareReceiverAdapter.ViewHolder> {

    private Context mContext;
    private Map<String, FileInfo> mDataHashMap;
    List<Map.Entry<String, FileInfo>> fileInfoMapList;
    List<FileEntity> mDatas;
    private String mSenderJson;
    private LayoutInflater mInflater;

    public ShareReceiverAdapter(Context context, String senderJson) {
        this.mContext = context;
        mDatas = new ArrayList<>();
        mSenderJson = senderJson;
        this.mInflater = LayoutInflater.from(context);
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        transitionData(fileInfoMapList);
        //排序
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
    }

    /**
     * 分组操作
     *
     * @param list
     */
    private void transitionData(List<Map.Entry<String, FileInfo>> list) {
        final HashMap<String, FileEntity> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            FileInfo fileInfo = list.get(i).getValue();
            FileEntity fileEntity = map.get(fileInfo.getMsgId());

            if (fileEntity == null) {
                fileEntity = new FileEntity();
                fileEntity.setApkList(new ArrayList<FileInfo>());
                fileEntity.setImgList(new ArrayList<FileInfo>());
                fileEntity.setVideoList(new ArrayList<FileInfo>());
                fileEntity.setMsgId(fileInfo.getMsgId());
                fileEntity.setDate(fileInfo.getDate());
                map.put(fileInfo.getMsgId(), fileEntity);
            }
            switch (fileInfo.getFileType()) {
                case FileInfo.TYPE_JPG:
                    fileEntity.getImgList().add(fileInfo);
                    break;
                case FileInfo.TYPE_APK:
                    fileEntity.getApkList().add(fileInfo);
                    break;
                case FileInfo.TYPE_MP4:
                    fileEntity.getVideoList().add(fileInfo);
                    break;
            }
        }

        /**
         * 多个线程同时操作一个数据集，所以放在UI线程操作。
         *             //add to List<FileEntity>
         */
        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                for (Map.Entry<String, FileEntity> entry : map.entrySet()) {
                    FileEntity fileEntity = entry.getValue();
                    mDatas.add(fileEntity);
                }

                Collections.sort(mDatas, new Comparator<FileEntity>() {
                    @Override
                    public int compare(FileEntity o1, FileEntity o2) {
                        //11-23-20
                        String[] sdate1 = o1.getDate().split("-");
                        String[] sdate2 = o2.getDate().split("-");
                        int sdate1hour = Integer.parseInt(sdate1[0]);
                        int sdate2hour = Integer.parseInt(sdate2[0]);
                        if (sdate1hour > sdate2hour) {
                            return -1;
                        } else if (sdate1hour < sdate2hour) {
                            return 1;
                        } else {
                            int sdate1minute = Integer.parseInt(sdate1[1]);
                            int sdate2minute = Integer.parseInt(sdate2[1]);
                            if (sdate1minute > sdate2minute) {
                                return -1;
                            } else if (sdate1minute < sdate2minute) {
                                return 1;
                            } else {
                                int sdate1second = Integer.parseInt(sdate1[2]);
                                int sdate2second = Integer.parseInt(sdate2[2]);
                                if (sdate1second > sdate2second) {
                                    return -1;
                                } else if (sdate1second < sdate2second) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        }
                    }
                });
            }
        });

    }

    /**
     * 更新数据
     */
    public void update() {
        mDataHashMap = App.getAppContext().getReceiverFileInfoMap();
        fileInfoMapList = new ArrayList<Map.Entry<String, FileInfo>>(mDataHashMap.entrySet());
        transitionData(fileInfoMapList);
        Collections.sort(fileInfoMapList, Constant.DEFAULT_COMPARATOR);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_share_content, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDatas != null && mDatas.size() > 0) {
            final FileEntity fileEntity = mDatas.get(position);

            if (fileEntity != null) {
                //desc A和Avatar都需要
                User user = User.toObject(mSenderJson);

                holder.mIvSender.setImageResource(user.getUserAvatar());

                //获取手机的唯一编码
                TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId();
                //获取手机的设备id
                String ssid = (isNullOrBlank(Build.DEVICE) ? Constant.DEFAULT_SSID : Build.DEVICE);
                //将设备id和手机的唯一编码拼接成一个唯一的id
                String uniqueId = ssid+DEVICE_ID.substring(10,14);

                String currentName = DaoHelper.getUserBySsidd(uniqueId).getUserName();
                String sendTo = user.getUserName() + " send to ";
                holder.mTvSendTo.setText(sendTo);
                holder.mTvMyName.setText(currentName);

                String totalDesc = App.getReceiverFileInfoMap().entrySet().size() + " files, total "
                        + FileUtils.showLongFileSzie(App.getAllReceiverFileInfoSize());
                holder.mTvTotalDesc.setText(totalDesc);

                //content
                //pic
                if (fileEntity.getImgList() != null && fileEntity.getImgList().size() > 0) {
                    //pic
                    ImageAdapter adapter = new ImageAdapter(mContext, fileEntity.getImgList());
                    holder.mRvPhotoArea.setLayoutManager(new GridLayoutManager(mContext, 4));
                    holder.mRvPhotoArea.setAdapter(adapter);
                    holder.mRlPhotoArea.setVisibility(View.VISIBLE);

                    adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            //点击图片
                            FileInfo fileInfo = fileEntity.getImgList().get(pos);
                            FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("发送页面","图片","打开");

                        }
                    });
                } else {
                    holder.mRlPhotoArea.setVisibility(View.GONE);
                }

                //apk
                if (fileEntity.getApkList() != null && fileEntity.getApkList().size() > 0) {
                    //apk
                    ApkAdapter adapter = new ApkAdapter(mContext, fileEntity.getApkList());

                    holder.mRvApkArea.setLayoutManager(new LinearLayoutManager(mContext));
                    holder.mRvApkArea.setAdapter(adapter);
                    holder.mRlApkArea.setVisibility(View.VISIBLE);
                } else {
                    holder.mRlApkArea.setVisibility(View.GONE);
                }

                //video
                if (fileEntity.getVideoList() != null && fileEntity.getVideoList().size() > 0) {
                    //video
                    VideoAdapter adapter = new VideoAdapter(mContext, fileEntity.getVideoList());
                    holder.mRvVideoArea.setLayoutManager(new LinearLayoutManager(mContext));
                    holder.mRvVideoArea.setAdapter(adapter);
                    holder.mRlVideoArea.setVisibility(View.VISIBLE);

                    adapter.setOnItemClickListener(new VideoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            FileInfo fileInfo = fileEntity.getVideoList().get(pos);
                            FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("发送页面","视频","打开");
                        }
                    });
                } else {
                    holder.mRlVideoArea.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_sendto) TextView mTvSendTo;
        @Bind(R.id.iv_sender) ImageView mIvSender;
        @Bind(R.id.rl_desc) RelativeLayout mRlDesc;
        @Bind(R.id.ll_photo_title) LinearLayout mLlPhotoTitle;
        @Bind(R.id.rv_photo_area) RecyclerView mRvPhotoArea;
        @Bind(R.id.ll_photo_area) LinearLayout mLlPhotoArea;
        @Bind(R.id.rl_photo_area) RelativeLayout mRlPhotoArea;
        @Bind(R.id.ll_apk_title) LinearLayout mLlApkTitle;
        @Bind(R.id.rv_apk_area) RecyclerView mRvApkArea;
        @Bind(R.id.ll_apk_area) LinearLayout mLlApkArea;
        @Bind(R.id.rl_apk_area) RelativeLayout mRlApkArea;
        @Bind(R.id.ll_video_title) LinearLayout mLlVideoTitle;
        @Bind(R.id.rv_video_area) RecyclerView mRvVideoArea;
        @Bind(R.id.ll_video_area) LinearLayout mLlVideoArea;
        @Bind(R.id.rl_video_area) RelativeLayout mRlVideoArea;
        @Bind(R.id.tv_totaldesc) TextView mTvTotalDesc;
        @Bind(R.id.tv_my_name) TextView mTvMyName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
