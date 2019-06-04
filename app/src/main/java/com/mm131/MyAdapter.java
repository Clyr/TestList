package com.mm131;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by M S I of clyr on 2019/5/29.
 */
public class MyAdapter extends FragmentPagerAdapter {
    FragmentManager fragmentManager;
    Fragment fg;

    public MyAdapter(FragmentManager fm, Fragment fg) {
        super(fm);
        fragmentManager = fm;
        this.fg = fg;
    }


    @Override
    public Fragment getItem(int position) {
        if (fg != null) {
            return fg;
        }
        return null;
    }

    @Override
    public int getCount() {
        if (fg != null) {
            return 1;
        }
        return 0;
    }
}
