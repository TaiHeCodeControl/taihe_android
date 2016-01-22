package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.meetinginfo.entity.InfoDetailMode;
import com.taihe.eggshell.widget.CircleImageView;
import com.taihe.eggshell.widget.MyListView;

import net.tsz.afinal.FinalBitmap;

import java.util.List;

/**
 * 视频列表数据
 */
public class InfoDetailAdapter extends BaseAdapter{

    private Context mContext;
    private  List<InfoDetailMode> list;
    private int type;
    private String mainName;
    public int childHeight;
    public InfoDetailAdapter(Context context){
        this.mContext = context;
    }
    public void setPlayData( List<InfoDetailMode> list,int type){
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if(null==convertView){
            viewHolder = new ViewHolder();
            convertView = LinearLayout.inflate(mContext,R.layout.info_chat_list_mb, null);
            viewHolder.mainTitle = (TextView)convertView.findViewById(R.id.chatlist_txt_title);
            viewHolder.mainName = (TextView)convertView.findViewById(R.id.chatlist_txt_name);
            viewHolder.mainDate = (TextView)convertView.findViewById(R.id.chatlist_txt_date);
            viewHolder.countNum = (TextView)convertView.findViewById(R.id.chatlist_txt_count);
            viewHolder.mainHead = (CircleImageView)convertView.findViewById(R.id.chatlist_img_head);
            viewHolder.childListView = (MyListView)convertView.findViewById(R.id.chatlist_child_listview);
            viewHolder.childLinView = (LinearLayout)convertView.findViewById(R.id.chatlist_child_lin);
            viewHolder.chatlist_lin_main = (LinearLayout)convertView.findViewById(R.id.chatlist_lin_main);
            viewHolder.lineview = (View)convertView.findViewById(R.id.viewline);
            viewHolder.childlineview = (View)convertView.findViewById(R.id.child_view_line);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(position!=0){
            viewHolder.countNum.setVisibility(View.GONE);
            viewHolder.lineview.setVisibility(View.GONE);
        }else{
            viewHolder.countNum.setVisibility(View.VISIBLE);
            viewHolder.lineview.setVisibility(View.VISIBLE);
        }
        if(list.get(position).getChild().size()==0){
            viewHolder.childlineview.setVisibility(View.GONE);
        }else{
            viewHolder.childlineview.setVisibility(View.VISIBLE);
        }
        viewHolder.mainTitle.setText(list.get(position).getD_coment());
        if(!"".equals(list.get(position).getUname())){
            mainName =list.get(position).getUname();
        }else{
            mainName =list.get(position).getUsername();
        }
        viewHolder.mainName.setText(mainName);
        viewHolder.countNum.setText(list.size()+"条评论");
        viewHolder.mainDate.setText(list.get(position).getAddtime());
        String aaa = list.get(position).getUphoto().toString();;
        viewHolder.mainHead.setImageResource(R.drawable.touxiang);
        if(!"".equals(list.get(position).getUphoto().toString())) {
            FinalBitmap bitmap = FinalBitmap.create(mContext);
            bitmap.display(viewHolder.mainHead, list.get(position).getUphoto().toString());
        }
        ChildAdapter childAdapter = new ChildAdapter(list.get(position).getChild(),mContext);
        viewHolder.childListView.setAdapter(childAdapter);
        viewHolder.chatlist_lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDetailActivity.ShowChatSend(true, list.get(position).getUname(), list.get(position).getUsername(), list.get(position).getD_id(), list.get(position).getUid());
            }
        });
        viewHolder.childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InfoDetailActivity.ShowChatSend(true,
                        list.get(position).getChild().get(i).getUname(),
                        list.get(position).getChild().get(i).getUsername(),
                        list.get(position).getD_id(),
                        list.get(position).getChild().get(i).getUid());
            }
        });
        return convertView;
    }
    public View getChildView(String rname,String rdate,String rtitle,String rusername){
        View view = LinearLayout.inflate(mContext,R.layout.info_chat_child_mb,null);
        TextView reTitle = (TextView) view.findViewById(R.id.chatchild_txt_title);
        if(!"".equals(rname)){
            reTitle.setText(rname+":　"+rtitle+"　"+rdate);
        }else{
            reTitle.setText(rusername+":　"+rtitle+"　"+rdate);
        }
        childHeight = childHeight+reTitle.getLineHeight();
        return view;
    }
    class ViewHolder{
        TextView mainTitle,mainName,mainDate,countNum;
        CircleImageView mainHead;
        LinearLayout childLinView,chatlist_lin_main;
        MyListView childListView;
        View lineview,childlineview;
    }
    public class ChildAdapter extends BaseAdapter{
        private  List<InfoDetailMode.ChildEntity> clist;
        private Context context;
        private String rname,rusername,uname,rdate,rcontent;
        SpannableString mspk;
        public ChildAdapter(List<InfoDetailMode.ChildEntity> clist,Context context){
            this.clist = clist;
            this.context = context;
        }
        @Override
        public int getCount() {
            return clist.size();
        }

        @Override
        public Object getItem(int position) {
            return clist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View childView, ViewGroup parent) {
            ViewChildHolder viewHolder;
            if (null == childView) {
                viewHolder = new ViewChildHolder();
                childView = LinearLayout.inflate(context, R.layout.info_chat_child_mb, null);
                viewHolder.reTitle = (TextView)childView.findViewById(R.id.chatchild_txt_title);
                childView.setTag(viewHolder);
            } else {
                viewHolder = (ViewChildHolder) childView.getTag();
            }
            rname = clist.get(position).getRname();
            rusername = clist.get(position).getRusername();
            if(!"".equals(clist.get(position).getUname())){
                uname = clist.get(position).getUname();
            }else {
                uname = clist.get(position).getUsername();
            }
            rdate = clist.get(position).getAddtime();

            if(!"".equals(rname)){
                rcontent = uname+":　回复:"+rname+":"+clist.get(position).getR_coment()+"　"+rdate;
//                viewHolder.reTitle.setText(rcontent);
            }else{
                rcontent = uname+":　回复:"+rusername+":"+clist.get(position).getR_coment()+"　"+rdate;
//                viewHolder.reTitle.setText(rcontent);
            }
            mspk = new SpannableString(rcontent);
            int k = (uname).length();
            mspk.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.next_step_color)),0, k+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.reTitle.setText(mspk);
            k = rcontent.length();
            mspk.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.font_color_gray_jobsearch)),k-rdate.length(), k,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.reTitle.setText(mspk);
            return childView;
        }
    }
    class ViewChildHolder{
        TextView reTitle;
    }
}
