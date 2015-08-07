package com.taihe.eggshell.job.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihe.eggshell.R;

/**
 * Created by huan on 2015/8/6.
 */
public class AllJobFragment extends Fragment {

    private ListView list_job_all;
    private View rootView;
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_job_all, null) ;

        mContext = getActivity();
        initView();
        initDate();
        return rootView ;
    }



    private void initView() {
        list_job_all = (ListView) rootView.findViewById(R.id.list_job_all);


    }


    private void initDate() {
        AllJobAdapter adapter = new AllJobAdapter();
        list_job_all.addFooterView(View.inflate(mContext,R.layout.list_job_all_footer,null));
        list_job_all.setAdapter(adapter);


    }

    private class AllJobAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view;
            ViewHolder holder;
            if(convertView != null){
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }else{

                view = View.inflate(mContext,R.layout.list_job_all,null);
                holder = new ViewHolder();

                holder.cb_select = (CheckBox) view.findViewById(R.id.cb_listjob_select);
                holder.tv_jobName = (TextView) view.findViewById(R.id.tv_listjob_jobname);
                holder.tv_businessName = (TextView) view.findViewById(R.id.tv_listjob_businessname);
                holder.tv_city = (TextView) view.findViewById(R.id.tv_listjob_city);
                holder.tv_edu = (TextView) view.findViewById(R.id.tv_listjob_edu);
                holder.tv_pubTiem = (TextView) view.findViewById(R.id.tv_listjob_pubtime);
                holder.tv_salaryRange = (TextView) view.findViewById(R.id.tv_listjob_salaryrange);

                view.setTag(holder);

                holder.cb_select.setChecked(true);
                holder.tv_businessName.setText("太和天下");
            }
            return view;
        }
    }

    class ViewHolder{

        CheckBox cb_select;
        TextView tv_jobName, tv_businessName,tv_city,tv_edu,tv_pubTiem,tv_salaryRange;
    }
}
