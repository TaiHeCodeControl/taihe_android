package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
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
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        if(position!=0){
            viewHolder.countNum.setVisibility(View.GONE);
        }
        viewHolder.mainTitle.setText(list.get(position).getD_coment());
        if(!"".equals(list.get(position).getUname())){
            mainName =list.get(position).getUname();
        }else{
            mainName =list.get(position).getUsername();
        }
        viewHolder.childListView.setDividerHeight(0);
        viewHolder.mainName.setText(mainName);
        viewHolder.countNum.setText(list.size()+"条数据");
        viewHolder.mainDate.setText(list.get(position).getAddtime());
        FinalBitmap bitmap = FinalBitmap.create(mContext);
        bitmap.display(viewHolder.mainHead,list.get(position).getUphoto().toString());
        ChildAdapter childAdapter = new ChildAdapter(list.get(position).getChild(),mContext);
        viewHolder.childListView.setAdapter(childAdapter);
        viewHolder.chatlist_lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDetailActivity.ShowChatSend(true, list.get(position).getUname(), list.get(position).getUsername(), list.get(position).getD_id(), list.get(position).getUid());
            }
        });
//        viewHolder.chatlist_lin_main.scrollTo(0,);
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
    }
    public class ChildAdapter extends BaseAdapter{
        private  List<InfoDetailMode.ChildEntity> clist;
        private Context context;
        private String rname,rusername,uname;
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
            if(!"".equals(clist.get(position).getUsername())){
                uname = clist.get(position).getUsername();
            }else {
                uname = clist.get(position).getUname();
            }
            if(!"".equals(rname)){
                viewHolder.reTitle.setText(uname+":　回复:"+rname+":"+clist.get(position).getR_coment()+"　"+clist.get(position).getAddtime());
            }else{
                viewHolder.reTitle.setText(uname+":　回复:"+rusername+":"+clist.get(position).getR_coment()+"　"+clist.get(position).getAddtime());
            }

            return childView;
        }
    }
    class ViewChildHolder{
        TextView reTitle;
    }
}
