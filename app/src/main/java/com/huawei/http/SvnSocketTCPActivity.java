package com.huawei.http;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;

import java.util.ArrayList;

public class SvnSocketTCPActivity extends BaseActivity
{
    private static final String TAG = "SvnSocketTCPActivity";
    private TextView btnTCPTest, btnSvnSocket;
    private TextView btnBack, btnGuide;
    private ViewPager socketTcpViewPager;
    private ArrayList<TextView> btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svn_socket_tcp);
        init();
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        btnTCPTest = (TextView) findViewById(R.id.btn_socket_tcp_tcp);
        btnSvnSocket = (TextView) findViewById(R.id.btn_socket_tcp_socket);
        btnBack = (TextView) findViewById(R.id.btn_socket_tcp_back);
        btnGuide = (TextView) findViewById(R.id.btn_socket_tcp_guide);
        socketTcpViewPager = (ViewPager) findViewById(R.id.socket_tcp);

        btnTCPTest.setOnClickListener(onClickListener);
        btnSvnSocket.setOnClickListener(onClickListener);
        btnBack.setOnClickListener(onClickListener);
        btnGuide.setOnClickListener(onClickListener);
        socketTcpViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    private void initData()
    {
        btnList = new ArrayList<TextView>();
        btnList.add(btnTCPTest);
        btnList.add(btnSvnSocket);
        socketTcpViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        socketTcpViewPager.setOffscreenPageLimit(2);
        navBtnClick(btnTCPTest, false);

    }

    private OnClickListener onClickListener = new OnClickListener()
    {

        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnBack.getId())
            {
                finish();
            }
            else if (v.getId() == btnGuide.getId())
            {

            }
            else
            {
                navBtnClick(v, true);
            }
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener()
    {

        @Override
        public void onPageSelected(int position)
        {
            navBtnClick(btnList.get(position), false);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2)
        {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageScrollStateChanged(int arg0)
        {
            // TODO Auto-generated method stub

        }
    };

    private class PagerAdapter extends FragmentPagerAdapter
    {

        public PagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment fragment = null;
            switch (position)
            {
                case 0:
                    fragment = new TcpTestFragment();
                    break;
                case 1:
                    fragment = new SvnSocketFragment();
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

    }

    /**
     * when navigation button had been click,will do view change and view pager
     * change, program can call this method which is not click action ,but
     * should set the isClick to be false
     *
     * @param btn
     * @param isClick               true:is click action;false:otherwise
     */
    public void navBtnClick(View btn, boolean isClick)
    {
        clearAllNavBtnBg();
        if (btn.getId() == btnTCPTest.getId())
        {
            setBtnSelectedStatus((TextView) btn);
            if (isClick)
            {
                socketTcpViewPager.setCurrentItem(0, true);
            }
        }
        else if (btn.getId() == btnSvnSocket.getId())
        {
            setBtnSelectedStatus((TextView) btn);
            if (isClick)
            {
                socketTcpViewPager.setCurrentItem(1, true);
            }
        }
    }

    /**
     * clear all navigations's background and set text's color to button content
     *
     */
    public void clearAllNavBtnBg()
    {
        btnSvnSocket.setBackgroundResource(0);
        btnTCPTest.setBackgroundResource(0);
        btnSvnSocket.setTextColor(getResources().getColor(R.color.btn_content));
        btnTCPTest.setTextColor(getResources().getColor(R.color.btn_content));
    }

    /**
     * change one navigation button to be selected,change it's background and
     * text's color
     *
     * @param btn
     */
    public void setBtnSelectedStatus(TextView btn)
    {
        if (btn.getId() == btnTCPTest.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_left_selected);
        }
        else if (btn.getId() == btnSvnSocket.getId())
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_right_selected);
        }
        else
        {
            btn.setBackgroundResource(R.drawable.bg_navigate_selected);
        }
        btn.setTextColor(getResources().getColor(R.color.white));
    }
}
