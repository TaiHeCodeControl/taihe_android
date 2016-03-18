package com.taihe.eggshell.personalCenter.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.personalCenter.fragment.MyDiscussFragment;
import com.taihe.eggshell.personalCenter.fragment.MyInviteFragment;
import com.taihe.eggshell.widget.TabPageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wang on 2016/3/9.
 */
public class MsgNotificationActivity extends FragmentActivity implements View.OnClickListener{

    private Context mContext;
    private ViewPager viewPager;
    private TabPageIndicator tabPageIndicator;
    private LinearLayout lin_back;
    private TextView titleTextView,editeButton;
    private boolean isedit = true;
    private PopupWindow editWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_msg_notification);

        mContext = this;
        lin_back = (LinearLayout)findViewById(R.id.lin_back);
        titleTextView = (TextView)findViewById(R.id.id_title);
        editeButton = (TextView)findViewById(R.id.id_commit);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        tabPageIndicator = (TabPageIndicator)findViewById(R.id.indicator);

        initData();
    }

    public void initData() {

        lin_back.setOnClickListener(this);
        titleTextView.setText("消息列表");
        editeButton.setText("编辑");
        editeButton.setVisibility(View.VISIBLE);
        editeButton.setOnClickListener(this);

        String[] pageTitle = new String[]{"面试邀请","评论回复"};
        ArrayList<Fragment> fragmentList = new ArrayList<Fragment>();
        Fragment discussfragment = new MyDiscussFragment();
        Fragment invitefragment = new MyInviteFragment();

        fragmentList.add(invitefragment);
        fragmentList.add(discussfragment);

        viewPager.setAdapter(new TabPageIndicatorAdapter(getSupportFragmentManager(), fragmentList,pageTitle));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        tabPageIndicator.setViewPager(viewPager);
        tabPageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.lin_back:
                finish();
                break;
            case R.id.id_commit:
                if(isedit){
                    isedit = false;
                    editeButton.setText("取消");
                }else{
                    isedit = true;
                    editeButton.setText("编辑");
                }

                showEditeDialog();
                break;
        }
    }

    private class TabPageIndicatorAdapter extends FragmentPagerAdapter{

        ArrayList<Fragment> list;
        String[] pageTitle;

        public TabPageIndicatorAdapter(FragmentManager fm,ArrayList<Fragment> fragmentlist,String[] title) {
            super(fm);
            this.list = fragmentlist;
            this.pageTitle = title;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle[position];
        }
    }


    private void showEditeDialog(){

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_edit_read, null);
        TextView cancelOK = (TextView) view.findViewById(R.id.id_is_edit);

        cancelOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewPager.getCurrentItem()==0){
                   MyInviteFragment myInviteFragment = (MyInviteFragment)getSupportFragmentManager().getFragments().get(0);
                   String str = myInviteFragment.getUnReadMsg();
                   if(TextUtils.isEmpty(str)){
                       ToastUtils.show(mContext,"暂无未读消息");
                   }else{
                       marksMessage(new String[]{str},0);
                   }
                }else{
                   MyDiscussFragment myDiscussFragment = (MyDiscussFragment)getSupportFragmentManager().getFragments().get(1);
                   String[] str = myDiscussFragment.getUnReadMsg();
                    if(TextUtils.isEmpty(str[0])){
                        ToastUtils.show(mContext,"暂无未读消息");
                    }else{
                        marksMessage(str,1);
                    }
                }

            }
        });

        editWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editWindow.setBackgroundDrawable(new BitmapDrawable());
        editWindow.showAtLocation(viewPager, Gravity.BOTTOM,0,0);
        editWindow.setOutsideTouchable(true);
        editWindow.setFocusable(true);
        editWindow.setAnimationStyle(R.style.mystyle);
        editWindow.update();

        editWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isedit = true;
                editeButton.setText("编辑");
            }
        });

    }

    private void marksMessage(String[] str,int page){

        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {
                    Log.v("TD:",(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0){
                        if(viewPager.getCurrentItem()==0){
                            MyInviteFragment myInviteFragment = (MyInviteFragment)getSupportFragmentManager().getFragments().get(0);
                            myInviteFragment.refreshMsg();
                        }else{
                            MyDiscussFragment myDiscussFragment = (MyDiscussFragment)getSupportFragmentManager().getFragments().get(1);
                            myDiscussFragment.refreshMsg();

                        }
                        editWindow.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        };

        Map<String,String> params = new HashMap<String,String>();
        String method = "";
        if(page == 0){
            method = Urls.METHOD_ALL_READ;
            params.put("ids",str[0]);
        }else{
            method = Urls.METHOD_ALL_DISS_READ;
            params.put("ids",str[0]);
            params.put("types",str[1]);
        }


        Log.v("TAG:", params.toString());
        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(),method,false,params,true,listener,errorListener);
    }


}
