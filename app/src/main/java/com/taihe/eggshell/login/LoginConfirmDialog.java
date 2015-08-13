package com.taihe.eggshell.login;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.widget.ChoiceDialog;

/**
 * Created by huan on 2015/8/12.
 */
public class LoginConfirmDialog {

    private static ChoiceDialog dialog;
    public static void dialogShow(final Context mContext) {


        dialog = new ChoiceDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
                //虚拟点击事件
//                radio_index.performClick();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);

            }
        });

        dialog.getTitleText().setText("亲，您还没有登录呢~");
        dialog.getLeftButton().setText("取消");
        dialog.getRightButton().setText("登录");

        dialog.show();
    }
}