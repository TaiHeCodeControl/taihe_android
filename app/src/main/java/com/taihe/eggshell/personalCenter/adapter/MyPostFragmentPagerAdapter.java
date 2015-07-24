package com.taihe.eggshell.personalCenter.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.taihe.eggshell.personalCenter.fragment.MyPostFailFragment;
import com.taihe.eggshell.personalCenter.fragment.MyPostFragment;
import com.taihe.eggshell.personalCenter.fragment.MyPostSuccFragment;

/**
 * Created by huan on 2015/7/24.
 */
public class MyPostFragmentPagerAdapter extends FragmentPagerAdapter {

    private Fragment fragment;

    public MyPostFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0://
                Fragment oneFragment = new MyPostFragment();
                return oneFragment;
            case 1://
                Fragment twoFragment = new MyPostSuccFragment();
                return twoFragment;
            case 2://
                Fragment threeFragment = new MyPostFailFragment();
                return threeFragment;

        }
        return null;
    }
}

