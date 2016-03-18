package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.meetinginfo.entity.DiscussInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussAdapter extends BaseAdapter{

    private Context context;
    private Handler delhandler;
    private ArrayList<DiscussInfo> discussInfoList = new ArrayList<DiscussInfo>();
    private boolean isMore = false;

    public DiscussAdapter(Context mcontext,ArrayList<DiscussInfo> lists,Handler handler){
        this.context = mcontext;
        this.delhandler = handler;
        this.discussInfoList = lists;
    }

    @Override
    public int getCount() {
        return discussInfoList.size();
    }

    @Override
    public Object getItem(int i) {
        return discussInfoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final DiscussInfo info = discussInfoList.get(i);
        final DiscussViewHolder viewHolder;
        if(view == null){
            viewHolder = new DiscussViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_discuss,null);
            viewHolder.relativeLayout = (RelativeLayout)view.findViewById(R.id.id_item_discuss);
            viewHolder.notificationImg = (ImageView)view.findViewById(R.id.id_notification);
            viewHolder.nameText = (TextView)view.findViewById(R.id.id_discuss_name);
            viewHolder.contextText = (TextView)view.findViewById(R.id.id_discuss_content);
            viewHolder.timeText = (TextView)view.findViewById(R.id.id_discuss_time);
            viewHolder.textShow = (TextView)view.findViewById(R.id.id_show);

            view.setTag(viewHolder);
        }else{
            viewHolder = (DiscussViewHolder)view.getTag();
        }

        if(TextUtils.isEmpty(info.getNick_name())){
            viewHolder.nameText.setText(info.getUsername()+"回复了你");
        }else{
            viewHolder.nameText.setText(info.getNick_name()+"回复了你");
        }

        viewHolder.contextText.setText(info.getComent());
        viewHolder.timeText.setText(info.getAddtime());

        viewHolder.contextText.post(new Runnable() {
            @Override
            public void run() {
                if(viewHolder.contextText.getLineCount()>1){
                    viewHolder.contextText.setMaxLines(1);
                    viewHolder.textShow.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.textShow.setVisibility(View.GONE);
                }
            }
        });

        viewHolder.textShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMore){
                    isMore = false;
                    viewHolder.contextText.setMaxLines(Integer.MAX_VALUE);
                    viewHolder.textShow.setText("收起");
                }else{
                    isMore = true;
                    viewHolder.contextText.setMaxLines(1);
                    viewHolder.textShow.setText("展开");
                }
            }
        });

        if("1".equals(info.getIsread())){//未阅读
            viewHolder.notificationImg.setVisibility(View.VISIBLE);
        }else if("2".equals(info.getIsread())){//已阅读
            viewHolder.notificationImg.setVisibility(View.INVISIBLE);
        }

        viewHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = info;
                delhandler.sendMessage(message);
                return false;
            }
        });

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.getIsread().equals("1")) {//未阅读
                    readRequest(info);
                } else {
                    Intent intent = new Intent(context, InfoDetailActivity.class);
                    intent.putExtra("playId", info.getAid());
                    context.startActivity(intent);
                }
            }
        });

        return view;
    }

    class DiscussViewHolder{
        ImageView notificationImg;
        TextView nameText;
        TextView contextText;
        TextView timeText;
        TextView textShow;
        RelativeLayout relativeLayout;
    }

    private void readRequest(final DiscussInfo info){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        Intent intent = new Intent(context,InfoDetailActivity.class);
                        intent.putExtra("playId",info.getAid());
                        context.startActivity(intent);
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
        params.put("id", info.getId());
        params.put("type", info.getType());
        RequestUtils.createRequest(context, Urls.getMopHostUrl(), Urls.METHOD_IS_READ, false, params, true, listener, errorListener);

    }
}
