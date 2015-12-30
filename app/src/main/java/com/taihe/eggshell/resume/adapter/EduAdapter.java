package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.chinaway.framework.swordfish.util.NetWorkDetectionUtils;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.resume.ResumeEduActivity;
import com.taihe.eggshell.resume.entity.ResumeData;
import com.taihe.eggshell.resume.entity.Resumes;
import com.taihe.eggshell.widget.ChoiceDialog;
import com.taihe.eggshell.widget.LoadingProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wang on 2015/9/9.
 */
public class EduAdapter extends BaseAdapter{

    private Context mContext;
    private Resumes resume;
    private Handler handler;
    private List<ResumeData> worklists;
    private ChoiceDialog deleteDialog;
    private LoadingProgressDialog loading;

    public EduAdapter(Context context,List<ResumeData> list,Resumes resumes,Handler handlers){
        this.mContext = context;
        this.worklists = list;
        this.resume = resumes;
        this.handler = handlers;
        loading = new LoadingProgressDialog(mContext,"正在请求...");
    }
    @Override
    public int getCount() {
        return worklists.size();
    }

    @Override
    public Object getItem(int position) {
        return worklists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ResumeData work = worklists.get(position);
        EduViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new EduViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_edu, null);

            viewHolder.edutime = (TextView)convertView.findViewById(R.id.id_time_edu);
            viewHolder.eduindusty = (TextView)convertView.findViewById(R.id.id_professional);
            viewHolder.eduschool = (TextView)convertView.findViewById(R.id.id_school_name);
            viewHolder.eduposition = (TextView)convertView.findViewById(R.id.id_school_posion);
            viewHolder.edubrief = (TextView)convertView.findViewById(R.id.id_prof_brief);
            viewHolder.delite = (TextView)convertView.findViewById(R.id.id_delite);
            viewHolder.edit = (TextView)convertView.findViewById(R.id.id_edit);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (EduViewHolder)convertView.getTag();
        }

        viewHolder.edutime.setText(FormatUtils.timestampToDatetime(work.getSdate())+"——"+FormatUtils.timestampToDatetime(work.getEdate()));
        viewHolder.eduindusty.setText(work.getSpecialty());
        viewHolder.eduposition.setText(work.getTitle());
        viewHolder.eduschool.setText(work.getName());
        viewHolder.edubrief.setText(work.getContent());

        viewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,ResumeEduActivity.class);
                intent.putExtra("eid",resume);
                intent.putExtra("type","2");
                work.setSdate(FormatUtils.timestampToDatetime(work.getSdate()));
                work.setEdate(FormatUtils.timestampToDatetime(work.getEdate()));
                intent.putExtra("listobj", work);
                intent.putExtra("state","");
                mContext.startActivity(intent);
            }
        });

        viewHolder.delite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog = new ChoiceDialog(mContext,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDialog.dismiss();
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(NetWorkDetectionUtils.checkNetworkAvailable(mContext)){
                            deleteDialog.dismiss();
                            loading.show();
                            deleteResume(work.getId()+"");
                        }else{
                            ToastUtils.show(mContext, R.string.check_network);
                        }
                    }
                });

                deleteDialog.getTitleText().setText("确定要删除吗？");
                deleteDialog.getRightButton().setText("确定");
                deleteDialog.getLeftButton().setText("取消");
                deleteDialog.show();
            }
        });
        return convertView;
    }

    class EduViewHolder{
        TextView edutime;
        TextView eduindusty;
        TextView eduschool;
        TextView eduposition;
        TextView edubrief,delite,edit;
    }

    private void deleteResume(String id){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                loading.dismiss();
//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        ToastUtils.show(mContext,"删除成功");
                        handler.sendEmptyMessage(1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                loading.dismiss();
//                Log.v(TAG,new String(volleyError.networkResponse.data));
                ToastUtils.show(mContext,volleyError);
            }
        };
        Map<String,String> map = new HashMap<String,String>();
        map.put("uid", EggshellApplication.getApplication().getUser().getId()+"");//EggshellApplication.getApplication().getUser().getId()+""
        map.put("eid",resume.getRid()+"");
        map.put("id",id);
        map.put("type","2");
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_DELETE_RESUME_ITEM, false, map, true, listener, errorListener);
    }
}
