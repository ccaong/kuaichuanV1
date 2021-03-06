package com.thinkd.xshare.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bestgo.adsplugin.ads.AdAppHelper;
import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.DisplayPhotoAdapter;
import com.thinkd.xshare.adapter.bean.FileInfoBean;
import com.thinkd.xshare.base.BaseActivity;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.thinkd.xshare.base.App.addFileInfoToDel;
import static com.thinkd.xshare.base.App.clearFileInfoToDel;
import static com.thinkd.xshare.base.App.delFileInfoToDel;
import static com.thinkd.xshare.base.App.getDelFileInfoMap;
import static com.thinkd.xshare.base.App.isDelExist;
import static com.thinkd.xshare.util.FileUtils.conversionTime;
import static com.thinkd.xshare.util.FileUtils.groupByTimeToList;

public class PhotoHistoryActivity extends BaseActivity {

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fl_guanggao)
    FrameLayout flGuanggao;

    public DisplayPhotoAdapter displayPhotoAdapter;

    //图片路径
    private String mPathname = FileUtils.getRootDirPath() + "jpg";
    private List<FileInfoBean> mFileInfoBeans = new ArrayList<>();
    private List<FileInfo> mFileInfos = new ArrayList<>();
    private List<File> mFiles = new ArrayList<>();
    private CurrentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_history);
        ButterKnife.bind(this);
        Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览", "photo页面");

        //native广告
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        AdAppHelper.getInstance(getApplicationContext()).getNative(0, flGuanggao, layoutParams);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle.setText("");
        getSupportActionBar().setTitle(getResources().getString(R.string.photo_title));

        //获取照片，然后把照片转换成FileInfo类型
        initData();
        mFileInfoBeans = groupByTimeToList(mFileInfos);

        //设置适配器
        mAdapter = new CurrentAdapter(this, mFiles, mFileInfoBeans);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        //删除按钮的点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //提示是否删除选中的文件
                showDialog();
                Firebase.getInstance(getApplicationContext()).logEvent("photo页面", "删除","点击");
                return false;
            }
        });

    }

    /**
     * 获取图片
     */
    private void initData() {
        File[] files = new File(mPathname).listFiles();
        getByFileName(files);
    }

    //获取图片的信息
    private String getByFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    getByFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                    if (FileUtils.isJpgFile(fileName)) {
                        File eachJpg = new File(mPathname + "/" + fileName);

                        FileInfo fileInfo = new FileInfo();
                        String strDate = null;
                        long size = 0;
                        //获取时间
                        Date date = new Date(eachJpg.lastModified());
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        strDate = sdf.format(date);

                        //获取图片的大小
                        size = eachJpg.length();

                        fileInfo.setFilePath(eachJpg.getPath());
                        fileInfo.setDate(strDate);
                        fileInfo.setSize(size);

                        mFiles.add(eachJpg);
                        mFileInfos.add(fileInfo);

                        str += fileName.substring(0, fileName.lastIndexOf(".")) + "\n";
                    }
                }
            }
        }
        return str;
    }

    //设置item点击监听事件
    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    //适配器
    class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

        private List<File> mDatas;
        public List<FileInfoBean> mListFileInfoBean;
        public ArrayList<DisplayPhotoAdapter> mAdapterList;
        private LayoutInflater mInflater;
        private Context mContext;

        private OnItemClickListener mOnItemClickListener;

        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            this.mOnItemClickListener = itemClickListener;
        }

        public CurrentAdapter(Context context, List<File> datas, List<FileInfoBean> fileInfoBeans) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mDatas = datas;
            mListFileInfoBean = fileInfoBeans;
            mAdapterList = new ArrayList<>();
        }

        public void deleteFile(FileInfo fileInfo) {

            for (int i = 0; i < mAdapterList.size(); i++) {
                if (mAdapterList.get(i).deleteSelectedFile(fileInfo)) {
                    if (mAdapterList.get(i).isEmpty()) {
                        if(i<mListFileInfoBean.size()){
                            mListFileInfoBean.remove(i);
                        }
                    }
                    break;
                }
            }
        }


        @Override
        public CurrentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CurrentViewHolder holder = new CurrentViewHolder(mInflater.inflate(R.layout.item_show_file, null, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final CurrentViewHolder holder, int position) {
            final FileInfoBean fileInfoBean = mListFileInfoBean.get(position);
            //转换日期格式
            String strDate = conversionTime(mContext,fileInfoBean.getFileDate());
            holder.tvTime.setText(strDate);

            displayPhotoAdapter = new DisplayPhotoAdapter(fileInfoBean.getFileInfoList(), FileInfo.TYPE_JPG, mContext);
            mAdapterList.add(displayPhotoAdapter);

            LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 4);
            linearLayoutManager.setAutoMeasureEnabled(true);

            holder.recyclerView.setLayoutManager(linearLayoutManager);
            holder.recyclerView.setAdapter(displayPhotoAdapter);

            if (displayPhotoAdapter != null) {
                displayPhotoAdapter.setOnItemClickListener(new DisplayPhotoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        FileInfo fileInfo = fileInfoBean.getFileInfoList().get(position);

                        if (getDelFileInfoMap().size() > 0) {
                            //选择待删除的文件
                            if (isDelExist(fileInfo)) {
                                //文件已经被选中，再次点击撤销选中
                                delFileInfoToDel(fileInfo);
                                if (getDelFileInfoMap().size() == 0) {
                                    //隐藏删除的按钮
                                    toolbar.getMenu().clear();
                                }
                            } else {
                                //选中文件
                                addFileInfoToDel(fileInfo);
                            }
                            notifyDataSetChanged();
                            displayPhotoAdapter.notifyDataSetChanged();
                        } else {
                            toolbar.getMenu().clear();
                            FileUtils.openFile(mContext, FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                            Firebase.getInstance(getApplicationContext()).logEvent("photo页面", "打开","点击");

                            Log.e("图片的路径",fileInfo.getFilePath());
                            Log.e("图片的路径>>", FileUtils.getLocalFilePath(fileInfo.getFilePath()));
                        }

                    }
                });

                //长按选中文件
                displayPhotoAdapter.setOnItemLongClickListener(new DisplayPhotoAdapter.OnItemLongClickListener() {
                    @Override
                    public void onItemLongClick(View view, int position) {
                        //显示状态的改变
                        FileInfo fileInfo = fileInfoBean.getFileInfoList().get(position);
                        addFileInfoToDel(fileInfo);
                        notifyDataSetChanged();
                        displayPhotoAdapter.notifyDataSetChanged();
                        //显示删除按钮
                        if (toolbar.getMenu().size() == 0) {
                            toolbar.inflateMenu(R.menu.toobardel);
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mListFileInfoBean.size();
        }

        public class CurrentViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.tv_time)
            TextView tvTime;
            @Bind(R.id.rv_file)
            RecyclerView recyclerView;

            public CurrentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearFileInfoToDel();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toobardel, menu);
//        if(!check){
//            MenuItem item = menu.findItem(R.id.delete);
//            item.setVisible(false);
//        }
//        return true;
//    }

    //返回页面
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                clearFileInfoToDel();
                finish();
                return true;
//            case R.id.delete:
//                if (getDelFileInfoMap().size() > 0) {
//                    showDialog();
//                } else {
//                    Toast.makeText(this, getResources().getString(R.string.select_the_file_to_delete), Toast.LENGTH_SHORT).show();
//                }
//                displayPhotoAdapter.notifyDataSetChanged();
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder
                .setMessage(getResources().getString(R.string.delete_files))
                .setNegativeButton(getResources().getString(R.string.prompt_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        return;
                    }
                }).setPositiveButton((getResources().getString(R.string.prompt_yes)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //删除文件
                delFile();
                getDelFileInfoMap().clear();
                toolbar.getMenu().clear();
                dialog.dismiss();
            }
        }).show();
    }


    //删除选中的文件
    public void delFile() {

        if (getDelFileInfoMap().size() > 0) {
            for (String key : getDelFileInfoMap().keySet()) {
                FileInfo fileInfo = getDelFileInfoMap().get(key);
                File file = new File(fileInfo.getFilePath());
                if (file == null || !file.exists() || file.isDirectory()) {
                    Log.e("删除照片","删除失败");
                    Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                } else {
                    file.delete();
                    mAdapter.deleteFile(fileInfo);
                    mAdapter.notifyDataSetChanged();
                    displayPhotoAdapter.notifyDataSetChanged();
                }
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.select_the_file_to_delete), Toast.LENGTH_SHORT).show();
            Log.e("刪除照片","出现了未知的错误");
        }

    }

}
