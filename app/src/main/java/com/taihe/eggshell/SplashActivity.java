package com.taihe.eggshell;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.taihe.eggshell.base.BaseActivity;

import java.util.zip.Inflater;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class SplashActivity extends BaseActivity{

    private Context mContext;

    @Override
    public void initView() {
        mContext = this;
        View view = View.inflate(mContext,R.layout.activity_splash,null);
        setContentView(view);

        Animation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1500);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(mContext,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        view.setAnimation(animation);
    }

    @Override
    public void initData() {

    }
}
