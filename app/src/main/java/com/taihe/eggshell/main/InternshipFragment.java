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

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.httprequest.MSCJSONObject;
import com.taihe.eggshell.base.utils.httprequest.MSCOpenUrlRunnable;
import com.taihe.eggshell.base.utils.httprequest.MSCPostUrlParam;
import com.taihe.eggshell.base.utils.httprequest.MSCUrlManager;
import com.taihe.eggshell.main.adapter.VideoAdapterHead;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.main.adapter.VideoAdapterGride;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.MyGridView;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternshipFragment extends Fragment implements View.OnClickListener{
    private LinearLayout lin_back,lin_head_video,id_lin_more,lin_fragment_top1,lin_fragment_top2,lin_fragment_video;
    private ImageView img_fragment_top1,img_fragment_top2;
    private TextView id_title,txt_fragment_top1,txt_fragment_top2;
    private MyGridView videoGrideView;
    private VideoAdapterGride videoAdapter;
    private  GridView grid_head_video;
    private int viewwidth1 = 0,index=0;
    private ScrollView scroll_hotnotes;
    List<VideoInfoMode> listInfo,listTopInfo;
    int pagesize=6,page=1;
    private ProgressBar progressBar;
    private LoadingProgressDialog loading;
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
        listTopInfo = new ArrayList<VideoInfoMode>();
        id_lin_more.setOnClickListener(this);
        lin_fragment_top1.setOnClickListener(this);
        lin_fragment_top2.setOnClickListener(this);
        lin_fragment_video.setVisibility(View.GONE);
        Display dw = getActivity().getWindowManager().getDefaultDisplay();
        viewwidth1 = dw.getWidth();
        loading = new LoadingProgressDialog(getActivity(),"正在请求...");
        // 滑动加载
        scroll_hotnotes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
//                        System.out.print("ACTION_DOWN:"+index);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        index++;
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
                            listInfo.clear();
                            page = 1;
                        } else {
                            page++;
                        }
                        id_lin_more.setVisibility(View.VISIBLE);
                        updataUI();
                    }
                }
                return false;
            }
        });
    }

   void initData(){
       updataUI();
   }
    public void updataUI(){
    //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            JSONObject jo = jsonObject.getJSONObject("data");
                            JSONArray j1 = jo.getJSONArray("list");
                            JSONArray jt = jo.getJSONArray("teacher");
                            JSONObject j2;
                            VideoInfoMode vMode;
                            int tmpImg = R.drawable.video_top1;
                            for (int i = 0; i < jt.length(); i++) {
                                vMode = new VideoInfoMode();
                                j2 = jt.getJSONObject(i);
                                vMode.setId(j2.optString("id").toString());
                                vMode.setC_id(j2.optString("c_id").toString());
                                vMode.setVideo_id(j2.optString("video_id").toString());
                                vMode.setVideo_name(j2.optString("video_name").toString());
                                vMode.setVideo_hour(j2.optString("video_hour").toString());
                                vMode.setVideo_teacher(j2.optString("video_teacher").toString());
                                vMode.setVideo_about(j2.optString("video_about").toString());
                                vMode.setVideo_obvious(j2.optString("video_obvious").toString());
                                vMode.setStatus(j2.optString("status").toString());
                                vMode.setVimage((tmpImg+i)+"");
                                vMode.setPlist(j2.optString("plist").toString());
                                listTopInfo.add(vMode);
                            }
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
                                vMode.setPlist(j2.optString("plist").toString());
                                listInfo.add(vMode);
                            }
                            if(j1.length()<1 && page>1){
                                page--;
                            }
                            videoAdapter.setVideoData(listInfo);
                            videoGrideView.setAdapter(videoAdapter);
                            //更新头部名师风采
                            VideoAdapterHead headAdapter = new VideoAdapterHead(getActivity());
                            headAdapter.setVideoData(listTopInfo);
                            grid_head_video.setAdapter(headAdapter);

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {
                        String msg = jsonObject.getString("msg");
//                        ToastUtils.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                loading.dismiss();
                ToastUtils.show(getActivity(), "网络异常");
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };

        Map<String,String> map = new HashMap<String,String>();
//        map.put("pagesize",""+pagesize);
//        map.put("page",""+page);
        String sWhere="";
        sWhere="?page="+page+"&pagesize="+pagesize;
        String url = Urls.VIDEO_LIST_URL+sWhere;
        RequestUtils.createRequest(getActivity(), url, "", true, map, true, listener, errorListener);
    }
    public void updataUIFL(){

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_lin_more:
                loading.show();
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
                loading.show();
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
                loading.show();
                page=1;
                updataUIFL();
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }
}