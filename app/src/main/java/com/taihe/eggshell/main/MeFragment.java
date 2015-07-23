package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.personalCenter.activity.SetUpActivity;

public class MeFragment extends Fragment implements View.OnClickListener{

    private Context mContext;

    private View rootView;
    private RelativeLayout rl_setting;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_me, null) ;
		return rootView ;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        rl_setting = (RelativeLayout)rootView.findViewById(R.id.rl_mine_setting);

        rl_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_mine_setting:
                Intent intent = new Intent(mContext, SetUpActivity.class);
                startActivity(intent);
                break;
        }
    }
}