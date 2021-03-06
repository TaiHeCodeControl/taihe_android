package com.taihe.eggshell.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.ToastUtils;

/**
 * Created by huan on 2015/8/11.
 */
public class JobApplyDialogUtil {

    private static MyDialog isApplyDialog = null;

    public static void isApplyJob(final Context mContext, int selectNum, int postedNum) {

        View view = View.inflate(mContext, R.layout.dialog_isapplyjob,
                null);

        TextView tv_isapplyjob_resumename = (TextView) view.findViewById(R.id.tv_isapplyjob_resumename);
        if (postedNum != 0) {
            tv_isapplyjob_resumename.setText("您投递" + selectNum + "个职位，其中" + postedNum + "个已投递，一周内不能重复投递");
        } else {
            tv_isapplyjob_resumename.setText("您投递" + selectNum + "个职位");
        }
        final ImageView iv_cancel = (ImageView) view.findViewById(R.id.iv_isapplyjob_cancel);
        final Button btn_ok = (Button) view.findViewById(R.id.btn_isapplyjob_ok);


        iv_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isApplyDialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                postJob();

                ToastUtils.show(mContext, "职位投递成功");
                isApplyDialog.dismiss();
            }
        });

        isApplyDialog = new MyDialog(mContext, view, R.style.mydialog_style);
        isApplyDialog.show();
    }
}
