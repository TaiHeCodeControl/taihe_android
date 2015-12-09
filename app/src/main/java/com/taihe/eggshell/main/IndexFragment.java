package com.taihe.eggshell.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinaway.framework.swordfish.network.http.Response;
import com.chinaway.framework.swordfish.network.http.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.Urls;
import com.taihe.eggshell.base.utils.APKUtils;
import com.taihe.eggshell.base.utils.RequestUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.base.utils.UpdateHelper;
import com.taihe.eggshell.company.CompanyActivity;
import com.taihe.eggshell.job.activity.FindJobActivity;
import com.taihe.eggshell.job.activity.JobSearchActivity;
import com.taihe.eggshell.job.bean.JobFilterUtils;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.main.adapter.ImgAdapter;
import com.taihe.eggshell.main.adapter.IndustryAdapter;
import com.taihe.eggshell.main.adapter.RecommendAdapter;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;
import com.taihe.eggshell.main.entity.RecommendCompany;
import com.taihe.eggshell.meetinginfo.Act_MeetingInfo;
import com.taihe.eggshell.meetinginfo.VActivity;
import com.taihe.eggshell.resume.ResumeManagerActivity;
import com.taihe.eggshell.videoplay.mode.VideoInfoMode;
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
    private TextView lookJob,jianZhi,shiXi,newInfos,writeResume,playMode,weChat,publicClass,companyOrPerson;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            changeViewPagerListener = (ChangeViewPagerListener)context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString()+"must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        companyOrPerson = (TextView)rootView.findViewById(R.id.id_to_company);
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
        companyOrPerson.setOnClickListener(this);
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
//                Toast.makeText(mContext, (position % imageViews.size()) + "", Toast.LENGTH_LONG).show();
            }

        });

        getCompanyLogo();//名企企Logo
        getVersionCode();//检查版本
        getIndustrys();//行业列表
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_to_company:
                intent = new Intent(mContext, CompanyActivity.class);
                startActivity(intent);
                break;
            case R.id.id_search_job://搜索
                intent = new Intent(mContext, JobSearchActivity.class);
                intent.putExtra("From","Index");
                startActivity(intent);
                break;
            case R.id.id_look_job://社交圈
                intent = new Intent(mContext,Act_MeetingInfo.class);
//                intent = new Intent(mContext,SwipecardsActivity.class);
                startActivity(intent);
                break;
            case R.id.id_look_jianzhi://玩出范
//                changeViewPagerListener.changeViewPager(1);
                intent = new Intent(mContext,PlayStarActivity.class);
                startActivity(intent);
                break;
            case R.id.id_look_shixi://V达人
                intent = new Intent(mContext,VActivity.class);
//                intent = new Intent(mContext,CompanyJobListActivity.class);
                startActivity(intent);
                break;
            case R.id.id_information://去学习
//                changeViewPagerListener.changeViewPager(2);
                intent = new Intent(mContext,InternshipActivity.class);
                startActivity(intent);
                break;
            case R.id.id_write_resume:
                //搜索职位  全职
                JobFilterUtils.filterJob(mContext, "", "55", "", "", "", "", "", "", "", "","");
                intent = new Intent(mContext,FindJobActivity.class);
                intent.putExtra("jobtype","全职");
                startActivity(intent);
                break;
            case R.id.id_play_mode:
                //搜索职位  兼职
                JobFilterUtils.filterJob(mContext, "", "56", "", "", "", "", "", "", "", "", "");
                intent = new Intent(mContext,FindJobActivity.class);
                intent.putExtra("jobtype","兼职");
                startActivity(intent);
                break;
            case R.id.id_we_chat://找实习
                //搜索职位 实习
                JobFilterUtils.filterJob(mContext, "", "129", "", "", "", "", "", "", "", "","");
                intent = new Intent(mContext,FindJobActivity.class);
                intent.putExtra("jobtype","实习");
                startActivity(intent);
                break;
            case R.id.id_public_class:
                if(null!= EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,ResumeManagerActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    startActivity(intent);
                }
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
        String[] type = new String[]{"教育培训","综合类"};
        int[] id_type = new int[]{842,0};
        String[] media = new String[]{"市场专员","咨询销售","培训讲师","教学管理","教质管理","就业专员"};
        int[] id_media = new int[]{962,994,988,986,995,996};
        String[] internet = new String[]{"网站策划","网站编辑","运营专员","银行柜员","会计","出纳员"};
        int[] id_internet = new int[]{131,132,125,296,251,252};

        int[] img = new int[]{R.drawable.jiaoyupeixun,R.drawable.zonghe};
        industryList = new ArrayList<Industry>();

        for(int i=0;i<2;i++){
            List<Professional> prolist = new ArrayList<Professional>();
            Industry industry = new Industry();
            industry.setId(id_type[i]);
            industry.setName(type[i]);
            industry.setImgsrc(img[i]);
            for(int j=0;j<6;j++){
                Professional professional = new Professional();
                if(i==0){
                    professional.setId(id_media[j]);
                    professional.setName(media[j]);
                }else if(i==1){
                    professional.setId(id_internet[j]);
                    professional.setName(internet[j]);
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

//                Log.v(TAG,(String)o);
                try {
                    JSONObject jsonObject = new JSONObject((String)o);
                    int code = jsonObject.getInt("code");
                    if(code == 0) {
                        final String url = jsonObject.getString("data");
                        String title = jsonObject.getString("title");
                        String message = jsonObject.getString("message");
                        Gson gson = new Gson();
                        List<String> meglist = gson.fromJson(message,new TypeToken<List<String>>(){}.getType());

                        dialog = new UpdateDialog(mContext,title,meglist,new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        },new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                updateAPK(url);
                            }
                        });

                        dialog.getTitleText().setText("发现新版本");
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
//                    ToastUtils.show(mContext,volleyError);
            }
        };

        Map<String,String> params = new HashMap<String, String>();
        params.put("version", APKUtils.getVersionCode() + "");

        RequestUtils.createRequest(mContext, Urls.getMopHostUrl(), Urls.METHOD_UPDATE, true, params, true, listener, errorListener);
    }

    private void updateAPK(String url){

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
        }).downloadInBackground(url);
    }

    private void getCompanyLogo(){
        Response.Listener listener = new Response.Listener() {
            @Override
            public void onResponse(Object obj) {//返回值

//                Log.v(TAG, (String) obj);
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
                                vMode.setId(j2.optString("mid").toString());
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
            public void onErrorResponse(VolleyError volleyError) {
                
            }
        };

        Map<String,String> map = new HashMap<String,String>();
        String url = Urls.COMPY_LIST_URL;
        RequestUtils.createRequest(getActivity(), url, "", true, map, true, listener, errorListener);
    }

}