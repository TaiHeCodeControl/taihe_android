package com.taihe.eggshell.company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by wang on 2015/11/24.
 */
public class CompanyJobAdapter extends BaseAdapter{

    private Context context;

    private ArrayList<CompanyJob> list;

    public CompanyJobAdapter(Context mcontext,ArrayList<CompanyJob> mlist){
        this.context = mcontext;
        this.list = mlist;
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

        CompanyJob job = list.get(position);
        ComViewHolder holder;
        if(null == contentview){
            holder = new ComViewHolder();
            contentview = LayoutInflater.from(context).inflate(R.layout.item_company_job,null);
            holder.linearLayout = (LinearLayout)contentview.findViewById(R.id.id_job);
            holder.checkBox = (CheckBox)contentview.findViewById(R.id.id_check_box);
            holder.statusTextView = (TextView)contentview.findViewById(R.id.id_postion_status);
            holder.receiveTextView = (TextView)contentview.findViewById(R.id.id_receive);
            holder.skipTextView = (TextView)contentview.findViewById(R.id.id_skip);
            holder.refreshTextView = (TextView)contentview.findViewById(R.id.id_refresh);
            holder.publicTextView = (TextView)contentview.findViewById(R.id.id_public);

            contentview.setTag(holder);
        }else{
            holder = (ComViewHolder)contentview.getTag();
        }

        if(0==job.getStatus()){
            holder.statusTextView.setText("招聘中");
        }else if(1==job.getStatus()){
            holder.statusTextView.setText("已暂停");
        }else if(2==job.getStatus()){
            holder.statusTextView.setText("招聘中");
        }

        holder.receiveTextView.setText(job.getCount());//收到个数
        holder.skipTextView.setText(job.getJobhits());//浏览个数
        holder.refreshTextView.setText(job.getLastupdate());//刷新时间
        holder.publicTextView.setText(job.getEdate());//结束时间
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean flag) {
                if(flag){
                    ToastUtils.show(context,flag+"");
                }
            }
        });

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.show(context,"sd");
            }
        });

        return contentview;
    }

    class ComViewHolder{
        LinearLayout linearLayout;
        CheckBox checkBox;
        TextView statusTextView,receiveTextView,skipTextView,refreshTextView,publicTextView;
    }
}
