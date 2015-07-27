package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.videoplay.VideoListActivity;

public class IndexFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "IndexFragment";
    private Context mContext;

    private View rootView;
    private ImageView toVideoImg;
    private TextView titleText;

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
    }

    private void initView(){
        toVideoImg = (ImageView)rootView.findViewById(R.id.id_to_videolist);
        toVideoImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_to_videolist:
                Intent intent = new Intent(mContext,VideoListActivity.class);
                startActivity(intent);
                break;
        }
    }
}