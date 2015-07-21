package com.taihe.eggshell.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.ChoiceDialog;

public class IndexFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "IndexFragment";
    private Context mContext;

    private View rootView;
    private ImageView backImgView;
    private TextView titleText;
    private ChoiceDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        mContext = getActivity();
        rootView = inflater.inflate(R.layout.index_fragment, null);
		return rootView;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dialog = new ChoiceDialog(mContext,new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext,"进入");
            }
        },new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext,"hello");
            }
        });
//        dialog.show();
        Log.v(TAG,"index");
    }

    @Override
    public void onClick(View v) {

    }
}