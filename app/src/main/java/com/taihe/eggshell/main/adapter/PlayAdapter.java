package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.videoplay.VideoPlayActivity;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 视频列表数据
 */
public class PlayAdapter extends BaseAdapter{

    private Context mContext;
    private  List<PlayInfoMode> list;
    private int type;

    public PlayAdapter(Context context){
        this.mContext = context;
    }
    public void setPlayData( List<PlayInfoMode> list,int type){
        this.list = list;
        this.type = type;
        notifyDataSetChanged();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext,R.layout.meeting_list_mb, null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_name);
            viewHolder.txtAddr = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_addr);
            viewHolder.txtUser = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_zbf);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_date);
            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.img_meetinglist_mb_log);

            viewHolder.txtTitle2 = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_name2);
            viewHolder.txtDate2 = (TextView)convertView.findViewById(R.id.txt_meetinglist_mb_date2);

            viewHolder.tag1 = (RelativeLayout)convertView.findViewById(R.id.relative_meeting_list_mb_tag1);
            viewHolder.tag2 = (RelativeLayout)convertView.findViewById(R.id.relative_meeting_list_mb_tag2);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(type==2) {
            viewHolder.tag1.setVisibility(View.VISIBLE);
            viewHolder.tag2.setVisibility(View.GONE);
            viewHolder.txtTitle.setText(list.get(position).getTitle().toString());
            viewHolder.txtAddr.setText(list.get(position).getAddress().toString());
            viewHolder.txtUser.setText(list.get(position).getOrganizers().toString());
//            viewHolder.txtDate.setText(list.get(position).getStarttime().toString());
            viewHolder.txtDate.setVisibility(View.GONE);
        }else{
            viewHolder.tag2.setVisibility(View.VISIBLE);
            viewHolder.tag1.setVisibility(View.GONE);
            viewHolder.txtTitle2.setText(list.get(position).getTitle().toString());
            viewHolder.txtDate2.setText(list.get(position).getStarttime().toString()+"～"+list.get(position).getEndtime().toString());
            viewHolder.txtDate.setVisibility(View.VISIBLE);
        }
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(viewHolder.imgPic,list.get(position).getLogo().toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,InfoDetailActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("id", list.get(position).getId().toString());
                intent.putExtra("title", list.get(position).getTitle().toString());
                intent.putExtra("logo", list.get(position).getLogo().toString());
                intent.putExtra("address", list.get(position).getAddress().toString());
                intent.putExtra("starttime", list.get(position).getStarttime().toString());
                intent.putExtra("endtime", list.get(position).getEndtime().toString());
                intent.putExtra("user", list.get(position).getUser().toString());
                intent.putExtra("content", list.get(position).getContent().toString());
                intent.putExtra("organizers", list.get(position).getOrganizers().toString());
                intent.putExtra("telphone", list.get(position).getTelphone().toString());
                intent.putExtra("traffic_route", list.get(position).getTraffic_route().toString());
                intent.putExtra("every_time", list.get(position).getEvery_time().toString());
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView txtTitle,txtAddr,txtUser,txtDate;
        TextView txtTitle2,txtDate2;
        ImageView imgPic;
        RelativeLayout tag1,tag2;
    }
}
