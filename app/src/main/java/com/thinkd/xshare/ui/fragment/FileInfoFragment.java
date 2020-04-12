package com.thinkd.xshare.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.thinkd.xshare.R;
import com.thinkd.xshare.adapter.FileInfoRvAdapter;
import com.thinkd.xshare.adapter.FileInfoRvMp4Adapter;
import com.thinkd.xshare.adapter.FileInfoRvPhotoAdapter;
import com.thinkd.xshare.adapter.bean.FileInfoBean;
import com.thinkd.xshare.base.App;
import com.thinkd.xshare.entity.FileInfo;
import com.thinkd.xshare.ui.activity.ChooseFileActivity;
import com.thinkd.xshare.ui.activity.Firebase;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.thinkd.xshare.base.App.isExist;
import static com.thinkd.xshare.util.FileUtils.groupByTimeToList;

public class FileInfoFragment extends Fragment {

    RecyclerView gv;
    ProgressBar pb;

    private int mType = FileInfo.TYPE_APK;
    private List<FileInfo> mFileInfoList;
    private FileInfoRvAdapter mFileInfoAdapter;
    private FileInfoRvPhotoAdapter mFileInfoPhotoAdapter;
    private FileInfoRvMp4Adapter mFileInfoMp4Adapter;

    @SuppressLint("ValidFragment")
    public FileInfoFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FileInfoFragment(int type) {
        super();
        this.mType = type;
    }

    public static FileInfoFragment newInstance(int type) {
        FileInfoFragment fragment = new FileInfoFragment(type);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_apk, container, false);
        gv = (RecyclerView) rootView.findViewById(R.id.gv);
        pb = (ProgressBar) rootView.findViewById(R.id.pb);

        if (mType == FileInfo.TYPE_APK) { //应用
            gv.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }
//        else if (mType == FileInfo.TYPE_JPG) { //图片
//            gv.setLayoutManager(new GridLayoutManager(getContext(), 4));
//
//        }
////        else if(mType == FileInfo.TYPE_MP3){ //音乐
////            gv.setNumColumns(1);
////        }
//        else if (mType == FileInfo.TYPE_MP4) { //视频
//            gv.setLayoutManager(new GridLayoutManager(getContext(), 4));
//        }
        else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            linearLayoutManager.setAutoMeasureEnabled(true);
            gv.setLayoutManager(linearLayoutManager);
        }

        //Android6.0 requires android.permission.READ_EXTERNAL_STORAGE
        init();//初始化界面

        return rootView;
    }

    private void init() {

        if (mType == FileInfo.TYPE_APK) {
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_APK).executeOnExecutor(App.MAIN_EXECUTOR);
            Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览","文件选择界面","app");
        } else if (mType == FileInfo.TYPE_JPG) {
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_JPG).executeOnExecutor(App.MAIN_EXECUTOR);
            Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览","文件选择界面","picture");

        }
