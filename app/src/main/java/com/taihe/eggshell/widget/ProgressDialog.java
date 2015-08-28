package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taihe.eggshell.R;

/**
 * Created by wang on 2015/8/17.
 */
public class ProgressDialog extends Dialog{

    private ProgressBar progressBar;

    public ProgressDialog(Context context){
        super(context);
        initView();
    }

    private void initView(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.upload_progress);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        setCanceledOnTouchOutside(false);
        progressBar = (ProgressBar)findViewById(R.id.update_progress);

        TextView backUpdate = (TextView)findViewById(R.id.id_persent);
        backUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
