package com.taihe.eggshell.meetinginfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.main.mode.PlayInfoMode;
import com.taihe.eggshell.meetinginfo.InfoDetailActivity;
import com.taihe.eggshell.meetinginfo.entity.InfoDetailMode;
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
            viewHolder.mainHead = (ImageView)convertView.findViewById(R.id.chatlist_img_head);
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
        int totalHeight = 0;
        for (int i = 0, len = childAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = childAdapter.getView(i, null, viewHolder.childListView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = viewHolder.childListView.getLayoutParams();
        params.height = totalHeight
                + (viewHolder.childListView.getHeight() * (childAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        viewHolder.childListView.setLayoutParams(params);
        viewHolder.chatlist_lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InfoDetailActivity.ShowChatSend(true,list.get(position).getUname(),list.get(position).getUsername());
//                ToastUtils.show(mContext, list.get(position).getD_coment());
            }
        });
        viewHolder.childListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InfoDetailActivity.ShowChatSend(true,list.get(position).getChild().get(i).getRname(),list.get(position).getChild().get(i).getUsername());
//                ToastUtils.show(mContext,list.get(position).getChild().get(i).getR_coment());
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
        ImageView mainHead;
        LinearLayout childLinView,chatlist_lin_main;
        MyListView childListView;
    }
    public class ChildAdapter extends BaseAdapter{
        private  List<InfoDetailMode.ChildEntity> clist;
        private Context context;
        private String rname,rusername;
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
            if(!"".equals(rname)){
                viewHolder.reTitle.setText(rname+":　"+clist.get(position).getR_coment()+"　"+clist.get(position).getAddtime());
            }else{
                viewHolder.reTitle.setText(rusername+":　"+clist.get(position).getR_coment()+"　"+clist.get(position).getAddtime());
            }
            return childView;
        }
    }
    class ViewChildHolder{
        TextView reTitle;
    }
}
