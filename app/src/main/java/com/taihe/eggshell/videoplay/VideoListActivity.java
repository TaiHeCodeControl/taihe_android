package com.taihe.eggshell.videoplay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;

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

        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext,VideoPlayActivity.class));
            }
        });
    }


}
