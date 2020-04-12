package com.thinkd.xshare.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

public class FileInfoRvMp4Adapter extends RecyclerView.Adapter<FileInfoRvMp4Adapter.Mp4ViewHolder>{
    public List<FileInfoBean> mlist;
    private int mType = FileInfo.TYPE_MP4;
    private Context mContext;
    private Fragment mFragment;
    DisplayMp4Adapter displayMp4Adapter;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    static class Mp4ViewHolder extends RecyclerView.ViewHolder{

        TextView tvTime;
        RecyclerView recyclerView;

        public Mp4ViewHolder(View view){
            super(view);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            recyclerView = (RecyclerView) view.findViewById(R.id.rv_file);
        }

    }

    public FileInfoRvMp4Adapter(List<FileInfoBean> list, int type,Context context,Fragment fragment){
        this.mlist = list;
        this.mType = type;
        this.mContext = context;
        this.mFragment = fragment;
    }

    @Override
    public FileInfoRvMp4Adapter.Mp4ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_show_file,null,false);
        Mp4ViewHolder holder = new Mp4ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final FileInfoRvMp4Adapter.Mp4ViewHolder holder, int position) {
        final FileInfoBean fileInfoBean = mlist.get(position);
        //转换日期格式
        String strDate = conversionTime(mContext,fileInfoBean.getFileDate());
        holder.tvTime.setText(strDate);
        displayMp4Adapter = new DisplayMp4Adapter(fileInfoBean.getFileInfoList(),mType,mContext);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        holder.recyclerView.setAdapter(displayMp4Adapter);
        if(displayMp4Adapter!=null){
            displayMp4Adapter.setOnItemClickListener(new DisplayMp4Adapter.OnItemClickListener() {
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
                    displayMp4Adapter.notifyDataSetChanged();
                }
            });
        }

//        if(mType ==  FileInfo.TYPE_MP4){
//
//            if(mlist != null && mlist.get(position) != null){
//
//                holder.iv_shortcut.setImageBitmap(fileInfo.getBitmap());
//
//                //全局变量是否存在FileInfo
//                if(AppContext.getAppContext().isExist(fileInfo)){
//                    holder.iv_ok_tick.setVisibility(View.VISIBLE);
//                }else{
//                    holder.iv_ok_tick.setVisibility(View.INVISIBLE);
//                }
//
//                if (mOnItemClickListener != null) {
//                    holder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int pos = holder.getLayoutPosition();
//                            mOnItemClickListener.onItemClick(v,pos);
//                        }
//                    });
//                }
//            }
//        }

    }

    @Override
    public int getItemCount() {
        return mlist == null ? 0 : mlist.size();
    }

    private void updateSelectedView() {
        if (getContext() != null && (getContext() instanceof ChooseFileActivity)) {
            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getContext();
            chooseFileActivity.getSelectedView();
        }
    }
}
