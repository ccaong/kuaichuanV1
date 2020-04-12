package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.common.Constant;
import com.thinkd.xshare.dao.DaoHelper;
import com.thinkd.xshare.dao.HeaderDesc;
import com.thinkd.xshare.dao.User;
import com.thinkd.xshare.entity.FileEntity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.history_mvp.music.MusicContract;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.ApkUtils;
import com.thinkd.xshare.util.FileUtils;
import com.thinkd.xshare.util.LogUtils;
import com.thinkd.xshare.widget.RoundProgressbar;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.thinkd.xshare.ui.activity.ShareFileActivity.isNullOrBlank;

/**
 * Created by altman29 on 2017/10/30.
 * e-mial:s1yuan_chen@163.com
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {


    private List<FileEntity> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;

    public HistoryAdapter(Context context, List<FileEntity> datas) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(mInflater.inflate(R.layout.item_history_content, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDatas != null && mDatas.size() > 0) {
            final FileEntity fileEntity = mDatas.get(position);
            if (fileEntity != null) {
                //desc A和Avatar都需要
                String historyDate = fileEntity.getHistoryDate();
                //转换时间显示的格式
                String month = historyDate.substring(0, 2);

                switch (month) {
                    case "1":
                        month = "Jan";
                        break;
                    case "2":
                        month = "Feb";
                        break;
                    case "3":
                        month = "Mar";
                        break;
                    case "4":
                        month = "Apr";
                        break;
                    case "5":
                        month = "May";
                        break;
                    case "6":
                        month = "Jun";
                        break;
                    case "7":
                        month = "Jul";
                        break;
                    case "8":
                        month = "Aug";
                        break;
                    case "9":
                        month = "Sep";
                        break;
                    case "10":
                        month = "Oct";
                        break;
                    case "11":
                        month = "Nov";
                        break;
                    case "12":
                        month = "Dec";
                        break;
                }

                String day = historyDate.substring(3, 5);
                String year = historyDate.substring((historyDate.lastIndexOf("-") + 1), historyDate.length());
                if (position == 0) {
                    //第一条数据一定会显示时间信息
                    holder.mLeftView.setVisibility(View.VISIBLE);
                    holder.mRightView.setVisibility(View.VISIBLE);
                    holder.mTvDateHeader.setVisibility(View.VISIBLE);
                    holder.mTvDateHeader.setText(month + " " + day + "," + year);
                } else {
                    if (!TextUtils.equals(fileEntity.getHistoryDate(), mDatas.get(position - 1).getHistoryDate())) {
                        //前后两条数据时间不一致，显示时间信息
                        holder.mLeftView.setVisibility(View.VISIBLE);
                        holder.mRightView.setVisibility(View.VISIBLE);
                        holder.mTvDateHeader.setVisibility(View.VISIBLE);
                        holder.mTvDateHeader.setText(month + " " + day + "," + year);
                    } else {
                        holder.mTvDateHeader.setVisibility(View.GONE);
                        holder.mLeftView.setVisibility(View.GONE);
                        holder.mRightView.setVisibility(View.GONE);
                    }
                }

                //从数据库中获取文件头信息
                HeaderDesc headerDesc = DaoHelper.queryByMsgId(fileEntity.getMsgId());
                holder.mIvSender.setImageResource(headerDesc.getSenderAvatar());
                String sendTo = " send to ";

                //获取自己的名字
                TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String DEVICE_ID = tm.getDeviceId();
                //获取手机的设备id
                String ssid = (isNullOrBlank(android.os.Build.DEVICE) ? Constant.DEFAULT_SSID : android.os.Build.DEVICE);
                //将设备id和手机的唯一编码拼接成一个唯一的id
                String uniqueId = ssid+DEVICE_ID.substring(10,14);
                //查询到自己（服务端）的详细信息
                User userBySsidd = DaoHelper.getUserBySsidd(uniqueId);
                if(userBySsidd.getUserName().equals(headerDesc.getSenderName())){
                    holder.mOtherName.setText(headerDesc.getSenderName());
                    holder.mOtherName.setTextColor(mContext.getResources().getColor(R.color.color_2a60ff));
                    holder.mTvSendto.setText(sendTo);
                    holder.mMyName.setText(headerDesc.getReceiveName());
                }else {
                    holder.mOtherName.setText(headerDesc.getSenderName());
                    holder.mTvSendto.setText(sendTo);
                    holder.mMyName.setText(headerDesc.getReceiveName());
                    holder.mMyName.setTextColor(mContext.getResources().getColor(R.color.color_2a60ff));
                }


                //获取文件数量
                int count = fileEntity.getImgList().size() + fileEntity.getApkList().size() + fileEntity.getVideoList().size();
                long size = 0;
                for (FileInfo fileInfo : fileEntity.getImgList()) {
                    size += fileInfo.getSize();
                }
                for (FileInfo fileInfo : fileEntity.getApkList()) {
                    size += fileInfo.getSize();
                }
                for (FileInfo fileInfo : fileEntity.getVideoList()) {
                    size += fileInfo.getSize();
                }

                //转换文件长度
                String totalSize = FileUtils.getFileSize(size);
                String totalDesc = count + " files, total " + totalSize;
                holder.mTvTotaldesc.setText(totalDesc);

                //content
                //pic
                if (fileEntity.getImgList() != null && fileEntity.getImgList().size() > 0) {
                    //pic
                    HistoryImageAdapter adapter = new HistoryImageAdapter(mContext, fileEntity.getImgList());
                    holder.mRvPhotoArea.setLayoutManager(new GridLayoutManager(mContext, 4));
                    holder.mRvPhotoArea.setAdapter(adapter);
                    holder.mRlPhotoArea.setVisibility(View.VISIBLE);

                    //点击事件
                    adapter.setOnItemClickListener(new HistoryImageAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            //点击图片
                            FileInfo fileInfo = fileEntity.getImgList().get(pos);
                            LogUtils.e("HistoryAdapter", "pic fileInfo.getFilePath()" + FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("历史页面", "图片", "点击");
                        }
                    });
                } else {
                    holder.mRlPhotoArea.setVisibility(View.GONE);
                }
                //apk
                if (fileEntity.getApkList() != null && fileEntity.getApkList().size() > 0) {
                    //apk
                    HistoryApkAdapter adapter = new HistoryApkAdapter(mContext, fileEntity.getApkList());
                    holder.mRvApkArea.setLayoutManager(new LinearLayoutManager(mContext));
                    holder.mRvApkArea.setAdapter(adapter);
                    holder.mRlApkArea.setVisibility(View.VISIBLE);
                    adapter.setOnItemClickListener(new HistoryApkAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            FileInfo fileInfo = fileEntity.getApkList().get(pos);

                            String path = FileUtils.getLocalFilePath(fileInfo.getFilePath());

                            String aa = path.substring(0, path.lastIndexOf("/"));
                            aa = aa + "/" + fileInfo.getName() + ".apk";
                            LogUtils.e("HistoryAdapter", "apk fileInfo.getFilePath()" + aa);

                            ApkUtils.install(mContext, aa);
                            Firebase.getInstance(getApplicationContext()).logEvent("历史页面", "app安装", "点击");
                        }
                    });
                } else {
                    holder.mRlApkArea.setVisibility(View.GONE);
                }

                //video
                if (fileEntity.getVideoList() != null && fileEntity.getVideoList().size() > 0) {
                    //video
                    HistoryVideoAdapter adapter = new HistoryVideoAdapter(mContext, fileEntity.getVideoList());
                    holder.mRvVideoArea.setLayoutManager(new LinearLayoutManager(mContext));
                    holder.mRvVideoArea.setAdapter(adapter);
                    holder.mRlVideoArea.setVisibility(View.VISIBLE);

                    adapter.setOnItemClickListener(new HistoryVideoAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            FileInfo fileInfo = fileEntity.getVideoList().get(pos);
                            LogUtils.e("HistoryAdapter", "video fileInfo.getFilePath()" + FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("历史页面", "视频", "点击");
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
        @Bind(R.id.tv_date_header) TextView mTvDateHeader;
        @Bind(R.id.iv_sender) ImageView mIvSender;
        @Bind(R.id.otherName)TextView mOtherName;
        @Bind(R.id.tv_sendto) TextView mTvSendto;
        @Bind(R.id.tv_my_name)TextView mMyName;
        @Bind(R.id.tv_totaldesc) TextView mTvTotaldesc;
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
        @Bind(R.id.left_view)View mLeftView;
        @Bind(R.id.right_view)View mRightView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
