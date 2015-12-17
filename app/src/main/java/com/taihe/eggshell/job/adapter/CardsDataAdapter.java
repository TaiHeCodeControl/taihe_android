package com.taihe.eggshell.job.adapter;

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
import com.taihe.eggshell.job.bean.JobInfo;

import net.tsz.afinal.FinalBitmap;

import java.util.ArrayList;

public class CardsDataAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<JobInfo> jblist;
    private Bitmap bitmap;
    private FinalBitmap finalBitmap;
    public CardsDataAdapter(Context context,ArrayList<JobInfo> list){
        this.mContext = context;
        this.jblist = list;
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.no_logo_company);
        finalBitmap = FinalBitmap.create(mContext);
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
        JobInfo jobInfo = jblist.get(position);

        CardViewHolder holder;
        if(null == contentView){

            holder = new CardViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            contentView = inflater.inflate(R.layout.card_content, parent, false);
            holder.comImageView = (ImageView)contentView.findViewById(R.id.id_company_logo);
            holder.positionView = (TextView)contentView.findViewById(R.id.id_job_name);
            holder.companyNameView = (TextView)contentView.findViewById(R.id.id_company_name);
            holder.salaryView = (TextView)contentView.findViewById(R.id.id_salary);
            holder.experienceView = (TextView)contentView.findViewById(R.id.id_experince);
            holder.eduLevelView = (TextView)contentView.findViewById(R.id.id_edu_level);
            holder.locationView = (TextView)contentView.findViewById(R.id.id_address_name);
            holder.positionTypeView = (TextView)contentView.findViewById(R.id.id_job_type);
            holder.jobNumView = (TextView)contentView.findViewById(R.id.id_job_num);
            holder.sexView = (TextView)contentView.findViewById(R.id.id_sex);
            holder.marriageView = (TextView)contentView.findViewById(R.id.id_is_marriy);
            holder.arriveView = (TextView)contentView.findViewById(R.id.id_arrive);
            holder.publishView = (TextView)contentView.findViewById(R.id.id_publish_time);

            contentView.setTag(holder);
        }else{
            holder = (CardViewHolder)contentView.getTag();
        }

            finalBitmap.display(holder.comImageView,jobInfo.getCom_logo(),bitmap,bitmap);
            holder.positionView.setText(jobInfo.getJob_name());
            holder.companyNameView.setText(jobInfo.getCom_name());
            holder.salaryView.setText(jobInfo.getSalary());
            holder.experienceView.setText(jobInfo.getExp());
            holder.eduLevelView.setText(jobInfo.getEdu());
            holder.locationView.setText(jobInfo.getProvinceid());
            holder.positionTypeView.setText("性质:"+jobInfo.getType());
            holder.jobNumView.setText("招聘:"+jobInfo.getNumber());
            holder.sexView.setText("性别:"+jobInfo.getSex());
            holder.marriageView.setText("婚姻:"+jobInfo.getMarriage());
            holder.arriveView.setText("到岗:"+jobInfo.getReport());
            holder.publishView.setText("发布时间:"+jobInfo.getLastupdate());

        return contentView;
    }

    class CardViewHolder{
        ImageView comImageView;
        TextView positionView;
        TextView companyNameView;
        TextView salaryView,experienceView,eduLevelView,locationView,positionTypeView,jobNumView,sexView,marriageView,arriveView,publishView;
    }

}

