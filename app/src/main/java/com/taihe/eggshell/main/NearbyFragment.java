package com.taihe.eggshell.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.personalCenter.adapter.MyActivityAdapter;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyFragment extends Fragment implements View.OnClickListener{

    private View headView;
    private TextView id_title,txt_around_tag1,txt_around_tag2;
    private LinearLayout lin_back,lin_around_tag1,lin_around_tag2,id_linear_listview_tag;
    private PullToRefreshListView playView;
    private MyActivityAdapter playAdapter;
    private ImageView img_around_tag1,img_around_tag2;
    int limit=5,page=1,type=2;
    private List<PlayInfoMode> list = new ArrayList<PlayInfoMode>();
    private LoadingProgressDialog loading;
    private Bitmap bitmap;
    private FinalBitmap finalBitmap;
    private PlayInfoMode playInfoModes;

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
        playView = (PullToRefreshListView) v.findViewById(R.id.id_video_listview);
        init();
        initData();
		return v;
	}

	public void init(){
        id_title.setText("爱活动");
        bitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.tu);
        finalBitmap = FinalBitmap.create(getActivity());

        lin_back.setVisibility(View.GONE);
        playView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        playAdapter = new MyActivityAdapter(getActivity(),list);
        playView.setAdapter(playAdapter);

        lin_around_tag1.setOnClickListener(this);
        lin_around_tag2.setOnClickListener(this);
        loading = new LoadingProgressDialog(getActivity(),"正在请求...");

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.item_activity_command,null);
        playView.getRefreshableView().addHeaderView(headView);

        playView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(type == 1){//往期回顾
                    ToastUtils.show(getActivity(),list.get(position-1).getTitle()+":"+position);
                    Intent intent = new Intent(getActivity(),InfoDetailActivity.class);
                    intent.putExtra("playId",list.get(position-1).getId());
                    intent.putExtra("outTime","3");
                    startActivity(intent);
                }else if(type == 2) {//正在进行的活动
                    if(1==position){
                        ToastUtils.show(getActivity(),playInfoModes.getTitle()+":"+position);
                        Intent intent = new Intent(getActivity(),InfoDetailActivity.class);
                        intent.putExtra("playId",playInfoModes.getId());
                        startActivity(intent);
                    }else{
                        ToastUtils.show(getActivity(),list.get(position-2).getTitle()+":"+position);
                        Intent intent = new Intent(getActivity(),InfoDetailActivity.class);
                        intent.putExtra("playId",list.get(position-2).getId());
                        startActivity(intent);
                    }
                }

            }
        });

        playView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading.show();
                page=1;
                list.clear();
                getListData();
                playView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loading.show();
                page++;
                getListData();
                playView.onRefreshComplete();
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
                type=2;
                loading.show();
                playView.getRefreshableView().addHeaderView(headView);
                img_around_tag1.setBackgroundResource(R.drawable.high);
                img_around_tag2.setBackgroundResource(R.drawable.fulick);
                txt_around_tag1.setTextColor(getActivity().getResources().getColor(R.color.include_title_color));
                txt_around_tag2.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
                playView.setVisibility(View.VISIBLE);
                getListData();
                playView.onRefreshComplete();
                break;
            case R.id.lin_around_tag2:
                list.clear();
                type=1;
                loading.show();
                playView.getRefreshableView().removeHeaderView(headView);
                img_around_tag1.setBackgroundResource(R.drawable.highck);
                img_around_tag2.setBackgroundResource(R.drawable.fuli);
                txt_around_tag2.setTextColor(getActivity().getResources().getColor(R.color.include_title_color));
                txt_around_tag1.setTextColor(getActivity().getResources().getColor(R.color.font_color_black));
                page=1;
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
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        String data = jsonObject.getString("data");
                        Gson gson = new Gson();
                        if(type==2){//正在进行的活动
                            JSONObject js = new JSONObject(data);
                            String command = js.getString("recommend");
                            playInfoModes = gson.fromJson(command,new TypeToken<PlayInfoMode>(){}.getType());
                            setHeadView(playInfoModes);//填充head
                            String result = js.getString("result");
                            List<PlayInfoMode> playlist = gson.fromJson(result, new TypeToken<List<PlayInfoMode>>(){}.getType());
                            list.addAll(playlist);
                        }else if(type==1){//往期回顾
                            List<PlayInfoMode> playlist = gson.fromJson(data, new TypeToken<List<PlayInfoMode>>(){}.getType());
                            list.addAll(playlist);
                        }
                        playAdapter.notifyDataSetChanged();
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
        map.put("type",""+type);
        map.put("limit",""+limit);
        map.put("page",""+page);

        RequestUtils.createRequest(getActivity(),Urls.getMopHostUrl(), Urls.METHOD_ACTIVITY_LIST, true, map, true, listener, errorListener);
    }

    private void setHeadView(PlayInfoMode playInfoMode){

        TextView txtTitle = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_name);
        TextView txtAddr = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_addr);
        TextView txtUser = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_owner);
        TextView txtDate = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_time);
        TextView txtColle = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_collection);
        TextView txtJoin = (TextView)playView.getRefreshableView().findViewById(R.id.id_activity_join);
        ImageView imgPic = (ImageView)playView.getRefreshableView().findViewById(R.id.id_activity_imge);

        txtTitle.setText(playInfoMode.getTitle());
        txtAddr.setText(playInfoMode.getAddress());
        txtUser.setText("主办方："+playInfoMode.getOrganizers());
        txtDate.setText(playInfoMode.getStarttime());
        txtColle.setText(playInfoMode.getCollect_count());
        txtJoin.setText(playInfoMode.getApply_count());

        finalBitmap.display(imgPic,playInfoMode.getLogo(),bitmap,bitmap);
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