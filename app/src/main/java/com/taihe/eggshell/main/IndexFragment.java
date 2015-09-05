package com.taihe.eggshell.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.Constants;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.APKUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateHelper;
import com.taihe.eggshell.job.activity.FindJobActivity;
import com.taihe.eggshell.job.activity.JobSearchActivity;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.adapter.ImgAdapter;
import com.taihe.eggshell.main.adapter.IndustryAdapter;
import com.taihe.eggshell.main.adapter.RecommendAdapter;
import com.taihe.eggshell.main.adapter.VideoAdapterHead;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;
import com.taihe.eggshell.main.entity.RecommendCompany;
import com.taihe.eggshell.meetinginfo.Act_MeetingInfo;
import com.taihe.eggshell.resume.ResumeManagerActivity;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
import com.taihe.eggshell.widget.LoadingProgressDialog;
import com.taihe.eggshell.widget.ImagesGallery;
import com.taihe.eggshell.widget.MyListView;
import com.taihe.eggshell.widget.MyScrollView;
import com.taihe.eggshell.widget.ProgressDialog;
import com.taihe.eggshell.widget.UpdateDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "IndexFragment";
    private Context mContext;

    private Intent intent;
    private View rootView;
    private LinearLayout linearLayoutFos,indexTitleView;
    private RelativeLayout searchRelativeLayout;
    private ImagesGallery gallery;
    private GridView companyGridView;
    private MyListView positionListView;
    private MyScrollView scrollView;
    private TextView lookJob,jianZhi,shiXi,newInfos,writeResume,playMode,weChat,publicClass,jobPlace;
    private UpdateDialog dialog;

    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> portImg = new ArrayList<ImageView>();
    private List<RecommendCompany> companylogolist;
    private List<Industry> industryList;

    private int[] imageResId; // 图片ID
    private int current = 0;
    private int preSelImgIndex = 0;
    private static final int ALPHA_START=0;
    private static final int ALPHA_END=180;
    private static final int ALPHA_MESSAGE = 1;
    List<VideoInfoMode> listInfo;
    private ChangeViewPagerListener changeViewPagerListener;

    public interface ChangeViewPagerListener{
        void changeViewPager(int position);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ALPHA_MESSAGE:
                    if(null!=msg.obj){
                        indexTitleView.getBackground().setAlpha((Integer)msg.obj);
                    }
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            changeViewPagerListener = (ChangeViewPagerListener)activity;
        }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnArticleSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        mContext = getActivity();
        rootView = inflater.inflate(R.layout.fragment_index, null);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView(){
        jobPlace = (TextView)rootView.findViewById(R.id.id_job_place);
        scrollView = (MyScrollView)rootView.findViewById(R.id.id_index_scroll);
        indexTitleView = (LinearLayout)rootView.findViewById(R.id.id_index_title);
        searchRelativeLayout = (RelativeLayout)rootView.findViewById(R.id.id_search_job);
        gallery = (ImagesGallery) rootView.findViewById(R.id.gallery);
        linearLayoutFos = (LinearLayout)rootView.findViewById(R.id.id_linear_fos);
        lookJob = (TextView)rootView.findViewById(R.id.id_look_job);
        jianZhi = (TextView)rootView.findViewById(R.id.id_look_jianzhi);
        shiXi = (TextView)rootView.findViewById(R.id.id_look_shixi);
        newInfos = (TextView)rootView.findViewById(R.id.id_information);
        writeResume = (TextView)rootView.findViewById(R.id.id_write_resume);
        playMode = (TextView)rootView.findViewById(R.id.id_play_mode);
        weChat = (TextView)rootView.findViewById(R.id.id_we_chat);
        publicClass = (TextView)rootView.findViewById(R.id.id_public_class);
        listInfo = new ArrayList<VideoInfoMode>();
        companyGridView = (GridView)rootView.findViewById(R.id.id_company_list);
        positionListView = (MyListView)rootView.findViewById(R.id.id_position_listview);
        lookJob.setOnClickListener(this);
        jianZhi.setOnClickListener(this);
        shiXi.setOnClickListener(this);
        newInfos.setOnClickListener(this);
        writeResume.setOnClickListener(this);
        playMode.setOnClickListener(this);
        weChat.setOnClickListener(this);
        publicClass.setOnClickListener(this);
        searchRelativeLayout.setOnClickListener(this);
//        jobPlace.setOnClickListener(this);
    }

    private void initData(){
        indexTitleView.getBackground().setAlpha(0);

        getGalleryImg();
        gallery.setAdapter(new ImgAdapter(mContext, imageViews));
        gallery.setFocusable(true);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1,int selIndex, long arg3) {
                selIndex = selIndex % imageViews.size();
                // 修改上一次选中项的背景
                portImg.get(preSelImgIndex).setImageResource(R.drawable.ic_focus);
                // 修改当前选中项的背景
                portImg.get(selIndex).setImageResource(R.drawable.ic_focus_select);
                preSelImgIndex = selIndex;
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        gallery.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(mContext, (position % imageViews.size()) + "", Toast.LENGTH_LONG).show();
            }

        });

        getCompanyLogo();
