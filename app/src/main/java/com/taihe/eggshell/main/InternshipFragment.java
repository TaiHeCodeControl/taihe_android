package com.taihe.eggshell.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.lsw.MSCJSONObject;
import com.taihe.eggshell.lsw.MSCOpenUrlRunnable;
import com.taihe.eggshell.lsw.MSCPostUrlParam;
import com.taihe.eggshell.lsw.MSCUrlManager;
import com.taihe.eggshell.main.adapter.VideoAdapterHead;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.main.adapter.VideoAdapterGride;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InternshipFragment extends Fragment implements View.OnClickListener{
    private LinearLayout lin_back,lin_head_video;
    private ImageView id_back;
    private TextView id_title;
    private PullToRefreshGridView videoGrideView;
    private VideoAdapterGride videoAdapter;
    private  GridView grid_head_video;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_intertship, null) ;
        lin_back = (LinearLayout)v.findViewById(R.id.lin_back);
        lin_head_video = (LinearLayout)v.findViewById(R.id.lin_head_video);
        id_title = (TextView) v.findViewById(R.id.id_title);
        grid_head_video = (GridView) v.findViewById(R.id.grid_head_video);

        videoGrideView = (PullToRefreshGridView) v.findViewById(R.id.id_video_grideview);
        init();
        initData();
        return v ;
    }
    void init(){
        id_title.setText("公开课");
        lin_back.setVisibility(View.GONE);
        videoGrideView.setMode(PullToRefreshBase.Mode.BOTH);
        final List<VideoInfoMode> vList = new ArrayList<VideoInfoMode>();

        videoAdapter = new VideoAdapterGride(getActivity());

        videoGrideView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                videoAdapter.setVideoData(vList);
                videoGrideView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
//                videoAdapter.setVideoData(vList);
                videoGrideView.onRefreshComplete();
            }
        });
    }

   void initData(){
       List<VideoInfoMode> listHead = new ArrayList<VideoInfoMode>();
       for (int i = 0; i <3 ; i++) {
           VideoInfoMode headMode = new VideoInfoMode();
           headMode.setVideo_teacher("老师"+(i+1));
           headMode.setVideo_name("物理特技教师" + (i + 1));
           headMode.setVimage("http://img.videocc.net/uimage/0/00018093b1/e/00018093b18897dc6d44dc97088d9dde_0.jpg");
           listHead.add(headMode);
       }
       VideoAdapterHead headAdapter = new VideoAdapterHead(getActivity());
       headAdapter.setVideoData(listHead);
       grid_head_video.setAdapter(headAdapter);

       MSCUrlManager url = new MSCUrlManager("video","getListImage");
       List<MSCPostUrlParam> list = new ArrayList<MSCPostUrlParam>();
       list.add(new MSCPostUrlParam("token", "pagenum"));
       new Thread(new MSCOpenUrlRunnable(url, list) {
           @Override
           public void onControl(MSCJSONObject jsonObject) throws JSONException {
               if (jsonObject.optString("code").equals("0")) {
                   JSONArray j1 = jsonObject.getJSONArray("data");
                   JSONObject j2;
                   List<VideoInfoMode> list = new ArrayList<VideoInfoMode>();
                   for(int i=0;i<j1.length();i++){
                       VideoInfoMode vMode = new VideoInfoMode();
                       j2 = j1.getJSONObject(i);
                       vMode.setId(j2.optString("id").toString());
                       vMode.setC_id(j2.optString("c_id").toString());
                       vMode.setVideo_id(j2.optString("video_id").toString());
                       vMode.setVideo_name(j2.optString("video_name").toString());
                       vMode.setVideo_hour(j2.optString("video_hour").toString());
                       vMode.setVideo_teacher(j2.optString("video_teacher").toString());
                       vMode.setVideo_about(j2.optString("video_about").toString());
                       vMode.setVideo_obvious(j2.optString("video_obvious").toString());
                       vMode.setStatus(j2.optString("status").toString());
                       vMode.setVimage(j2.optString("vimage").toString());
                       list.add(vMode);
                   }
                   videoAdapter.setVideoData(list);
                   videoGrideView.setAdapter(videoAdapter);
                   videoAdapter.notifyDataSetChanged();
                   videoGrideView.onRefreshComplete();
               } else {
//					Toast.makeText(getActivity(), jsonObject.length()+"false", Toast.LENGTH_LONG).show();
               }
               super.onControl(jsonObject);
           }
       }).start();
   }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}