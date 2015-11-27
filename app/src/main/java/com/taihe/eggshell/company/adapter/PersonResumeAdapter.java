package com.taihe.eggshell.company.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.company.mode.ComResumeMode;
import com.taihe.eggshell.company.mode.PersonResumeMode;
import com.taihe.eggshell.resume.ResumeScanActivity;
import com.taihe.eggshell.resume.entity.Resumes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业简历列表数据
 */
public class PersonResumeAdapter extends BaseAdapter{

    private Context mContext;
    private  List<PersonResumeMode> list;
    private int type;

    public PersonResumeAdapter(Context context){
        this.mContext = context;
    }
    public void setPlayData( List<PersonResumeMode> list,int type){
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
        final ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext,R.layout.activity_person_resume_mb, null);
            viewHolder.txtUName = (TextView)convertView.findViewById(R.id.personresume_txt_mb_uname);
            viewHolder.txtJob = (TextView)convertView.findViewById(R.id.personresume_txt_mb_job);
            viewHolder.txtJobDate = (TextView)convertView.findViewById(R.id.personresume_txt_mb_jobdate);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.personresume_txt_mb_date);
            viewHolder.txtMoney = (TextView)convertView.findViewById(R.id.personresume_txt_mb_money);

            viewHolder.com_rel_mb = (RelativeLayout)convertView.findViewById(R.id.person_rel_mb);
            viewHolder.com_lin_mb = (LinearLayout)convertView.findViewById(R.id.person_lin_mb);

            viewHolder.imgCheck = (ImageView)convertView.findViewById(R.id.personresume_img_mb_check);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.txtUName.setText(list.get(position).getName().toString());
        viewHolder.txtJob.setText(list.get(position).getHopeprofess().toString());
        viewHolder.txtJobDate.setText(list.get(position).getExp().toString());
        viewHolder.txtDate.setText(list.get(position).getCtime().toString());
        viewHolder.txtMoney.setText(list.get(position).getSalary().toString());

        if (list.get(position).isChecked()) {
            viewHolder.imgCheck.setImageResource(R.drawable.xuankuang_red);
        } else {
            viewHolder.imgCheck.setImageResource(R.drawable.xuankuang);
        }
        viewHolder.com_rel_mb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resumes resume = new Resumes();
                resume.setRid(Integer.parseInt(list.get(position).getEid().toString()));
                Intent intent = new Intent(mContext,ResumeScanActivity.class);
                intent.putExtra("eid",resume);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
    class ViewHolder{
        TextView txtUName,txtJob,txtJobDate,txtDate,txtMoney;
        ImageView imgCheck;
        LinearLayout com_lin_mb;
        RelativeLayout com_rel_mb;
    }
}
