package com.taihe.eggshell.company.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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
public class ComResumeAdapter extends BaseAdapter{

    private Context mContext;
    private  List<ComResumeMode> list;
    private int type;

    public ComResumeAdapter(Context context){
        this.mContext = context;
    }
    public void setPlayData( List<ComResumeMode> list,int type){
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
            convertView = LinearLayout.inflate(mContext,R.layout.activity_com_resume_mb, null);
            viewHolder.txtUName = (TextView)convertView.findViewById(R.id.comresume_txt_mb_uname);
            viewHolder.txtJob = (TextView)convertView.findViewById(R.id.comresume_txt_mb_job);
            viewHolder.txtJobDate = (TextView)convertView.findViewById(R.id.comresume_txt_mb_jobdate);
            viewHolder.txtDate = (TextView)convertView.findViewById(R.id.comresume_txt_mb_date);
            viewHolder.txtMoney = (TextView)convertView.findViewById(R.id.comresume_txt_mb_money);

            viewHolder.txtLook = (TextView)convertView.findViewById(R.id.comresume_txt_mb_look);
            viewHolder.txtNotice = (TextView)convertView.findViewById(R.id.comresume_txt_mb_notice);
            viewHolder.txtNoUse = (TextView)convertView.findViewById(R.id.comresume_txt_mb_nouse);
            viewHolder.com_rel_mb = (RelativeLayout)convertView.findViewById(R.id.com_rel_mb);
            viewHolder.com_lin_mb = (LinearLayout)convertView.findViewById(R.id.com_lin_mb);

            viewHolder.imgCheck = (ImageView)convertView.findViewById(R.id.comresume_img_mb_check);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(type==2){
            viewHolder.txtLook.setVisibility(View.GONE);
        }else if(type==3){
            viewHolder.txtLook.setVisibility(View.GONE);
            viewHolder.txtNotice.setVisibility(View.GONE);
        }else if(type==4){
            viewHolder.txtLook.setVisibility(View.GONE);
            viewHolder.txtNotice.setVisibility(View.GONE);
            viewHolder.txtNoUse.setVisibility(View.GONE);
        }
        viewHolder.txtUName.setText(list.get(position).getId().toString()+list.get(position).getName().toString());
        viewHolder.txtJob.setText(list.get(position).getJob_name().toString());
        viewHolder.txtJobDate.setText(list.get(position).getExp().toString());
        viewHolder.txtDate.setText(list.get(position).getDatetime().toString());
        viewHolder.txtMoney.setText(list.get(position).getSalary().toString());

        if (list.get(position).isChecked()) {
            viewHolder.imgCheck.setImageResource(R.drawable.xuankuang_red);
        } else {
            viewHolder.imgCheck.setImageResource(R.drawable.xuankuang);
        }
        viewHolder.txtLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Urls.COMPY_LOOK_RESUME_URL;
                getListData(list.get(position).getCom_id().toString(),list.get(position).getEid().toString(),list.get(position).getJob_id().toString(),position,url);
            }
        });
        viewHolder.txtNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Urls.COMPY_NOTICE_RESUME_URL;
                getListData(list.get(position).getCom_id().toString(),list.get(position).getEid().toString(),list.get(position).getJob_id().toString(),position,url);
            }
        });
        viewHolder.txtNoUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = Urls.COMPY_NOUSE_RESUME_URL;
                getListData(list.get(position).getCom_id().toString(),list.get(position).getEid().toString(),list.get(position).getJob_id().toString(),position,url);
            }
        });
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
        TextView txtLook,txtNotice,txtNoUse;
        ImageView imgCheck;
        LinearLayout com_lin_mb;
        RelativeLayout com_rel_mb;
    }
    public void getListData(String comID,String eID,String jobID,final int itemID,String url) {
        //返回监听事件
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            list.remove(itemID);
                            notifyDataSetChanged();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
                ToastUtils.show(mContext, "网络异常");
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        map.put("com_id",comID);
        map.put("eid",eID);
        map.put("job_id",jobID);
        RequestUtils.createRequest(mContext, url, "", true, map, true, listener, errorListener);
    }

}
