package com.laughing.emovie.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> listData;
    private List<String> listTitle;

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> listData) {
        super(fm);
        this.listData = listData;
    }

    public MyFragmentAdapter(FragmentManager fm, List<Fragment> listData,
                             List<String> listTitle) {
        super(fm);
        this.listData = listData;
        this.listTitle = listTitle;
    }

    @Override
    public Fragment getItem(int arg0) {
        return listData.get(arg0);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (listTitle != null) {
            return listTitle.get(position);
        } else {
            return "";
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}

