package com.taihe.eggshell.main.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.videoplay.VideoPlayActivity;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 视频列表数据
 */
public class VideoAdapterGride extends BaseAdapter{

    private Context mContext;
    private  List<VideoInfoMode> list;

    public VideoAdapterGride(Context context){
        this.mContext = context;
    }
    public void setVideoData( List<VideoInfoMode> list){
        this.list = list;
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
            convertView = LinearLayout.inflate(mContext,R.layout.video_info_mb, null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txt_video_mb_title);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_video_mb_name);
            viewHolder.txtMoney = (TextView)convertView.findViewById(R.id.txt_video_mb_money);
            viewHolder.txtObvious = (TextView)convertView.findViewById(R.id.txt_video_mb_click_num);
            viewHolder.txtAbout = (TextView)convertView.findViewById(R.id.txt_video_mb_about);
            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.img_video_mb_log);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.txtTitle.setText(list.get(position).getVideo_name().toString());
        viewHolder.txtName.setText(list.get(position).getVideo_teacher().toString());
        viewHolder.txtMoney.setText(list.get(position).getStatus().toString()+"元");
        viewHolder.txtObvious.setText(list.get(position).getVideo_obvious().toString());
        viewHolder.txtAbout.setText(list.get(position).getVideo_about().toString());
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(viewHolder.imgPic,list.get(position).getVimage().toString());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RequestUtils.GetWebType(mContext) != 0) {
                    Intent intent = new Intent(mContext,VideoPlayActivity.class);
                    intent.putExtra("vid", list.get(position).getVideo_id().toString());
                    intent.putExtra("title", list.get(position).getVideo_name().toString());
                    intent.putExtra("c_id", list.get(position).getC_id().toString());
                    intent.putExtra("plist", list.get(position).getPlist().toString());
                    intent.putExtra("path", "");
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext,"网络连接异常,请检查网络是否正常！",Toast.LENGTH_LONG).show();
                }

            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView txtTitle,txtName,txtMoney,txtObvious,txtAbout;
        ImageView imgPic;
    }
}
