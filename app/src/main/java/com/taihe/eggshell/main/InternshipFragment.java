package com.taihe.eggshell.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.httprequest.MSCJSONObject;
import com.taihe.eggshell.base.utils.httprequest.MSCOpenUrlRunnable;
import com.taihe.eggshell.base.utils.httprequest.MSCPostUrlParam;
import com.taihe.eggshell.base.utils.httprequest.MSCUrlManager;
import com.taihe.eggshell.main.adapter.VideoAdapterHead;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.main.adapter.VideoAdapterGride;
import com.taihe.eggshell.widget.MyGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InternshipFragment extends Fragment implements View.OnClickListener{
    private LinearLayout lin_back,lin_head_video,id_lin_more,lin_fragment_top1,lin_fragment_top2,lin_fragment_video;
    private ImageView img_fragment_top1,img_fragment_top2;
    private TextView id_title,txt_fragment_top1,txt_fragment_top2;
    private MyGridView videoGrideView;
    private VideoAdapterGride videoAdapter;
    private  GridView grid_head_video;
    private int viewwidth1 = 0,index=0;
    private ScrollView scroll_hotnotes;
    List<VideoInfoMode> listInfo;
    int pagesize=4,page=1;
    private ProgressBar progressBar;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.fragment_intertship, null) ;
        lin_back = (LinearLayout)v.findViewById(R.id.lin_back);
        lin_head_video = (LinearLayout)v.findViewById(R.id.lin_head_video);
        lin_fragment_top1 = (LinearLayout)v.findViewById(R.id.lin_fragment_top1);
        lin_fragment_top2 = (LinearLayout)v.findViewById(R.id.lin_fragment_top2);
        lin_fragment_video = (LinearLayout)v.findViewById(R.id.lin_fragment_video);
        id_title = (TextView) v.findViewById(R.id.id_title);
        txt_fragment_top1 = (TextView) v.findViewById(R.id.txt_fragment_top1);
        txt_fragment_top2 = (TextView) v.findViewById(R.id.txt_fragment_top2);
        img_fragment_top1 = (ImageView) v.findViewById(R.id.img_fragment_top1);
        img_fragment_top2 = (ImageView) v.findViewById(R.id.img_fragment_top2);
        id_lin_more = (LinearLayout) v.findViewById(R.id.id_lin_more);
        grid_head_video = (GridView) v.findViewById(R.id.grid_head_video);
        videoGrideView = (MyGridView) v.findViewById(R.id.id_video_grideview);
        scroll_hotnotes = (ScrollView) v.findViewById(R.id.scroll_hotnotes);
        progressBar = (ProgressBar) v.findViewById(R.id.loadingmore);

        init();
        initData();
        return v ;
    }
    void init(){
        id_title.setText("公开课");
        lin_back.setVisibility(View.GONE);
        videoAdapter = new VideoAdapterGride(getActivity());
        listInfo = new ArrayList<VideoInfoMode>();
        id_lin_more.setOnClickListener(this);
        lin_fragment_top1.setOnClickListener(this);
        lin_fragment_top2.setOnClickListener(this);
        lin_fragment_video.setVisibility(View.GONE);
        Display dw = getActivity().getWindowManager().getDefaultDisplay();
        viewwidth1 = dw.getWidth();

        // 滑动加载
        scroll_hotnotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        System.out.print("ACTION_DOWN:"+index);
                        Log.e("err","ACTION_DOWN:"+index);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        index++;
                        Log.e("err1","ACTION_DOWN:"+index);
                        break;
                    default:
                        break;
                }
                if (event.getAction() == MotionEvent.ACTION_UP && index > 0) {
                    index = 0;
                    View view = ((ScrollView) v).getChildAt(0);
                    if (view.getMeasuredHeight() <= v.getScrollY()
                            + v.getHeight()) {
                        // 加载数据代码
                        if (listInfo.size() <= 3) {
                            listInfo = new ArrayList<VideoInfoMode>();
                            page = 1;
                        } else {
                            page++;
                        }
                        Log.e("err","=====:"+page);
                        id_lin_more.setVisibility(View.VISIBLE);
                        updataUI();
                    }
                }
                return false;
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

       updataUI();
   }
    public void updataUI(){
        MSCUrlManager url = new MSCUrlManager("video","getPageList",pagesize,page);
        List<MSCPostUrlParam> list = new ArrayList<MSCPostUrlParam>();
        list.add(new MSCPostUrlParam("token", pagesize));
//        list.add(new MSCPostUrlParam("pagesize", pagesize));
//        list.add(new MSCPostUrlParam("page", page));
        new Thread(new MSCOpenUrlRunnable(url, list) {
            @Override
            public void onControl(MSCJSONObject jsonObject) throws JSONException {
                if (jsonObject.optString("code").equals("0")) {
                    JSONArray j1 = jsonObject.getJSONArray("data");
                    JSONObject j2;
                    VideoInfoMode vMode;
                    for(int i=0;i<j1.length();i++){
                        vMode = new VideoInfoMode();
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
                        listInfo.add(vMode);
                    }
                    if(j1.length()<1 && page>1){
                        page--;
                    }
                    videoAdapter.setVideoData(listInfo);
                    videoGrideView.setAdapter(videoAdapter);
//                    id_lin_more.setVisibility(View.INVISIBLE);
//                    videoAdapter.notifyDataSetChanged();
                } else {
//					Toast.makeText(getActivity(), jsonObject.length()+"false", Toast.LENGTH_LONG).show();
                }
                super.onControl(jsonObject);
            }
        }).start();
    }
    public void updataUIFL(){

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_lin_more:
                page++;
                updataUI();
                break;
            case R.id.lin_fragment_top1:
                listInfo.clear();
                scroll_hotnotes.setVisibility(View.VISIBLE);
                lin_fragment_video.setVisibility(View.GONE);
                img_fragment_top1.setBackgroundResource(R.drawable.shipin);
                img_fragment_top2.setBackgroundResource(R.drawable.yuyinck);
                txt_fragment_top1.setTextColor(getActivity().getResources().getColor(R.color.font_color_red));
                txt_fragment_top2.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
                updataUI();
                break;
            case R.id.lin_fragment_top2:
                scroll_hotnotes.setVisibility(View.GONE);
                lin_fragment_video.setVisibility(View.VISIBLE);
                img_fragment_top1.setBackgroundResource(R.drawable.shipinck);
                img_fragment_top2.setBackgroundResource(R.drawable.yuyin);
                txt_fragment_top2.setTextColor(getActivity().getResources().getColor(R.color.font_color_red));
                txt_fragment_top1.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
                updataUIFL();
                break;
        }
    }

}