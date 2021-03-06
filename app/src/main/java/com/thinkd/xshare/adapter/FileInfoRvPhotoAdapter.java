package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.bean.FileInfoBean;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.ChooseFileActivity;
import com.thinkd.xshare.util.AnimationUtils;

import java.util.List;

import static com.thinkd.xshare.base.BaseActivity.getContext;
import static com.thinkd.xshare.util.FileUtils.conversionTime;

/**
 * Created by 百思移动 on 2017/10/25.
 */

public class FileInfoRvPhotoAdapter extends RecyclerView.Adapter<FileInfoRvPhotoAdapter.PhotoViewHolder>{
    public List<FileInfoBean> mList;
    private int mType = FileInfo.TYPE_JPG;
    private Context mContext;
    private Fragment mFragment;
    DisplayPhotoAdapter displayPhotoAdapter;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        RecyclerView recyclerView;

        public PhotoViewHolder(View view){
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_file);
        }
    }

    public FileInfoRvPhotoAdapter(List<FileInfoBean> list, int type, Context context,Fragment fragment){
        this.mList = list;
        this.mType = type;
        this.mContext = context;
        this.mFragment = fragment;
    }

    @Override
    public FileInfoRvPhotoAdapter.PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_file,null,false);
        PhotoViewHolder holder = new PhotoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final FileInfoRvPhotoAdapter.PhotoViewHolder holder, int position) {
        final FileInfoBean fileInfoBean = mList.get(position);
        //转换日期格式
        String strDate = conversionTime(mContext,fileInfoBean.getFileDate());
        holder.tvTime.setText(strDate);
        displayPhotoAdapter = new DisplayPhotoAdapter(fileInfoBean.getFileInfoList(),mType,mContext);

        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 4);
        linearLayoutManager.setAutoMeasureEnabled(true);

        holder.recyclerView.setLayoutManager(linearLayoutManager);
        holder.recyclerView.setAdapter(displayPhotoAdapter);
        
        if(displayPhotoAdapter!=null){
            displayPhotoAdapter.setOnItemClickListener(new DisplayPhotoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FileInfo fileInfo = fileInfoBean.getFileInfoList().get(position);
                    if (App.getAppContext().isExist(fileInfo)) {
                        App.getAppContext().delFileInfo(fileInfo);
                        updateSelectedView();
                    }
                    else {
                        //1.添加任务
                        App.getAppContext().addFileInfo(fileInfo);
                        //2.添加任务 动画
                        View startView = null;
                        View targetView = null;

                        startView = view.findViewById(R.id.iv_shortcut);
                        if (mFragment.getActivity() != null && (mFragment.getActivity() instanceof ChooseFileActivity)) {
                            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) mFragment.getActivity();
                            targetView = chooseFileActivity.getSelectedView();
                        }
//                        AnimationUtils.setAddTaskAnimation(mFragment.getActivity(), startView, targetView, null);
                    }
                    notifyDataSetChanged();
                    displayPhotoAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    private void updateSelectedView() {
        if (getContext() != null && (getContext() instanceof ChooseFileActivity)) {
            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getContext();
            chooseFileActivity.getSelectedView();
        }
    }
}
