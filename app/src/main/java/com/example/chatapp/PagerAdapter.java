package com.example.chatapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {


    int tabsNum;

    public PagerAdapter(FragmentManager fm, int tabsNum) {
        super(fm);
        this.tabsNum = tabsNum;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatFragment();
            case 1:
                return new UsersFragment();
            default:
                return new UsersFragment();
        }
    }

    @Override
    public int getCount() {
        return tabsNum;
    }
}
