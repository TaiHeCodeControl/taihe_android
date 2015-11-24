package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
import android.content.Intent;
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

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 视频列表数据
 */
public class MeetingAdapter extends BaseAdapter{

    private Context mContext;
    private  List<PlayInfoMode> list;
    private int type;

    public MeetingAdapter(Context context){
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
            convertView = LinearLayout.inflate(mContext,R.layout.activity_meetinginfo_mb, null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.meetinginfo_txt_title);
            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.meetinginfo_img_ico);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.txtTitle.setText(list.get(position).getTitle().toString());
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(viewHolder.imgPic,list.get(position).getLogo().toString());

        return convertView;
    }
    class ViewHolder{
        TextView txtTitle;
        ImageView imgPic;
    }
}