//        RecommendAdapter recommendAdapter = new RecommendAdapter(mContext,companylogolist);
//        companyGridView.setAdapter(recommendAdapter);
//        recommendAdapter.notifyDataSetChanged();
//        companyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        ToastUtils.show(mContext,position+"");
//                        Intent intent = new Intent(mContext,CompanyDetailActivity.class);
//                        intent.putExtra("companyId",position);
//                        startActivity(intent);
//            }
//        });

        getIndustrys();
        IndustryAdapter industryAdapter = new IndustryAdapter(mContext,industryList);
        positionListView.setAdapter(industryAdapter);
        industryAdapter.notifyDataSetChanged();

        scrollView.setOnScrollChangeListener(new MyScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(int x, int y, int oldxX, int oldY) {
                Message message = Message.obtain();
                message.what = ALPHA_MESSAGE;
                if (oldY >= 0) {
                    message.obj = oldY * (ALPHA_END - ALPHA_START) / scrollView.getMaxScrollAmount() + ALPHA_START;
                }
                handler.sendMessage(message);

            }
        });

        getVersionCode();

//        FormatUtils.getMD5("你妹");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_job_place:
//                intent = new Intent(mContext, CitySelectActivity.class);
//                startActivity(intent);
                break;
            case R.id.id_search_job:
                intent = new Intent(mContext, JobSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.id_look_job:
                intent = new Intent(mContext,FindJobActivity.class);
                startActivity(intent);
                break;
            case R.id.id_look_jianzhi:
                intent = new Intent(mContext,FindJobActivity.class);
                startActivity(intent);
                break;
            case R.id.id_look_shixi:
                intent = new Intent(mContext,FindJobActivity.class);
                startActivity(intent);
                break;
            case R.id.id_information:
                intent = new Intent(mContext,Act_MeetingInfo.class);
                startActivity(intent);
                break;
            case R.id.id_write_resume:
                intent = new Intent(mContext,ResumeManagerActivity.class);
                startActivity(intent);
                /*if(null!= EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,ResumeManagerActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    startActivity(intent);
                }*/
                break;
            case R.id.id_play_mode:
                changeViewPagerListener.changeViewPager(1);
                break;
            case R.id.id_we_chat:
                ToastUtils.show(mContext,"敬请期待...");
                break;
            case R.id.id_public_class:
                changeViewPagerListener.changeViewPager(2);
                break;
        }
    }

    private void getGalleryImg(){
        imageResId = new int[] { R.drawable.img1, R.drawable.img2, R.drawable.img3};
        //轮播图片资源
        for (int i = 0; i < imageResId.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }
        //Focus图片
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView localImageView = new ImageView(mContext);
            localImageView.setId(i);
            localImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
            params.leftMargin = 10;
            params.bottomMargin = 10;
            localImageView.setLayoutParams(params);
            localImageView.setPadding(5, 5, 5, 5);
            localImageView.setImageResource(R.drawable.ic_focus);
            portImg.add(localImageView);
            linearLayoutFos.addView(localImageView);
        }
    }

    private void getIndustrys(){
        String[] type = new String[]{"互联网","金融行业","广告媒体"};
        String[] internet = new String[]{"网站策划","网站编辑","运营专员","SEO专员","UI设计","美工",};
        String[] bank = new String[]{"银行柜员","业务专员","清算员","操盘手","会计","出纳员",};
        String[] media = new String[]{"客户专员","创意专员","企业策划","规划设计","地产销售","测绘测量",};
        int[] img = new int[]{R.drawable.hulianwang,R.drawable.bank,R.drawable.media};
        industryList = new ArrayList<Industry>();
        for(int i=0;i<3;i++){
            List<Professional> prolist = new ArrayList<Professional>();
            Industry industry = new Industry();
            industry.setId(i);
            industry.setName(type[i]);
            industry.setImgsrc(img[i]);
            for(int j=0;j<6;j++){
                Professional professional = new Professional();
                professional.setId(i);
                if(i==0){
                    professional.setName(internet[j]);
                }else if(i==1){
                    professional.setName(bank[j]);
                }else if(i==2){
                    professional.setName(media[j]);
                }
                prolist.add(professional);
            }
            industry.setProfessionalList(prolist);
            industryList.add(industry);
        }
    }

    private void getVersionCode(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object o) {

                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0) {
//                        JSONObject data = jsonObject.getJSONObject("data");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                dialog = new UpdateDialog(mContext,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                },new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.show(mContext,"更新");
                        dialog.dismiss();
//                        updateAPK();
                    }
                });

                dialog.getTitleText().setText("发现新版本"+APKUtils.getVersionName());
