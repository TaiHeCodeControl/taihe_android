package com.taihe.eggshell.main;

import android.content.Context;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.main.adapter.VideoListAdapter;

/**
 * Created by Thinkpad on 2015/7/23.
 */
public class VideoListActivity extends BaseActivity{

    private static final String TAG = "VideoListActivity";
    private Context mContext;
    private PullToRefreshListView videoListView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_video_list);
        mContext = this;
        super.initView();

        videoListView = (PullToRefreshListView)findViewById(R.id.id_video_listview);
        videoListView.setMode(PullToRefreshBase.Mode.BOTH);


    }

    @Override
    public void initData() {
        super.initData();

        VideoListAdapter videoListAdapter = new VideoListAdapter(mContext);
        videoListView.setAdapter(videoListAdapter);
        videoListAdapter.notifyDataSetChanged();
    }


}
