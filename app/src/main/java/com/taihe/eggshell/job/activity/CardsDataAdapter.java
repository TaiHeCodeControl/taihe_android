package com.taihe.eggshell.job.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;

import java.util.ArrayList;

public class CardsDataAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<JobInfo> jblist;
    private TextView v1;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private int resourceId;

    public CardsDataAdapter(Context context,ArrayList<JobInfo> list){
        this.mContext = context;
        this.jblist = list;
    }

    @Override
    public int getCount() {
        return jblist.size();
    }

    @Override
    public Object getItem(int position) {
        return jblist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View userListItem = inflater.inflate(R.layout.card_content, parent, false);

        linearLayout = (LinearLayout)userListItem.findViewById(R.id.id_linear);
        imageView = (ImageView)userListItem.findViewById(R.id.id_card_view);
        TextView v = (TextView)(userListItem.findViewById(R.id.content));
        v1 = (TextView)(userListItem.findViewById(R.id.content1));
        TextView v2 = (TextView)(userListItem.findViewById(R.id.content2));
        v.setText(jblist.get(position).getName());
        v1.setText(jblist.get(position).getAge());
        v2.setText(jblist.get(position).getId()+"");
        return userListItem;
    }

}

