package com.taihe.eggshell.videoplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;

import java.util.ArrayList;

/**
 * Created by Thinkpad on 2015/7/23.
 */
public class VideoListAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<String> list = new ArrayList<String>();

    public VideoListAdapter(Context context){
        this.mContext = context;

        for(int i=0;i<10;i++){
            list.add("哈哈"+i);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if(null==convertView){
               viewHolder = new ViewHolder();
               convertView = LayoutInflater.from(mContext).inflate(R.layout.item_video_list,null);
               viewHolder.titleView = (TextView)convertView.findViewById(R.id.id_book_title);

               convertView.setTag(viewHolder);
        }else{
               viewHolder = (ViewHolder)convertView.getTag();
        }
        return convertView;
    }

    class ViewHolder{
        TextView titleView;
    }
}
