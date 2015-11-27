package com.taihe.eggshell.meetinginfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;

/**
 * Created by wang on 2015/11/25.
 */
public class DRAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PersonModel> list;

    public DRAdapter(Context context, ArrayList<PersonModel> plist) {
        this.mContext = context;
        this.list = plist;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View contentview, ViewGroup viewGroup) {
        PersonModel personModel = list.get(position);
        VViewHolder holder;
        if (null == contentview) {
            holder = new VViewHolder();
            contentview = LayoutInflater.from(mContext).inflate(R.layout.item_dr_v, null);
            holder.vImageView = (ImageView) contentview.findViewById(R.id.id_v);
            holder.nameTextView = (TextView) contentview.findViewById(R.id.id_user_name);
            holder.typeTextView = (TextView) contentview.findViewById(R.id.id_user_type);

            contentview.setTag(holder);
        } else {
            holder = (VViewHolder) contentview.getTag();
        }
        Bitmap defaultmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tu);
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(holder.vImageView, personModel.getStudentsphoto(),defaultmap,defaultmap);
        holder.nameTextView.setText(personModel.getStudentsname());
        holder.typeTextView.setText(personModel.getStudentsnature());

        return contentview;
    }

    class VViewHolder {
        ImageView vImageView;
        TextView nameTextView;
        TextView typeTextView;
    }
}
