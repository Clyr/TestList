package com.douyu.mvp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.douyu.commons.Pages;
import com.douyu.mvp.view.activity.impl.HomeActivityView;
import com.douyu.mvp.view.fragment.AttentionFragment;
import com.douyu.mvp.view.fragment.ChannelsFragment;
import com.douyu.mvp.view.fragment.LiveFragment;
import com.douyu.mvp.view.fragment.RecommendFragment;
import com.douyu.mvp.view.fragment.SettingFragment;
import com.matrix.myapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主界面
 */
public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, HomeActivityView {
    @BindView(R.id.fr_main)
    FrameLayout frMain;
    @BindView(R.id.rb_recommend)
    RadioButton rbRecommend;
    @BindView(R.id.rb_live)
    RadioButton rbLive;
    @BindView(R.id.rb_channels)
    RadioButton rbChannels;
    @BindView(R.id.rb_like)
    RadioButton rbLike;
    @BindView(R.id.rb_setting)
    RadioButton rbSetting;
    @BindView(R.id.rg_bottom)
    RadioGroup rgBottom;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void switchTab(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fr_main, fragment).commit();
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        rgBottom.setOnCheckedChangeListener(this);
        rgBottom.check(R.id.rb_recommend);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 选择页面
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_recommend:
                RecommendFragment homeFragment = RecommendFragment.getInstance(Pages.HOT_PAGE);
                switchTab(homeFragment);
                break;
            case R.id.rb_live:
                LiveFragment liveFragment = LiveFragment.getInstance(Pages.LIVE_PAGE);
                switchTab(liveFragment);
                break;
            case R.id.rb_channels:
                ChannelsFragment channelsFragment = ChannelsFragment.getInstance(Pages.CHANNELS_PAGE);
                switchTab(channelsFragment);
                break;
            case R.id.rb_like:
                AttentionFragment likeFragment = AttentionFragment.getInstance(Pages.LIKE_PAGE);
                switchTab(likeFragment);
                break;
            case R.id.rb_setting:
                SettingFragment settintFragment = SettingFragment.getInstance(Pages.SETTING_PAGE);
                switchTab(settintFragment);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.douyu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, SearchActivity.class));
        return super.onOptionsItemSelected(item);
    }
}
