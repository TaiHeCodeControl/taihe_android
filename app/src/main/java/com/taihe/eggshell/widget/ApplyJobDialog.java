package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;

/**
 * Created by wang on 2015/8/11.
 */
public class ApplyJobDialog extends Dialog{

    private Context mContext;
    private View.OnClickListener applyListener;

    public ApplyJobDialog(Context context,View.OnClickListener listener) {
        super(context);
        this.mContext = context;
        this.applyListener = listener;
        init();
    }

    private void init(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_isapplyjob);

        final ImageView iv_cancel = (ImageView)findViewById(R.id.iv_isapplyjob_cancel);
        final Button btn_ok = (Button)findViewById(R.id.btn_isapplyjob_ok);
        btn_ok.setOnClickListener(applyListener);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext,"小时");
                dismiss();
            }
        });
    }


}
