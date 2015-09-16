package com.taihe.eggshell.main.adapter;

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
import com.taihe.eggshell.videoplay.VideoPlayActivity;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 视频列表数据
 */
public class VideoInfoAdapter extends BaseAdapter{

    private Context mContext;
    private  List<VideoInfoMode> list;
    private View arrLayout[];
    private int selectedPosition;

    public VideoInfoAdapter(Context context){
        this.mContext = context;
    }
    public void setVideoData( List<VideoInfoMode> list){
        this.list = list;
        arrLayout = new View[list.size()];
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

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext,R.layout.video_info_list_mb, null);
            viewHolder.txtNum = (TextView)convertView.findViewById(R.id.txt_vinfo_mb_num);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_vinfo_mb_name);
            viewHolder.txtTime = (TextView)convertView.findViewById(R.id.txt_vinfo_mb_time);
            viewHolder.relat_vinfo_list_mb = (RelativeLayout)convertView.findViewById(R.id.relat_vinfo_list_mb);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
//        if(0==position){
//            viewHolder.relat_vinfo_list_mb.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
//        }else {
//            viewHolder.relat_vinfo_list_mb.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//        }

        if (selectedPosition == position) {
            viewHolder.relat_vinfo_list_mb.setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
        } else {
            viewHolder.relat_vinfo_list_mb.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
        viewHolder.txtNum.setText(position+1+"");
        viewHolder.txtName.setText(list.get(position).getVideo_name().toString());
        viewHolder.txtTime.setText(list.get(position).getVideo_hour().toString());
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                for (int i = 0; i < arrLayout.length; i++) {
////                    arrLayout[i].getChildAt(0).setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                    arrLayout[i].setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                }
//                arrLayout[position].setBackgroundColor(mContext.getResources().getColor(R.color.bg_gray));
////                Intent intent = new Intent(mContext,VideoPlayActivity.class);
////                intent.putExtra("vid", list.get(position).getVideo_id().toString());
////                intent.putExtra("title", list.get(position).getVideo_name().toString());
////                intent.putExtra("c_id", list.get(position).getC_id().toString());
////                intent.putExtra("path", "");
////                mContext.startActivity(intent);
//            }
//        });

        arrLayout[position]=convertView;
        return convertView;
    }
    class ViewHolder{
        TextView txtNum,txtName,txtTime;
        RelativeLayout relat_vinfo_list_mb;
    }
}
