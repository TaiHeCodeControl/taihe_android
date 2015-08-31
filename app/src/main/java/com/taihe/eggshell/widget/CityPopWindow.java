package com.taihe.eggshell.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.taihe.eggshell.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/31.
 */
public class CityPopWindow {

    private Context mContext;
    private PopupWindow popupWindow;

    private List<String> citylist = new ArrayList<String>();

    public CityPopWindow(Context context){
        this.mContext = context;

        View view = LayoutInflater.from(mContext).inflate(R.layout.city_list,null);
        ListView listView = (ListView)view.findViewById(R.id.id_city_list);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        CityAdapter cityAdapter = new CityAdapter();
        listView.setAdapter(cityAdapter);

        citylist.add("东城区");
        citylist.add("西城区");
        citylist.add("朝阳区");
        citylist.add("海淀区");
    }

    public void setAsDropDown(View parent){
        popupWindow.showAsDropDown(parent);
        popupWindow.update();
    }

    public class CityAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(citylist!=null && citylist.size()>0){
                return citylist.size();
            }else{
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return citylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = citylist.get(position);

            CityViewHolder viewHolder;
            if(convertView!=null){
                viewHolder = (CityViewHolder)convertView.getTag();
            }else{
                viewHolder = new CityViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address,null);
                viewHolder.cityName = (TextView)convertView.findViewById(R.id.textViewName);
                convertView.setTag(viewHolder);
            }

            viewHolder.cityName.setText(name);
            return convertView;
        }
    }

    class CityViewHolder{
        TextView cityName;
    }

}
