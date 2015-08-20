package com.taihe.eggshell.job.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;

/**
 * Created by huan on 2015/8/6.
 */
public class FujinFragment extends Fragment {
    private static final String TAG = "FujinFragment";
    private View rootView;
    private Context mContext;
   

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_job_fujin, null);
        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
