package com.taihe.eggshell.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.taihe.eggshell.R;

/**
 * Created by wang on 2015/8/18.
 */
public class UpdateDialog extends Dialog{

    private Context mContext;

    private Button leftButton,rightButton;
    private TextView titleText;
    private View.OnClickListener leftClickListener,rightClickListener;

    public UpdateDialog(Context context, View.OnClickListener leftListener, View.OnClickListener rightListener){
        super(context);

        this.mContext = context;
        this.leftClickListener = leftListener;
        this.rightClickListener = rightListener;
        initView();
    }

    private void initView(){

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.update_dialog);
        getWindow().setBackgroundDrawable(new BitmapDrawable());

        titleText = (TextView)findViewById(R.id.id_dialog_title);
        leftButton = (Button)findViewById(R.id.id_dialog_left);
        rightButton = (Button)findViewById(R.id.id_dialog_right);

        leftButton.setOnClickListener(leftClickListener);
        rightButton.setOnClickListener(rightClickListener);
    }

    public Button getLeftButton() {
        return leftButton;
    }

    public void setLeftButton(Button leftButton) {
        this.leftButton = leftButton;
    }

    public Button getRightButton() {
        return rightButton;
    }

    public void setRightButton(Button rightButton) {
        this.rightButton = rightButton;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }
}
