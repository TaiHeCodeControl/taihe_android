package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taihe.eggshell.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/31.
 */
public class CityDialog extends Dialog{

    private Context mContext;
    private CityClickListener cityClickListener;
    private List<String> citylist = new ArrayList<String>();

    public interface CityClickListener{
        void cityId(String id);
    }

    public CityDialog(Context context,CityClickListener listener) {
        super(context);
        this.mContext = context;
        this.cityClickListener = listener;

        initview();
    }

    private void initview(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.city_list);
        getWindow().setBackgroundDrawable(new BitmapDrawable());

        ListView listView = (ListView)findViewById(R.id.id_city_list);
        CityAdapter cityAdapter = new CityAdapter();
        listView.setAdapter(cityAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cityClickListener.cityId(citylist.get(position));
            }
        });

        citylist.add("东城区");
        citylist.add("西城区");
        citylist.add("朝阳区");
        citylist.add("海淀区");

        cityAdapter.notifyDataSetChanged();
    }

    public class CityAdapter extends BaseAdapter {

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city,null);
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
