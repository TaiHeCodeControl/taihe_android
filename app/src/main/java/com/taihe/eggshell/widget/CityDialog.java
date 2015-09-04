package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chinaway.framework.swordfish.db.sqlite.Selector;
import com.chinaway.framework.swordfish.exception.DbException;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.DbHelper;
import com.taihe.eggshell.main.entity.CityBJ;
import com.taihe.eggshell.main.entity.StaticData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2015/8/31.
 */
public class CityDialog extends Dialog{

    private Context mContext;
    private CityClickListener cityClickListener;
    private List<CityBJ> citylist = new ArrayList<CityBJ>();
    private CityAdapter cityAdapter;
    private int page = 0;
    private int PAGE_SIZE = 100;

    public interface CityClickListener{
        void city(CityBJ id);
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

        getCityDataFromDB();

        ListView listView = (ListView)findViewById(R.id.id_city_list);
        cityAdapter = new CityAdapter();
        listView.setAdapter(cityAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    cityClickListener.city(citylist.get(position));
            }
        });

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
            CityBJ city = citylist.get(position);

            CityViewHolder viewHolder;
            if(convertView!=null){
                viewHolder = (CityViewHolder)convertView.getTag();
            }else{
                viewHolder = new CityViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_city,null);
                viewHolder.cityName = (TextView)convertView.findViewById(R.id.textViewName);
                convertView.setTag(viewHolder);
            }

            viewHolder.cityName.setText(city.getName());
            return convertView;
        }
    }

    class CityViewHolder{
        TextView cityName;
    }

    private void getCityDataFromDB() {
        final int offset = page * PAGE_SIZE;
        new AsyncTask<Void, Void, List<CityBJ>>() {
            @Override
            protected List<CityBJ> doInBackground(Void... params) {

                Selector selector = Selector.from(CityBJ.class).limit(PAGE_SIZE).offset(offset);

                List<CityBJ> list = null;
                try {
                    list = DbHelper.getDbUtils(DbHelper.DB_TYPE_USER).findAll(selector);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                return list;
            }

            protected void onPostExecute(List<CityBJ> result) {
                if (result != null && result.size() > 0) {
                    citylist.clear();
                    citylist.addAll(result);
                    cityAdapter.notifyDataSetChanged();
                } else {
                    Log.v("搜索：", "空");
                }

                super.onPostExecute(result);
            }
        }.execute();
    }
}
