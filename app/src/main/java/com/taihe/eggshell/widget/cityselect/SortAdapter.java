package com.taihe.eggshell.widget.cityselect;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Constants;

import java.util.ArrayList;
import java.util.List;

public class SortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SortModel> list = null;
	private Context mContext;

    private String content = "----蛋壳儿专注于教育行业的就业规划平台----帮助你就业求职找工作参加活动。http://www.eggker.cn/ap";

    public SortAdapter(Context mContext, List<SortModel> list) {
		this.mContext = mContext;
		this.list = list;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<SortModel> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SortModel mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.item_place, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            viewHolder.tvPhone = (TextView) view.findViewById(R.id.id_phone_number);
            viewHolder.tvVisited = (TextView) view.findViewById(R.id.id_is_invisited);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getSortLetters());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getName());
        if(this.list.get(position).getPhoneNum().contains("-")){
            viewHolder.tvPhone.setText(this.list.get(position).getPhoneNum().replace("-",""));
        }else{
            viewHolder.tvPhone.setText(this.list.get(position).getPhoneNum());
        }

        if(this.list.get(position).getIsVisited().equals("1")){
            viewHolder.tvVisited.setText("已添加");
            viewHolder.tvVisited.setTextColor(mContext.getResources().getColor(R.color.font_color_gray));
            viewHolder.tvVisited.setBackground(null);
            viewHolder.tvVisited.setClickable(false);
        }else{

            viewHolder.tvVisited.setClickable(true);
            viewHolder.tvVisited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendSMS(list.get(position).getPhoneNum(),content);
                }
            });
        }

		return view;

	}

    private void sendSMS(String phoneNumber, String message) {
        // ---sends an SMS message to another device---
        SmsManager sms = SmsManager.getDefault();

        // create the sentIntent parameter
        Intent sentIntent = new Intent(Constants.SENT_SMS_ACTION);
        sentIntent.putExtra("phoneNum",phoneNumber);
        PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, sentIntent, 0);

        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(Constants.DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(mContext, 0, deliverIntent, 0);

        //如果短信内容超过70个字符 将这条短信拆成多条短信发送出去
        if (message.length() > 70) {
            ArrayList<String> msgs = sms.divideMessage(message);
            for (String msg : msgs) {
                sms.sendTextMessage(phoneNumber, null, msg, sentPI, deliverPI);
            }
        } else {
            sms.sendTextMessage(phoneNumber, null, message, sentPI, deliverPI);
            //跳转到短信界面
            /*Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse("smsto:" + phoneNumber));
            sendIntent.putExtra("sms_body", message);
            mContext.startActivity(sendIntent);*/
        }
    }

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
        TextView tvPhone;
        TextView tvVisited;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}