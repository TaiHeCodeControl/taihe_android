package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.BaseActivity;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.adapter.PlayAdapter;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.adapter.MeetingAdapter;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/8/12.
 */
public class Act_MeetingInfo extends BaseActivity {
    private LinearLayout lin_meetinginfo_top1,lin_meetinginfo_top2;
    private TextView txt_meetinginfo_top1,txt_meetinginfo_top2;
    private ImageView img_meetinginfo_top1,img_meetinginfo_top2;
    private PullToRefreshGridView meetingView;
    private MeetingAdapter playAdapter;
    int limit=8,page=1,type=2;
    List<PlayInfoMode> list;
    private LoadingProgressDialog loading;
    private Context mContext;

    @Override
    public void initView() {
        setContentView(R.layout.activity_meetinginfo_list);
        super.initView();
        mContext = this;
        lin_meetinginfo_top1 = (LinearLayout)findViewById(R.id.lin_meetinginfo_top1);
        lin_meetinginfo_top2 = (LinearLayout)findViewById(R.id.lin_meetinginfo_top2);
        txt_meetinginfo_top1 = (TextView)findViewById(R.id.txt_meetinginfo_top1);
        txt_meetinginfo_top2 = (TextView)findViewById(R.id.txt_meetinginfo_top2);
        img_meetinginfo_top1 = (ImageView)findViewById(R.id.img_meetinginfo_top1);
        img_meetinginfo_top2 = (ImageView)findViewById(R.id.img_meetinginfo_top2);
        meetingView = (PullToRefreshGridView)findViewById(R.id.id_meeting_listview);
        lin_meetinginfo_top1.setOnClickListener(this);
        lin_meetinginfo_top2.setOnClickListener(this);
    }

    public void initData(){
        super.initData();
        initTitle("社交圈");
        loading = new LoadingProgressDialog(mContext,"正在请求...");
        meetingView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        playAdapter = new MeetingAdapter(mContext);
        list = new ArrayList<PlayInfoMode>();
        getListData();
        meetingView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page=1;
                list.clear();
                getListData();
                meetingView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                getListData();
                meetingView.onRefreshComplete();
            }
        });
        meetingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext,Act_MeetingInfoImage.class);
                intent.putExtra("title",list.get(i).getTitle());
                intent.putExtra("pic",list.get(i).getLogo());
                intent.putExtra("content",list.get(i).getContent());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.lin_meetinginfo_top1:
                list.clear();
                type=2;
                img_meetinginfo_top1.setBackgroundResource(R.drawable.zhaopinhui);
                img_meetinginfo_top2.setBackgroundResource(R.drawable.shuangxuanhuick);
                txt_meetinginfo_top1.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_meetinginfo_top2.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
                break;
            case R.id.lin_meetinginfo_top2:
                list.clear();
                type=1;
                img_meetinginfo_top1.setBackgroundResource(R.drawable.zhaopinhuick);
                img_meetinginfo_top2.setBackgroundResource(R.drawable.shuangxuanhui);
                txt_meetinginfo_top2.setTextColor(getResources().getColor(R.color.font_color_red));
                txt_meetinginfo_top1.setTextColor(getResources().getColor(R.color.font_color_black));
                page=1;
                meetingView.setVisibility(View.VISIBLE);
                getListData();
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
                    int code = jsonObject.getInt("code");
                    meetingView.setSelection(list.size()-1);
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        try{
                            JSONArray j1 = new JSONArray(data);
                            JSONObject j2;
                            for(int i=0;i<j1.length();i++){
                                PlayInfoMode vMode = new PlayInfoMode();
                                j2 = j1.getJSONObject(i);
                                vMode.setId(j2.optString("id").toString());
                                vMode.setTitle(j2.optString("name").toString());
                                vMode.setLogo(j2.optString("photo").toString());
                                vMode.setContent(j2.optString("content").toString());
//                                vMode.setId(j2.optString("id").toString());
//                                vMode.setTitle(j2.optString("title").toString());
//                                vMode.setAddress(j2.optString("address").toString());
//                                vMode.setOrganizers(j2.optString("organizers").toString());
//                                vMode.setEvery_time("");
//                                vMode.setUser(j2.optString("user").toString());
//                                vMode.setTelphone(j2.optString("phone").toString());
//                                vMode.setTraffic_route(j2.optString("traffic").toString());
//                                vMode.setLogo(j2.optString("logo").toString());
//                                vMode.setContent(j2.optString("body").toString());
//                                vMode.setStarttime(j2.optString("starttime").toString());
//                                vMode.setEndtime(j2.optString("endtime").toString());
                                list.add(vMode);
                            }
                            meetingView.setVisibility(View.VISIBLE);
                            playAdapter.setPlayData(list,2);
                            meetingView.setAdapter(playAdapter);
                            if(list.size()==0){
                                meetingView.removeAllViews();
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {
                        if(list.size()==0) {
                            meetingView.setVisibility(View.GONE);
                        }
                        //String msg = jsonObject.getString("message");
//                        ToastUtils.show(mContext, "网络连接异常");
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
                ToastUtils.show(mContext, "网络异常");
            }
        };

        loading.show();
        Map<String,String> map = new HashMap<String,String>();
        map.put("type",""+type);
        map.put("limit",""+limit);
        map.put("page",""+page);
        String url = Urls.MEETING_LIST_URL;
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(mContext);
    }
}
