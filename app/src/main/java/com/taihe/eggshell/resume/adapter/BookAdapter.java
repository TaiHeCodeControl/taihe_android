package com.taihe.eggshell.resume.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.FormatUtils;
import com.taihe.eggshell.resume.entity.ResumeData;

import java.util.List;

/**
 * Created by wang on 2015/9/10.
 */
public class BookAdapter extends BaseAdapter{
    private Context mContext;
    private List<ResumeData> worklists;

    public BookAdapter(Context context,List<ResumeData> list){
        this.mContext = context;
        this.worklists = list;
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
        ResumeData book = worklists.get(position);
        BookViewHolder viewHolder;
        if(null==convertView) {
            viewHolder = new BookViewHolder();
            convertView = LinearLayout.inflate(mContext, R.layout.item_book, null);

            viewHolder.booktime = (TextView)convertView.findViewById(R.id.id_time_book);
            viewHolder.bookname = (TextView)convertView.findViewById(R.id.id_book_name);
            viewHolder.bookcompany = (TextView)convertView.findViewById(R.id.id_book_from);
            viewHolder.bookbrief = (TextView)convertView.findViewById(R.id.id_book_brief);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (BookViewHolder)convertView.getTag();
        }

        viewHolder.booktime.setText(FormatUtils.timestampToDatetime(book.getSdate()));
        viewHolder.bookcompany.setText(book.getTitle());
        viewHolder.bookname.setText(book.getName());
        viewHolder.bookbrief.setText(book.getContent());

        return convertView;
    }

    class BookViewHolder{
        TextView booktime;
        TextView bookcompany;
        TextView bookname;
        TextView bookbrief;
    }
}
