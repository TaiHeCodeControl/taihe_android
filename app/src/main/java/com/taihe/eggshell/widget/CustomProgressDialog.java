package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.taihe.eggshell.R;

public class CustomProgressDialog extends Dialog {

	public CustomProgressDialog(Context context, String strMessage) {
		this(context, R.style.CustomProgressDialog, strMessage);
	}

	public CustomProgressDialog(Context context, int theme, String message) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.setContentView(R.layout.progressdialog_item);

		this.getWindow().getAttributes().gravity = Gravity.CENTER;
		this.setCanceledOnTouchOutside(false);
		TextView tvMsg = (TextView) this.findViewById(R.id.dialog_text);
		if (tvMsg != null) {
			tvMsg.setText(message);
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		if (!hasFocus) {
			dismiss();
			cancel();
		}
	}

}
