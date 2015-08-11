package com.taihe.eggshell.job.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.bean.SearchHistory;

import java.util.List;

/**
 * Created by huan on 2015/8/11.
 */
public class SearchHistoryAdapter extends BaseAdapter {

    private Context context;
    private TextView tv_searchHistory;
    private List<SearchHistory> historyList;

    public SearchHistoryAdapter(Context context,List<SearchHistory> list) {
        this.context = context;
        this.historyList = list;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Object getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        SearchHistory history = historyList.get(i);
        HistoryViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new HistoryViewHolder();
            convertView = View.inflate(context,R.layout.list_search_history, null);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_list_search_history);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryViewHolder)convertView.getTag();
        }

        viewHolder.textView.setText(history.getName());

        return convertView;
    }

    class HistoryViewHolder{
        TextView textView;
    }

}
