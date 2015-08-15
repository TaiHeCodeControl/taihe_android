package com.taihe.eggshell.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.EggshellApplication;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.base.utils.ToastUtils;
import com.taihe.eggshell.job.activity.MyCollectActivity;
import com.taihe.eggshell.login.LoginActivity;
import com.taihe.eggshell.personalCenter.activity.AboutActivity;
import com.taihe.eggshell.job.activity.MyPostActivity;
import com.taihe.eggshell.personalCenter.activity.MyBasicActivity;
import com.taihe.eggshell.resume.ResumeManagerActivity;
import com.taihe.eggshell.widget.ChoiceDialog;

/**
 * 我的界面
 */
public class MeFragment extends Fragment implements View.OnClickListener{

    private Context mContext;

    private ChoiceDialog dialog;

    private View rootView;
    private RelativeLayout rl_setting,rl_editZiliao ,rl_post,rl_collect,rl_jianli,rl_about,rl_hezuo,rl_logout;
    private TextView tv_logintxt,tv_username, tv_qianming , tv_postNum, tv_collectNum , jianliNum;
    private LinearLayout ll_userinfo;

    private Intent intent;

    @Override
	public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savedInstanceState){

        mContext = getActivity();
		rootView = inflater.inflate(R.layout.fragment_me, null) ;
		return rootView ;
	}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rl_setting = (RelativeLayout)rootView.findViewById(R.id.rl_mine_setting);
        rl_editZiliao = (RelativeLayout)rootView.findViewById(R.id.rl_mine_editziliao);
        rl_post = (RelativeLayout)rootView.findViewById(R.id.rl_mine_postposition);
        rl_collect = (RelativeLayout)rootView.findViewById(R.id.rl_mine_collectpostion);
        rl_jianli = (RelativeLayout)rootView.findViewById(R.id.rl_mine_jianliguanli);
        rl_about = (RelativeLayout)rootView.findViewById(R.id.rl_mine_about);
        rl_hezuo = (RelativeLayout)rootView.findViewById(R.id.rl_mine_hezuoqudao);
        rl_logout = (RelativeLayout)rootView.findViewById(R.id.rl_mine_logout);

        ll_userinfo = (LinearLayout) rootView.findViewById(R.id.ll_mine_userinfo);
        tv_logintxt = (TextView) rootView.findViewById(R.id.tv_mine_logintxt);

        tv_logintxt.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_editZiliao.setOnClickListener(this);
        rl_post.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        rl_jianli.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_hezuo.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        initView();
    }

    private void initView() {
        if(null == EggshellApplication.getApplication().getUser()){

            tv_logintxt.setVisibility(View.VISIBLE);
            ll_userinfo.setVisibility(View.GONE);

            rl_logout.setVisibility(View.GONE);
        }

        dialog = new ChoiceDialog(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ToastUtils.show(mContext, "取消");
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                EggshellApplication.getApplication().setUser(null);
                PrefUtils.saveStringPreferences(mContext, PrefUtils.CONFIG, PrefUtils.KEY_USER_JSON, "");

                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.putExtra("LoginTag","logout");
                startActivity(intent);
            }
        });

        dialog.getTitleText().setText("确定退出当前账号吗？");
        dialog.getLeftButton().setText("以后再说");
        dialog.getRightButton().setText("确认退出");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_mine_logintxt://登录

                intent = new Intent(mContext,LoginActivity.class);
                intent.putExtra("LoginTag","meFragment");
                startActivity(intent);
                break;
            case R.id.rl_mine_editziliao://基本资料
//                LoginConfirmDialog.dialogShow(mContext);
                if(null != EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,MyBasicActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    intent.putExtra("LoginTag","myBasic");
                    startActivity(intent);
                }

                break;
            case R.id.rl_mine_postposition://我的投递
                if(null != EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,MyPostActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    intent.putExtra("LoginTag","myPost");
                    startActivity(intent);
                }

                break;

            case R.id.rl_mine_collectpostion://我的收藏
                if(null != EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,MyCollectActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    intent.putExtra("LoginTag","myCollect");
                    startActivity(intent);
                }

                break;

            case R.id.rl_mine_jianliguanli://简历管理
                if(null != EggshellApplication.getApplication().getUser()){
                    intent = new Intent(mContext,ResumeManagerActivity.class);
                    startActivity(intent);
                }else{
                    intent = new Intent(mContext,LoginActivity.class);
                    intent.putExtra("LoginTag","myResume");
                    startActivity(intent);
                }
                break;
            case R.id.rl_mine_about://关于蛋壳儿
                intent = new Intent(mContext,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_hezuoqudao://合作渠道
                intent = new Intent(mContext,AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_mine_logout://退出登录
                dialog.show();

                break;
//            case R.id.rl_mine_setting:
//                intent = new Intent(mContext, SetUpActivity.class);
//                startActivity(intent);
//                break;
        }
    }
}