package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.meetinginfo.entity.DiscussInfo;

import java.util.ArrayList;

/**
 * Created by wang on 2016/2/17.
 */
public class DiscussAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<DiscussInfo> discussInfoList = new ArrayList<DiscussInfo>();

    public DiscussAdapter(Context mcontext,ArrayList<DiscussInfo> lists){
        this.context = mcontext;
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

        DiscussInfo info = discussInfoList.get(i);
        DiscussViewHolder viewHolder;
        if(view == null){
            viewHolder = new DiscussViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_discuss,null);
            viewHolder.notificationImg = (ImageView)view.findViewById(R.id.id_notification);
            viewHolder.nameText = (TextView)view.findViewById(R.id.id_discuss_name);
            viewHolder.contextText = (TextView)view.findViewById(R.id.id_discuss_content);
            viewHolder.timeText = (TextView)view.findViewById(R.id.id_discuss_time);

            view.setTag(viewHolder);
        }else{
            viewHolder = (DiscussViewHolder)view.getTag();
        }

        viewHolder.nameText.setText(info.getName());
        viewHolder.contextText.setText(info.getDiscuss());
        viewHolder.timeText.setText(info.getAnswerTime());

        return view;
    }

    class DiscussViewHolder{
        ImageView notificationImg;
        TextView nameText;
        TextView contextText;
        TextView timeText;
    }
}
