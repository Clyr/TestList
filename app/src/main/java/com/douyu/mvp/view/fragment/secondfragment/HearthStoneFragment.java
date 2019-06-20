package com.douyu.mvp.view.fragment.secondfragment;

import android.os.Bundle;

import com.douyu.mvp.view.base.BaseFragment;


/**
 * Created by l on 16-12-31.
 */

public class HearthStoneFragment extends BaseFragment {

    private static HearthStoneFragment fragment = null;

    public static HearthStoneFragment getInstance(int type) {
        Bundle args = new Bundle();
        fragment = new HearthStoneFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getType() {
        Bundle arguments = fragment.getArguments();
        return arguments.getInt("type");
    }
}