//        else if (mType == FileInfo.TYPE_MP3) {
//            new GetFileInfoListTask(getContext(), FileInfo.TYPE_MP3).executeOnExecutor(AppContext.MAIN_EXECUTOR);
//        }
        else if (mType == FileInfo.TYPE_MP4) {
            new GetFileInfoListTask(getContext(), FileInfo.TYPE_MP4).executeOnExecutor(App.MAIN_EXECUTOR);
            Firebase.getInstance(getApplicationContext()).logEvent("屏幕浏览","文件选择界面","video");

        }
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                Toast.makeText(FileInfoFragment.super.getActivity(), "选择了一个文件", Toast.LENGTH_SHORT).show();
//                FileInfo fileInfo = mFileInfoList.get(position);
//                if (AppContext.getAppContext().isExist(fileInfo)) {
//                    AppContext.getAppContext().delFileInfo(fileInfo);
//                    updateSelectedView();
//                } else {
//                    //1.添加任务
//                    AppContext.getAppContext().addFileInfo(fileInfo);
//                    //2.添加任务 动画
//                    View startView = null;
//                    View targetView = null;
//
//                    startView = view.findViewById(R.id.iv_shortcut);
//                    if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)) {
//                        ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
//                        targetView = chooseFileActivity.getSelectedView();
//                    }
//                    AnimationUtils.setAddTaskAnimation(getActivity(), startView, targetView, null);
//                }
//
//                mFileInfoAdapter.notifyDataSetChanged();
//            }
//        });
    }

    private void bindClick() {
        //点击事件
        if (mFileInfoAdapter != null) {
            mFileInfoAdapter.setOnItemClickListener(new FileInfoRvAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    FileInfo fileInfo = mFileInfoList.get(position);
                    if (isExist(fileInfo)) {
                        App.getAppContext().delFileInfo(fileInfo);
                        updateSelectedView();
                    } else {
                        //1.添加任务
                        App.getAppContext().addFileInfo(fileInfo);
                        //2.添加任务 动画
                        View startView = null;
                        View targetView = null;

                        startView = view.findViewById(R.id.iv_shortcut);
                        if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)) {
                            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
                            targetView = chooseFileActivity.getSelectedView();
                        }
//                        AnimationUtils.setAddTaskAnimation(getActivity(), startView, targetView, null);
                    }
                    mFileInfoAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onResume() {
        updateFileInfoAdapter();
        super.onResume();
    }

    /**
     * 更新FileInfoAdapter
     */
    public void updateFileInfoAdapter() {
        if (mFileInfoAdapter != null) {
            mFileInfoAdapter.notifyDataSetChanged();
        }
        if (mFileInfoPhotoAdapter != null) {
            mFileInfoPhotoAdapter.notifyDataSetChanged();
        }
        if (mFileInfoMp4Adapter != null) {
            mFileInfoMp4Adapter.notifyDataSetChanged();
        }
    }

    /**
     * 更新ChooseActivity选中View
     */
    private void updateSelectedView() {
        if (getActivity() != null && (getActivity() instanceof ChooseFileActivity)) {
            ChooseFileActivity chooseFileActivity = (ChooseFileActivity) getActivity();
            chooseFileActivity.getSelectedView();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 显示进度
     */
    public void showProgressBar() {
        if (pb != null) {
            pb.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏进度
     */
    public void hideProgressBar() {
        if (pb != null && pb.isShown()) {
            pb.setVisibility(View.GONE);
        }
    }

    /**
     * 获取ApkInfo列表任务
     */
    class GetFileInfoListTask extends AsyncTask<String, Integer, List<FileInfo>> {
        Context sContext = null;
        int sType = FileInfo.TYPE_APK;
        List<FileInfo> sFileInfoList = null;
        List<FileInfoBean> mFileInfoBeanList = null;

        public GetFileInfoListTask(Context sContext, int type) {
            this.sContext = sContext;
            this.sType = type;
        }

        @Override
        protected void onPreExecute() {
            showProgressBar();
            super.onPreExecute();
        }

        @Override
        protected List doInBackground(String... params) {
            //FileUtils.getSpecificTypeFiles 只获取FileInfo的属性 filePath与size
            if (sType == FileInfo.TYPE_APK) {
                //获取手机中的所有apk文件
                sFileInfoList = getAllApp();
                //设置apk的详细信息（apk名，apk大小，缩略图，类型）
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_APK);

                /**
                 * 增加MsgId
                 */
            } else if (sType == FileInfo.TYPE_JPG) {
                //获取手机中所有的图片文件
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_JPG, FileInfo.EXTEND_JPEG});

                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_JPG);
                // 把这个list中的fileInfo根据时间排序
                mFileInfoBeanList = groupByTimeToList(sFileInfoList);
            }
//            else if(sType == FileInfo.TYPE_MP3){
//                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{ FileInfo.EXTEND_MP3});
//                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_MP3);
//            }
            //TODO
            else if (sType == FileInfo.TYPE_MP4) {
                //获取手机中所有的视频文件
                sFileInfoList = FileUtils.getSpecificTypeFiles(sContext, new String[]{FileInfo.EXTEND_MP4});
                //设置视频的详细信息（视频名，视频大小，缩略图，类型）
                sFileInfoList = FileUtils.getDetailFileInfos(sContext, sFileInfoList, FileInfo.TYPE_MP4);
                // 把这个list中的fileInfo根据时间排序
                mFileInfoBeanList = groupByTimeToList(sFileInfoList);
            }
            mFileInfoList = sFileInfoList;
            return sFileInfoList;
        }

        @Override
        protected void onPostExecute(List<FileInfo> list) {
            hideProgressBar();
            if (sFileInfoList != null && sFileInfoList.size() > 0) {

                if (mType == FileInfo.TYPE_APK) { //应用
                    mFileInfoAdapter = new FileInfoRvAdapter(sFileInfoList, FileInfo.TYPE_APK);
                    gv.setAdapter(mFileInfoAdapter);
                } else if (mType == FileInfo.TYPE_JPG) { //图片
                    mFileInfoPhotoAdapter = new FileInfoRvPhotoAdapter(mFileInfoBeanList,
                            FileInfo.TYPE_JPG, FileInfoFragment.this.getActivity(), FileInfoFragment.this);
                    gv.setAdapter(mFileInfoPhotoAdapter);
                }
//                else if(mType == FileInfo.TYPE_MP3){ //音乐
//                    mFileInfoAdapter = new FileInfoAdapter(sContext, sFileInfoList, FileInfo.TYPE_MP3);
//                    gv.setAdapter(mFileInfoAdapter);
//                }
                else if (mType == FileInfo.TYPE_MP4) { //视频
                    mFileInfoMp4Adapter = new FileInfoRvMp4Adapter(mFileInfoBeanList,
                            FileInfo.TYPE_MP4, FileInfoFragment.this.getActivity(), FileInfoFragment.this);
                    gv.setAdapter(mFileInfoMp4Adapter);
                }
                bindClick();
            } else {
                Toast.makeText(FileInfoFragment.super.getActivity(), "暂时找不到应用信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //获取手机中的App信息
    public List<FileInfo> getAllApp() {
        List<FileInfo> appFileInfoList = new ArrayList<>();
        List<ApplicationInfo> apps = FileInfoFragment.super.getActivity().getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                //非系统应用
                FileInfo fileInfo = new FileInfo();
                //app的路径
                String appPath = app.sourceDir;
                //获取应用名称
                String appName = FileInfoFragment.super.getActivity().getPackageManager().getApplicationLabel(app).toString();
                //获取大小
                File appFile = new File(appPath);
                long appSize = appFile.length();

                //获取应用图标
//                Drawable appIcon = FileInfoFragment.super.getActivity().getPackageManager().getApplicationIcon(app);
                fileInfo.setFilePath(appPath);
                fileInfo.setName(appName);
                fileInfo.setFileType(FileInfo.TYPE_APK);
                fileInfo.setSize(appSize);
                appFileInfoList.add(fileInfo);
            }
        }

        return appFileInfoList;
    }

}
