package com.thinkd.xshare.ui.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thinkd.xshare.R;
import com.thinkd.xshare.entity.MusicEntity;
import com.thinkd.xshare.util.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MusicHistoryActivity extends AppCompatActivity {

    @Bind(R.id.rv_sticky) RecyclerView mRvSticky;
    @Bind(R.id.header_view) RelativeLayout mHeaderView;
    @Bind(R.id.header_textview) TextView mHeaderTextView;
    @Bind(R.id.activity_main) RelativeLayout mActivityMain;

    //RecyclerView第一个item，肯定要展示StickyLayout的
    public static final int FIRST_STICKY_VIEW = 1;
    //RecyclerView除了第一个item意外，要展示StickyLayout的
    public static final int HAS_STICKY_VIEW = 2;
    //RecyclerView不展示StickyLayout的item。
    public static final int NONE_STICKY_VIEW = 3;

    //music路径
    private String mPathname = FileUtils.getRootDirPath() + "mp3";

    //数据集
    private List<MusicEntity> mDatas = new ArrayList<>();
    private CurrentAdapter mAdapter;

    //统计数量
    private Map<String, Integer> dateMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_history);
        ButterKnife.bind(this);

        initData();

        initRecyclerView();

        initEvent();

    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                FileUtils.openFile(MusicHistoryActivity.this, FileUtils.getLocalFilePath(mDatas.get(pos).getFile().getPath()));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobar, menu);
        return true;
    }

    private void initRecyclerView() {

        Collections.sort(mDatas, new MusicComparator());
        mAdapter = new CurrentAdapter(this, mDatas);

        mRvSticky.setLayoutManager(new LinearLayoutManager(this));
        mRvSticky.setAdapter(mAdapter);

        mRvSticky.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*
                找到RecyclerView的item中，和RecyclerVIew的getTop()向下相距5个元素那个item
                尝试2、3个像素都找不到结果，就用了5
                根据这个item 来更新吸顶布局的内容
                 */
                View stickyInfoView = recyclerView.findChildViewUnder(mHeaderView.getMeasuredWidth() / 2, 5);

                if (stickyInfoView != null && stickyInfoView.getContentDescription() != null) {
                    mHeaderTextView.setText(String.valueOf(stickyInfoView.getContentDescription()));
                }

                /*
                找到固定再屏幕上方哪个StickyLayout下面一个像素位的RecyclerVIew的item
                根据这个item来更新要translate多少距离
                并且只处理HAS_STICKY_VIEW和NONE_STICKY_VIEW这俩种tag
                因为第一个item的StickyLayout虽然展示，但是不会引起滚动
                 */
                View transInfoView = recyclerView.findChildViewUnder(mHeaderView.getMeasuredWidth() / 2, mHeaderView.getMeasuredHeight() + 1);

                if (transInfoView != null && transInfoView.getTag() != null) {
                    int transViewStatus = (int) transInfoView.getTag();
                    int dealtY = transInfoView.getTop() - mHeaderView.getMeasuredHeight();

                    /*
                    如果当前item需要展示StickyLayout
                    那么根据这个item的getTop和StickyLayout的高度相差的距离来滚动StickyLayout
                    这里有一处 注意 如果这个item的getTop已经小雨0 也就是滚出了屏幕
                    那么我就把假的StickyLayout恢复原味，来覆盖住这个item对应的吸顶信息。
                     */
                    if (transViewStatus == HAS_STICKY_VIEW) {
                        if (transInfoView.getTop() > 0) {
                            mHeaderView.setTranslationY(dealtY);
                        } else {
                            mHeaderView.setTranslationY(0);
                        }
                    } else if (transViewStatus == NONE_STICKY_VIEW) {
                        mHeaderView.setTranslationY(0);
                    }
                }
            }
        });
    }

    /**
     * 排序
     */
    class MusicComparator implements Comparator<MusicEntity> {
        @Override
        public int compare(MusicEntity o1, MusicEntity o2) {
            if (o1.getFile().lastModified() > o2.getFile().lastModified())
                return -1;
            return 1;
        }
    }

    /**
     * 获取图片
     */
    private void initData() {
        File[] files = new File(mPathname).listFiles();
        getByFileName(files);
        getDateMap();
    }

    /**
     * 统计对应日期的个数
     */
    private void getDateMap() {
        List<String> sortList = new ArrayList<>();
        for (MusicEntity entity : mDatas) {
            sortList.add(entity.getDate());
        }
        Collections.sort(sortList);
        int count = 1;
        for (String str : sortList) {
            if (!dateMap.containsKey(str)) {
                count = 0;
                dateMap.put(str, count);
            }
            if (dateMap.containsKey(str)) {
                count++;
                dateMap.put(str, count);
            }
        }
    }

    /**
     * 返回日期对应的个数
     *
     * @return
     */
    private String getDateCount(String key) {
        return " (" + dateMap.get(key) + ")";
    }

    private String getByFileName(File[] files) {
        String str = "";
        if (files != null) { // 先判断目录是否为空，否则会报空指针
            for (File file : files) {
                if (file.isDirectory()) {//检查此路径名的文件是否是一个目录(文件夹)
                    getByFileName(file.listFiles());
                } else {
                    String fileName = file.getName();
                    if (FileUtils.isMp3File(fileName)) {
                        String date = new SimpleDateFormat("yyyy-MM-dd")
                                .format(new Date(file.lastModified()));
                        MusicEntity musicEntity = new MusicEntity(date, file, false);
                        mDatas.add(musicEntity);

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

    class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.CurrentViewHolder> {

        private List<MusicEntity> mDatas;
        private LayoutInflater mInflater;
        private Context mContext;
        private OnItemClickListener mOnItemClickListener;

        public CurrentAdapter(Context context, List<MusicEntity> datas) {
            mContext = context;
            mDatas = datas;
            mInflater = LayoutInflater.from(context);
        }

        public void setOnItemClickListener(OnItemClickListener itemClickListener) {
            this.mOnItemClickListener = itemClickListener;
        }

        @Override
        public CurrentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CurrentViewHolder holder = new CurrentViewHolder(mInflater.inflate(R.layout.item_music, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final CurrentViewHolder holder, int position) {
            holder.mIvShortcut.setImageResource(R.mipmap.icon_mp3);
            MusicEntity musicEntity = mDatas.get(position);
            String time = musicEntity.getDate();
            File file = musicEntity.getFile();

            holder.mIvShortcut.setTag(file.getPath());
            ImgTask imgTask = new ImgTask(holder.mIvShortcut, file.getPath());
            imgTask.execute(file.getPath());

            holder.mTvName.setText(file.getName());
            holder.mTvSize.setText(FileUtils.showLongFileSzie(file.length()));
            holder.mIvOkTick.setVisibility(View.GONE);

            if (position == 0) {
                holder.mHeaderView.setVisibility(View.VISIBLE);
                holder.mHeaderTextView.setText(time + getDateCount(time));
                //第一个itme的吸顶信息肯定是展示的，并且标记为tag为FIRST
                holder.itemView.setTag(FIRST_STICKY_VIEW);
            } else {
                //之后的item都会和前一个item要展示的吸顶信息进行比较，不相同就展示，并且标记tag为HAS_STICKY，
                if (!TextUtils.equals(musicEntity.getDate(), mDatas.get(position - 1).getDate())) {
                    holder.mHeaderView.setVisibility(View.VISIBLE);
                    holder.mHeaderTextView.setText(time + getDateCount(time));
                    holder.itemView.setTag(HAS_STICKY_VIEW);
                } else {
                    //相同的不展示，并且标记tag为NONE_STICKY
                    holder.mHeaderView.setVisibility(View.GONE);
                    holder.itemView.setTag(NONE_STICKY_VIEW);
                }
            }
            //ContentDescription 用来记录并获取要吸顶展示的信息。
            holder.itemView.setContentDescription(time + getDateCount(time));

            if (mOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        mOnItemClickListener.onItemClick(holder.itemView, pos);
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        class CurrentViewHolder extends RecyclerView.ViewHolder {
            @Bind(R.id.header_view) RelativeLayout mHeaderView;
            @Bind(R.id.header_textview) TextView mHeaderTextView;
            @Bind(R.id.iv_shortcut) ImageView mIvShortcut;
            @Bind(R.id.tv_name) TextView mTvName;
            @Bind(R.id.tv_size) TextView mTvSize;
            @Bind(R.id.iv_ok_tick) ImageView mIvOkTick;

            public CurrentViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        //异步加载专辑图片 设置path为了放置图片复用错位。
        class ImgTask extends AsyncTask<String, Void, Bitmap> {
            private ImageView iv;
            private String path;

            public ImgTask(ImageView iv, String path) {
                this.iv = iv;
                this.path = path;
            }

            @Override
            protected Bitmap doInBackground(String... param) {
                try {
                    String path = param[0];
                    Bitmap art = FileUtils.createAlbumArt(path);
                    if (art != null) {
                        return art;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (result != null && path.equals(iv.getTag())) {
                    iv.setImageBitmap(result);
                }
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
        }
    }
}
