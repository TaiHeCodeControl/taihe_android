package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.personalCenter.activity.MyPostActivity;
import com.taihe.eggshell.personalCenter.activity.SetUpActivity;

public class MeFragment extends Fragment implements View.OnClickListener{

    private Context mContext;

    private View rootView;
    private RelativeLayout rl_setting;
    private TextView tv_post, tv_intension,tv_collect;
    private Intent intent;

    @Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_me, null) ;
		return rootView ;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_post = (TextView) rootView.findViewById(R.id.tv_mine_post);
        tv_intension = (TextView) rootView.findViewById(R.id.tv_mine_jobintension);
        tv_collect = (TextView) rootView.findViewById(R.id.tv_mine_mycollect);
        rl_setting = (RelativeLayout)rootView.findViewById(R.id.rl_mine_setting);

        tv_post.setOnClickListener(this);
        tv_intension.setOnClickListener(this);
        tv_collect.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mine_post:
                intent = new Intent(mContext,MyPostActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mine_jobintension:
                intent = new Intent(mContext,MyPostActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_mine_mycollect:
                intent = new Intent(mContext,MyPostActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_setting:
                intent = new Intent(mContext, SetUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}