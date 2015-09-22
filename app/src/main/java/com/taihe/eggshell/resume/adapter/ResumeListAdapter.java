package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.resume.ResumeScanActivity;
import com.taihe.eggshell.resume.entity.Resumes;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by wang on 2015/9/21.
 */
public class ResumeListAdapter extends BaseAdapter{

    private Context mContext;
    private List<Resumes> resumelist;
    private ResumeSelectedListener resumeSelectedListener;

    public ResumeListAdapter(Context context,List<Resumes> list,ResumeSelectedListener listener){
        this.mContext = context;
        this.resumelist = list;
        this.resumeSelectedListener = listener;
    }

    @Override
    public int getCount() {
        return resumelist.size();
    }

    @Override
    public Object getItem(int position) {
        return resumelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Resumes resume = resumelist.get(position);
        ResumeHolderView holder;
        if(null == convertView){
            holder = new ResumeHolderView();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_resume_list,null);

            holder.checkBox = (CheckBox)convertView.findViewById(R.id.id_check_box);
            holder.scanResume = (TextView)convertView.findViewById(R.id.id_scan_resume);
            holder.resumeTime = (TextView)convertView.findViewById(R.id.id_resume_time);

            convertView.setTag(holder);
        }else{
            holder = (ResumeHolderView)convertView.getTag();
        }
        holder.checkBox.setChecked(false);
        holder.checkBox.setText("\u3000"+resume.getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    resumeSelectedListener.selectedResume(resume);
                }else{
                    resumeSelectedListener.deleteResume(resume);
                }
            }
        });

        holder.resumeTime.setText(FormatUtils.timestampToDatetime(resume.getCtime()));

        holder.scanResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,ResumeScanActivity.class);
                intent.putExtra("eid",resume);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    class ResumeHolderView{
        CheckBox checkBox;
        TextView scanResume,resumeTime;
    }

    public interface ResumeSelectedListener{
        public void selectedResume(Resumes resume);
        public void deleteResume(Resumes resumes);
    }

}
