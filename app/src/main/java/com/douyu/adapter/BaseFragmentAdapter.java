package com.douyu.adapter;

/**
 * Created by LZJ on 2016/11/6.
 */

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页的每个分支的适配器
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments = new ArrayList<>();
    List<String> mTitles = new ArrayList<>();

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mTitles.add(title);
    }
}