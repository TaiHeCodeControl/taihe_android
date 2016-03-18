package com.taihe.eggshell.personalCenter.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.job.activity.JobDetailActivity;
import com.taihe.eggshell.personalCenter.entity.InvitedCompany;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/3/11.
 */
public class InvitedAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<InvitedCompany> list;
    private Handler handler;
    private Bitmap bitmap;
    private FinalBitmap finalBitmap;

    public InvitedAdapter(Context context,ArrayList<InvitedCompany> invitedCompanies,Handler vitedHandler){
        this.mContext = context;
        this.list = invitedCompanies;
        this.handler = vitedHandler;

        bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.tu);
        finalBitmap = FinalBitmap.create(mContext);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        final InvitedCompany company  = list.get(i);
        InvitedHolder holder;
        if(null==view){
            holder = new InvitedHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_invite,null);

            holder.relativeLayout = (RelativeLayout)view.findViewById(R.id.id_item_discuss);
            holder.logo = (ImageView)view.findViewById(R.id.id_company_logo);
            holder.isread = (ImageView)view.findViewById(R.id.id_notification);
            holder.name = (TextView)view.findViewById(R.id.id_name);
            holder.address = (TextView)view.findViewById(R.id.id_addres);
            holder.position = (TextView)view.findViewById(R.id.id_position_name);
            holder.salary = (TextView)view.findViewById(R.id.id_salary);
            holder.time = (TextView)view.findViewById(R.id.id_invited_time);

            view.setTag(holder);
        }else{
            holder = (InvitedHolder)view.getTag();
        }

        holder.name.setText(company.getCompany_name());
        holder.position.setText(company.getCompany_job_name());
        holder.address.setText(company.getAddress());
        holder.time.setText(company.getTime());
        holder.salary.setText(company.getSalary());

        finalBitmap.display(holder.logo, company.getCompany_logo(), bitmap, bitmap);

        if("1".equals(company.getIsread())){//未阅读
            holder.isread.setVisibility(View.VISIBLE);
        }else if("2".equals(company.getIsread())){//已阅读
            holder.isread.setVisibility(View.INVISIBLE);
        }

        holder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Message message = Message.obtain();
                message.what = 1;
                message.obj = company;
                handler.sendMessage(message);
                return false;
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (company.getIsread().equals("1")) {//未阅读
                    readRequest(company);
                } else {
                    Intent intent = new Intent(mContext, JobDetailActivity.class);
                    intent.putExtra("ID", company.getCompany_job_id());
                    intent.putExtra("com_id", company.getCompany_id());
                    mContext.startActivity(intent);
                }
            }
        });


        return view;
    }

    class InvitedHolder{
        ImageView logo,isread;
        TextView name,address,time,position,salary;
        RelativeLayout relativeLayout;
    }

    private void readRequest(final InvitedCompany info){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        Intent intent = new Intent(mContext,JobDetailActivity.class);
                        intent.putExtra("ID", info.getCompany_job_id());
                        intent.putExtra("com_id", info.getCompany_id());
                        mContext.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                Log.v("EEE:",new String(volleyError.networkResponse.data));
            }
        };

        Map<String,String> params = new HashMap<String,String>();
        params.put("id", info.getId());
//        params.put("type", info.getType());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_INVITE_IS_READ, false, params, true, listener, errorListener);

    }
}