//                dialog.show();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                    ToastUtils.show(mContext,volleyError.networkResponse.statusCode+mContext.getResources().getString(R.string.error_server));
            }
        };

        Map<String,String> params = new HashMap<String, String>();
        params.put("version", APKUtils.getVersionCode() + "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_UPDATE, true, params, true, listener, errorListener);
    }

    private void updateAPK(){

        final ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.show();

        new UpdateHelper(mContext,new UpdateHelper.DownloadProgress() {
            @Override
            public void progress(int percent) {
                    progressDialog.getProgressBar().setProgress(percent);
            }

            @Override
            public void complete() {
                progressDialog.dismiss();
            }

            @Override
            public void error() {
                ToastUtils.show(mContext,"错误");
            }
        }).downloadInBackground(Constants.UPDATE_APK_URL);
    }

    private void getCompanyLogo(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值
                try {
                    JSONObject jsonObject = new JSONObject((String) obj);
                    int code = jsonObject.getInt("code");
                    if (code == 0) {
                        try{
                            JSONArray j1 = jsonObject.getJSONArray("data");
                            JSONObject j2;
                            VideoInfoMode vMode;
                            for(int i=0;i<j1.length();i++){
                                vMode = new VideoInfoMode();
                                j2 = j1.getJSONObject(i);
                                vMode.setId(j2.optString("id").toString());
                                vMode.setC_id(j2.optString("uid").toString());
                                vMode.setVimage(j2.optString("hot_pic").toString());
                                listInfo.add(vMode);
                            }

                            RecommendAdapter recommendAdapter = new RecommendAdapter(mContext,listInfo);
                            companyGridView.setAdapter(recommendAdapter);
                            recommendAdapter.notifyDataSetChanged();
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                        // Log.e("data",data);
                    } else {
                        //String msg = jsonObject.getString("msg");
//                        ToastUtils.show(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {//返回值
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        String url = Urls.COMPY_LIST_URL;
        RequestUtils.createRequest(getActivity(), url, "", true, map, true, listener, errorListener);
    }

}