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
public class BookScanAdapter extends BaseAdapter{
    private Context mContext;
    private List<ResumeData> worklists;
    private String itemString;

    public BookScanAdapter(Context context, List<ResumeData> list,String book){
        this.mContext = context;
        this.worklists = list;
        this.itemString = book;
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
            convertView = LinearLayout.inflate(mContext, R.layout.activity_resume_item_list, null);

            viewHolder.id_name = (TextView)convertView.findViewById(R.id.id_name);
            viewHolder.id_time = (TextView)convertView.findViewById(R.id.id_time);
            viewHolder.id_department = (TextView)convertView.findViewById(R.id.id_department);
            viewHolder.id_content = (TextView)convertView.findViewById(R.id.id_content);

            viewHolder.tv_goon_name = (TextView) convertView.findViewById(R.id.tv_goon_name);
            viewHolder.tv_goon_time = (TextView) convertView.findViewById(R.id.tv_goon_time);
            viewHolder.tv_gon_department = (TextView) convertView.findViewById(R.id.tv_gon_department);
            viewHolder.tv_gon_position = (TextView) convertView.findViewById(R.id.tv_gon_position);
            viewHolder.tv_resume_goon_content = (TextView) convertView.findViewById(R.id.tv_resume_goon_content);

            viewHolder.ll_goon_position = (LinearLayout) convertView.findViewById(R.id.ll_goon_position);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (BookViewHolder)convertView.getTag();
        }

        if(itemString.equals("book")){
            viewHolder.ll_goon_position.setVisibility(View.GONE);
            viewHolder.tv_goon_name.setText("证书名称:");
            viewHolder.tv_goon_time.setText("颁发时间:");
            viewHolder.tv_gon_department.setText("颁发单位:");
            viewHolder.tv_resume_goon_content.setText("证书描述:");

        }

        viewHolder.id_time.setText(FormatUtils.timestampToDatetime(book.getSdate()));
        viewHolder.id_department.setText(book.getTitle());
        viewHolder.id_name.setText(book.getName());
        viewHolder.id_content.setText(book.getContent());

        return convertView;
    }


    class BookViewHolder{
        TextView tv_goon_name;
        TextView id_name;

        TextView tv_goon_time;
        TextView id_time;

        TextView tv_gon_department;
        TextView id_department;

        TextView tv_gon_position;
        TextView id_position;

        TextView tv_resume_goon_content;
        TextView id_content;

        LinearLayout ll_goon_position;
    }
}
