package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taihe.eggshell.R;
import com.taihe.eggshell.map.LocationMapActivity;

public class NearbyFragment extends Fragment implements View.OnClickListener{
    private TextView id_title;
    private LinearLayout lin_back;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_around, null) ;
        lin_back = (LinearLayout)v.findViewById(R.id.lin_back);
        id_title = (TextView) v.findViewById(R.id.id_title);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	public void init(){
        id_title.setText("玩出范");
        lin_back.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){

		}
	}
}