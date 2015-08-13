package com.taihe.eggshell.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.taihe.eggshell.R;
import com.taihe.eggshell.base.utils.PrefUtils;
import com.taihe.eggshell.main.GuideActivity;
import com.taihe.eggshell.main.MainActivity;

/**
 * Created by Thinkpad on 2015/7/15.
 */
public class SplashActivity extends Activity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        View view = View.inflate(mContext, R.layout.activity_splash,null);
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
                boolean isFirstUsing = PrefUtils.getBooleanData(mContext,PrefUtils.KEY_FIRST_USE,true);
                if(isFirstUsing){
                    Intent intent = new Intent(mContext,GuideActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(mContext,MainActivity.class);
                    intent.putExtra("MeFragment","");
                    startActivity(intent);
                    finish();
                }

            }
        });

        view.setAnimation(animation);
    }

}
