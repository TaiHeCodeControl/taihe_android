package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.widget.ProgressBar;

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
        setContentView(R.layout.upload_progress);
        progressBar = (ProgressBar)findViewById(R.id.update_progress);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
