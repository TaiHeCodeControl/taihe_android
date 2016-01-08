package com.taihe.eggshell.personalCenter.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.main.mode.PlayInfoMode;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * Created by wang on 2016/1/8.
 */
public class MyActivityAdapter extends BaseAdapter{

    private Context mContext;
    private List<PlayInfoMode> activitylist;
    private Bitmap bitmap;
    private FinalBitmap finalBitmap;

    public MyActivityAdapter(Context context,List<PlayInfoMode> list){
        this.mContext = context;
        this.activitylist = list;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tu);
        finalBitmap = FinalBitmap.create(mContext);
    }

    @Override
    public int getCount() {
        return activitylist.size();
    }

    @Override
    public Object getItem(int position) {
        return activitylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        PlayInfoMode playInfoMode = activitylist.get(position);
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_my_activity, null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.id_activity_name);
            viewHolder.txtAddr = (TextView)convertView.findViewById(R.id.id_activity_addr);
            viewHolder.txtUser = (TextView)convertView.findViewById(R.id.id_activity_owner);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.id_activity_time);
            viewHolder.txtColle = (TextView)convertView.findViewById(R.id.id_activity_collection);
            viewHolder.txtJoin = (TextView)convertView.findViewById(R.id.id_activity_join);
            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.id_activity_imge);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.txtTitle.setText(playInfoMode.getTitle());
        viewHolder.txtAddr.setText(playInfoMode.getAddress());
        viewHolder.txtUser.setText("主办方："+playInfoMode.getOrganizers());
        viewHolder.txtDate.setText(playInfoMode.getStarttime());
        viewHolder.txtColle.setText(playInfoMode.getCollect_count());
        viewHolder.txtJoin.setText(playInfoMode.getApply_count());

        finalBitmap.display(viewHolder.imgPic, playInfoMode.getLogo(), bitmap, bitmap);

        return convertView;
    }

    class ViewHolder{
        TextView txtTitle,txtAddr,txtUser,txtDate;
        TextView txtColle,txtJoin;
        ImageView imgPic;
    }
}
