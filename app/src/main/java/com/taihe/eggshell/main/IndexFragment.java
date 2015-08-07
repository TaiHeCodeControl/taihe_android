package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihe.eggshell.R;
import com.taihe.eggshell.job.activity.FindJobActivity;
import com.taihe.eggshell.main.adapter.ImgAdapter;
import com.taihe.eggshell.widget.cityselect.CitySelectActivity;
import com.taihe.eggshell.videoplay.VideoListActivity;
import com.taihe.eggshell.widget.ImagesGallery;

import java.util.ArrayList;

public class IndexFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "IndexFragment";
    private Context mContext;

    private Intent intent;

    private TextView tv_place;
    private View rootView;
    private ImageView toVideoImg;
    private LinearLayout linearLayoutFos;
    private ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
    private ArrayList<ImageView> portImg = new ArrayList<ImageView>();
    private int[] imageResId; // 图片ID
    private int current = 0;
    private ImagesGallery gallery;
    private int preSelImgIndex = 0;

    private Button btn_job;

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
        toVideoImg = (ImageView)rootView.findViewById(R.id.id_to_videolist);
        linearLayoutFos = (LinearLayout)rootView.findViewById(R.id.id_linear_fos);
        tv_place = (TextView) rootView.findViewById(R.id.tv_place);
        btn_job = (Button)rootView.findViewById(R.id.btn_index_job);

        btn_job.setOnClickListener(this);
        toVideoImg.setOnClickListener(this);
        tv_place.setOnClickListener(this);
    }

    private void initData(){
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
            localImageView.setLayoutParams(new LinearLayout.LayoutParams(24, 24));
            localImageView.setPadding(5, 5, 5, 5);
            localImageView.setImageResource(R.drawable.ic_focus);
            portImg.add(localImageView);
            linearLayoutFos.addView(localImageView);
        }

        gallery = (ImagesGallery) rootView.findViewById(R.id.gallery);
        gallery.setAdapter(new ImgAdapter(mContext, imageViews));
        gallery.setFocusable(true);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int selIndex, long arg3) {
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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_to_videolist:
                intent = new Intent(mContext,VideoListActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_place:
                intent = new Intent(mContext,CitySelectActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_index_job:
                intent = new Intent(mContext,FindJobActivity.class);
                startActivity(intent);
                break;
        }
    }
}