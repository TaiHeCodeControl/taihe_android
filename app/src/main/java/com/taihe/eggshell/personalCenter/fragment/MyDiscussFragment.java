package com.taihe.eggshell.personalCenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.meetinginfo.adapter.DiscussAdapter;
import com.taihe.eggshell.meetinginfo.entity.DiscussInfo;
import com.taihe.eggshell.widget.ChoiceDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huan on 2015/7/23.
 */
public class MyDiscussFragment extends Fragment {

    private Context mContext;
    private PullToRefreshListView listView;
    private DiscussAdapter discussAdapter;
    private ChoiceDialog deleteDialog;
    private ArrayList<DiscussInfo> discussList = new ArrayList<DiscussInfo>();
    private int pagesize = 1;
    private DiscussInfo deletInfo;
    private View rootview;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    deletInfo = (DiscussInfo)msg.obj;
                    deleteDialog.show();
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        rootview = inflater.inflate(R.layout.fragment_discuss, null) ;
        mContext = getActivity();
        return rootview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (PullToRefreshListView)rootview.findViewById(R.id.id_discuss_list);
        listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        deleteDialog = new ChoiceDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                    deleteDialog.dismiss();
                    if(deletInfo!=null){
                        deletDiscussInfo();
                    }else{
                        ToastUtils.show(mContext, "");
                    }

                }else{
                    ToastUtils.show(mContext, R.string.check_network);
                }
            }
        });

        deleteDialog.getTitleText().setText("确定要删除吗？");
        deleteDialog.getRightButton().setText("确定");
        deleteDialog.getLeftButton().setText("取消");

        initData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            discussList.clear();
            pagesize = 1;
            getDiscussData();
        }else{
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getUserVisibleHint()) {
            discussList.clear();
            pagesize = 1;
            getDiscussData();
        }else{
        }
    }

    private void initData(){

        discussAdapter = new DiscussAdapter(mContext,discussList,handler);
        listView.setAdapter(discussAdapter);
        discussAdapter.notifyDataSetChanged();

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pagesize +=1;
                getDiscussData();
            }
        });
    }

    private void getDiscussData(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
//                Log.v("LISt:", (String) o);
                listView.onRefreshComplete();
                Gson gson = new Gson();
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        String data = jsonObject.getString("data");
                        if(data.equals("[]")){
                            ToastUtils.show(mContext,"没有了");
                        }else{
                            List<DiscussInfo> discussInfoList = gson.fromJson(data,new TypeToken<List<DiscussInfo>>(){}.getType());
                            discussList.addAll(discussInfoList);
                        }

                        discussAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                        Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("uid", EggshellApplication.getApplication().getUser().getId()+"");
        params.put("page",pagesize+"");
//        Log.v("PARA:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_DISCUSS, false, params, true, listener, errorListener);

    }

    private void deletDiscussInfo(){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

//                Log.v("TTT:", (String) o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        discussList.remove(deletInfo);
                        discussAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("id", deletInfo.getId());
        params.put("type", deletInfo.getType());
//        Log.v("DD:",params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),Urls.METHOD_DELETE_DISCUS_IFNO,false,params,true,listener,errorListener);

    }

    public String[] getUnReadMsg(){
        StringBuilder sb = new StringBuilder("");
        StringBuilder ty = new StringBuilder("");
        for(int i=0;i<discussList.size();i++){
            if(discussList.get(i).getIsread().equals("1")){
                sb.append(discussList.get(i).getId()+",");
                ty.append(discussList.get(i).getType()+",");
            }
        }

        String[] str = new String[2];
        str[0] = sb.toString().length()>0 ? sb.toString().substring(0,sb.toString().lastIndexOf(",")) : "";
        str[1] = ty.toString().length()>0 ? ty.toString().substring(0,ty.toString().lastIndexOf(",")) : "";
        return str;
    }

    public void refreshMsg(){
        discussList.clear();
        pagesize = 1;
        getDiscussData();
    }
}
