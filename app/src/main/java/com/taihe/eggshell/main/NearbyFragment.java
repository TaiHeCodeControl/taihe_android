package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.taihe.eggshell.R;
import com.taihe.eggshell.map.LocationMapActivity;

public class NearbyFragment extends Fragment implements View.OnClickListener{


	private Button btn_map;
	private View v;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){
		v = inflater.inflate(R.layout.fragment_around, null) ;
		ViewUtils.inject(getActivity());
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}

	public void init(){
		btn_map = (Button)v.findViewById(R.id.btn_around_map);
		btn_map.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_around_map:
				Intent intent = new Intent(getActivity(),LocationMapActivity.class);
				startActivity(intent);
				break;
		}
	}
}