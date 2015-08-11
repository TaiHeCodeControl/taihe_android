package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;

/**
 * Created by huan on 2015/8/11.
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private Context context;
    private TextView tv_searchHistory;

    public SearchHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 5;
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
        if (convertView == null) {
            view = View.inflate(context,
                    R.layout.list_search_history, null);
            tv_searchHistory = (TextView) view.findViewById(R.id.tv_list_search_history);
        } else {
            view = convertView;
        }

        return view;

    }

}
