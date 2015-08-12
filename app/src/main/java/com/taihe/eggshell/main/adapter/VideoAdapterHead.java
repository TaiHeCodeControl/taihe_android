package com.taihe.eggshell.main.adapter;

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
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;

import net.tsz.afinal.FinalBitmap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * 视频列表数据
 */
public class VideoAdapterHead extends BaseAdapter{

    private Context mContext;
    private  List<VideoInfoMode> list;

    public VideoAdapterHead(Context context){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext,R.layout.video_head_mb, null);
            viewHolder.txtTitle = (TextView)convertView.findViewById(R.id.txt_video_head_mb_title);
            viewHolder.txtName = (TextView)convertView.findViewById(R.id.txt_video_head_mb_name);
            viewHolder.imgPic = (ImageView)convertView.findViewById(R.id.img_video_head_mb_log);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.txtTitle.setText(list.get(position).getVideo_name().toString());
        viewHolder.txtName.setText(list.get(position).getVideo_teacher().toString());
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(viewHolder.imgPic,list.get(position).getVimage().toString());

        return convertView;
    }
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }

        return bitmap;

    }
    class ViewHolder{
        TextView txtTitle,txtName,txtMoney,txtObvious,txtAbout;
        ImageView imgPic;
    }
}
