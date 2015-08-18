package com.taihe.eggshell.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.httprequest.MSCJSONObject;
import com.taihe.eggshell.base.utils.httprequest.MSCOpenUrlRunnable;
import com.taihe.eggshell.base.utils.httprequest.MSCPostUrlParam;
import com.taihe.eggshell.base.utils.httprequest.MSCUrlManager;
import com.taihe.eggshell.main.adapter.PlayAdapter;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyFragment extends Fragment implements View.OnClickListener{
    private TextView id_title;
    private LinearLayout lin_back;
    private Context context;
    private PullToRefreshGridView playView;
    private PlayAdapter playAdapter;
    int limit=2,page=1;
    List<PlayInfoMode> list;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 100:
                    try{
                        playView.isSelected();
                        JSONArray j1 = new JSONArray(msg.obj.toString());
                        JSONObject j2;
                        for(int i=0;i<j1.length();i++){
                            PlayInfoMode vMode = new PlayInfoMode();
                            j2 = j1.getJSONObject(i);
                            vMode.setId(j2.optString("id").toString());
                            vMode.setTitle(j2.optString("title").toString());
                            vMode.setAddress(j2.optString("address").toString());
                            vMode.setOrganizers(j2.optString("organizers").toString());
                            vMode.setEvery_time(j2.optString("every_time").toString());
                            vMode.setUser(j2.optString("user").toString());
                            vMode.setTelphone(j2.optString("telphone").toString());
                            vMode.setTraffic_route(j2.optString("traffic_route").toString());
                            vMode.setLogo(j2.optString("logo").toString());
                            vMode.setContent(j2.optString("content").toString());
                            vMode.setStarttime(j2.optString("starttime").toString());
                            vMode.setEndtime(j2.optString("endtime").toString());
                            list.add(vMode);
                        }
                        playAdapter.setPlayData(list);
                        playView.setAdapter(playAdapter);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    break;
            }
        }
    };
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_around, null) ;
        lin_back = (LinearLayout)v.findViewById(R.id.lin_back);
        id_title = (TextView) v.findViewById(R.id.id_title);
        playView = (PullToRefreshGridView) v.findViewById(R.id.id_video_listview);
        init();
        initData();
		return v;
	}

	public void init(){
        id_title.setText("玩出范");
        lin_back.setVisibility(View.GONE);
        playView.setMode(PullToRefreshBase.Mode.BOTH);
        playAdapter = new PlayAdapter(getActivity());
        list = new ArrayList<PlayInfoMode>();
        playView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page=1;
                list.clear();
                getListData();
                playView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                getListData();
                playView.onRefreshComplete();
            }
        });
	}

    public void initData(){
//        List<PlayInfoMode> list = new ArrayList<PlayInfoMode>();
//        for(int i=0;i<10;i++) {
//            PlayInfoMode vMode = new PlayInfoMode();
//            vMode.setTitle("哈哈"+(i+1));
//            list.add(vMode);
//        }
//        playAdapter.setPlayData(list);
//        playView.setAdapter(playAdapter);
//        playView.onRefreshComplete();

        getListData();
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()){

		}
	}

    private void getListData() {

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    System.out.println("code=========" + code);
                    if (code == 1) {
                        String data = jsonObject.getString("data");
                        Message msg = new Message();
                        msg.what=100;
                        msg.obj=data;
                        mHandler.sendMessage(msg);
                        Log.e("data",data);
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
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };

        Map<String,String> map = new HashMap<String,String>();
//        map.put("m","act");
//        map.put("c","list");
//        map.put("tokey","740c957a");
        map.put("limit",""+limit);
        map.put("page",""+page);

        String url = "http://195.198.1.122:8066/eggker/phpyun/api/admin/index.php?m=act&c=list";
        RequestUtils.createRequest(getActivity(), url, "", true, map, true, listener, errorListener);
    }

}