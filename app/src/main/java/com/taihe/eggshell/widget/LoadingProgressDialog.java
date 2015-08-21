package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.Window;
import android.widget.TextView;

import com.taihe.eggshell.R;

public class LoadingProgressDialog extends Dialog {

    private Context mContext;
    private String message;

	public LoadingProgressDialog(Context context, String strMessage) {
        super(context);

        this.mContext = context;
        this.message = strMessage;

        initView();
	}

    private void initView(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new BitmapDrawable());

        setContentView(R.layout.progressdialog_item);

        TextView tvMsg = (TextView) this.findViewById(R.id.dialog_text);
        if (tvMsg != null) {
            tvMsg.setText(message);
        }
    }

}
