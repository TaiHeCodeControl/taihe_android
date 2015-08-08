package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.activity.FindJobActivity;
import com.taihe.eggshell.main.adapter.ImgAdapter;
import com.taihe.eggshell.main.adapter.IndustryAdapter;
import com.taihe.eggshell.main.adapter.RecommendAdapter;
import com.taihe.eggshell.main.entity.Industry;
import com.taihe.eggshell.main.entity.Professional;
import com.taihe.eggshell.main.entity.RecommendCompany;
import com.taihe.eggshell.widget.ImagesGallery;
import com.taihe.eggshell.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "IndexFragment";
    private Context mContext;

    private Intent intent;

    private View rootView;
    private LinearLayout linearLayoutFos;
    private ImagesGallery gallery;
    private GridView companyGridView;
    private MyListView positionListView;
    private TextView btn_job;

    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> portImg = new ArrayList<ImageView>();
    private List<RecommendCompany> companylogolist;
    private List<Industry> industryList;

    private int[] imageResId; // 图片ID
    private int current = 0;
    private int preSelImgIndex = 0;

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
        gallery = (ImagesGallery) rootView.findViewById(R.id.gallery);
        linearLayoutFos = (LinearLayout)rootView.findViewById(R.id.id_linear_fos);
        btn_job = (TextView)rootView.findViewById(R.id.btn_index_job);

        companyGridView = (GridView)rootView.findViewById(R.id.id_company_list);
        positionListView = (MyListView)rootView.findViewById(R.id.id_position_listview);
        btn_job.setOnClickListener(this);
    }

    private void initData(){

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
        RecommendAdapter recommendAdapter = new RecommendAdapter(mContext,companylogolist);
        companyGridView.setAdapter(recommendAdapter);
        recommendAdapter.notifyDataSetChanged();

        getPostions();
        IndustryAdapter industryAdapter = new IndustryAdapter(mContext,industryList);
        positionListView.setAdapter(industryAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*case R.id.id_to_videolist:
                intent = new Intent(mContext,VideoListActivity.class);
                startActivity(intent);
                break;*/

            /*case R.id.tv_place:
                intent = new Intent(mContext,CitySelectActivity.class);
                startActivity(intent);
                break;*/

            case R.id.btn_index_job:
                intent = new Intent(mContext,FindJobActivity.class);
                startActivity(intent);
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

    private void getCompanyLogo(){
        companylogolist = new ArrayList<RecommendCompany>();
        int[] logo = new int[] { R.drawable.yahu, R.drawable.nike, R.drawable.zendesk,R.drawable.hp,R.drawable.mobify,R.drawable.holastic};
        for(int i=0;i<6;i++){
            RecommendCompany company = new RecommendCompany();
            company.setId(i);
            company.setImgsrc(logo[i]);
            companylogolist.add(company);
        }
    }

    private void getPostions(){
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
}