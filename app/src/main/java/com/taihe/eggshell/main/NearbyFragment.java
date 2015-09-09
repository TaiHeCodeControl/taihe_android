package com.taihe.eggshell.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.adapter.PlayAdapter;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyFragment extends Fragment implements View.OnClickListener{
    private TextView id_title,txt_around_tag1,txt_around_tag2;
    private LinearLayout lin_back,lin_around_tag1,lin_around_tag2,id_linear_listview_tag;
    private Context context;
    private PullToRefreshGridView playView;
    private PlayAdapter playAdapter;
    private ImageView img_around_tag1,img_around_tag2;
    int limit=5,page=1,type=1;
    List<PlayInfoMode> list;
    private LoadingProgressDialog loading;
	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_around, null) ;
        lin_back = (LinearLayout)v.findViewById(R.id.lin_back);
        id_linear_listview_tag = (LinearLayout)v.findViewById(R.id.id_linear_listview_tag);
        lin_around_tag1 = (LinearLayout)v.findViewById(R.id.lin_around_tag1);
        lin_around_tag2 = (LinearLayout)v.findViewById(R.id.lin_around_tag2);
        id_title = (TextView) v.findViewById(R.id.id_title);
        txt_around_tag1 = (TextView) v.findViewById(R.id.txt_around_tag1);
        txt_around_tag2 = (TextView) v.findViewById(R.id.txt_around_tag2);
        img_around_tag1 = (ImageView) v.findViewById(R.id.img_around_tag1);
        img_around_tag2 = (ImageView) v.findViewById(R.id.img_around_tag2);
        playView = (PullToRefreshGridView) v.findViewById(R.id.id_video_listview);
        init();
        initData();
		return v;
	}

	public void init(){
        id_title.setText("玩出范");
        lin_back.setVisibility(View.GONE);
        playView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        playAdapter = new PlayAdapter(getActivity());
        list = new ArrayList<PlayInfoMode>();
        lin_around_tag1.setOnClickListener(this);
        lin_around_tag2.setOnClickListener(this);
        loading = new LoadingProgressDialog(getActivity(),"正在请求...");
        playView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                loading.show();
                page=1;
                list.clear();
                getListData();
                playView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                loading.show();
                page++;
                getListData();
                playView.onRefreshComplete();
//                playView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        playView.scrollTo(0, id_linear_listview_tag.getMeasuredHeight() - playView.getHeight());
//                    }
//                });
            }
        });
	}

    public void initData(){
        getListData();
    }

	@Override
	public void onClick(View view) {
		switch (view.getId()){
            case R.id.lin_around_tag1:
                list.clear();
                type=1;
                loading.show();
                img_around_tag1.setBackgroundResource(R.drawable.high);
                img_around_tag2.setBackgroundResource(R.drawable.fulick);
                txt_around_tag1.setTextColor(getActivity().getResources().getColor(R.color.font_color_red));
                txt_around_tag2.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
                playView.setVisibility(View.VISIBLE);
                getListData();
                playView.onRefreshComplete();
                break;
            case R.id.lin_around_tag2:
                list.clear();
                type=2;
                loading.show();
                img_around_tag1.setBackgroundResource(R.drawable.highck);
                img_around_tag2.setBackgroundResource(R.drawable.fuli);
                txt_around_tag2.setTextColor(getActivity().getResources().getColor(R.color.font_color_red));
                txt_around_tag1.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
//                playView.setVisibility(View.GONE);
                getListData();
                playView.onRefreshComplete();
                break;
		}
	}

    private void getListData() {

        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    loading.dismiss();
                    JSONObject jsonObject = new JSONObject((String) obj);
//                    Log.e("data", jsonObject.toString());
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        try{
                            playView.isSelected();
                            playView.setSelection(list.size()-1);
                            JSONArray j1 = new JSONArray(data);
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
                            playView.setVisibility(View.VISIBLE);
                            playAdapter.setPlayData(list,type);
                            playView.setAdapter(playAdapter);
                            if(list.size()==0){
                                playView.removeAllViews();
                            }

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                       // Log.e("data",data);
                    } else {
                        if(list.size()==0) {
                            playView.setVisibility(View.GONE);
                        }
                        //String msg = jsonObject.getString("message");
//                        ToastUtils.show(getActivity(), "网络连接异常");
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
                ToastUtils.show(getActivity(), volleyError.networkResponse.statusCode + "网络错误");
//                    String err = new String(volleyError.networkResponse.data);
//                    volleyError.networkResponse.statusCode;
            }
        };
        Map<String,String> map = new HashMap<String,String>();
        map.put("type",""+type);
        map.put("limit",""+limit);
        map.put("page",""+page);
        String sWhere="";
//        sWhere="type="+type+"&page="+page+"&limit="+limit;
        String url = Urls.NEARBY_URL+sWhere;
        RequestUtils.createRequest(getActivity(), url, "", true, map, true, listener, errorListener);
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