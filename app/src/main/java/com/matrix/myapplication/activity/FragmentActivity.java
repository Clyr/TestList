package com.matrix.myapplication.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.fragment.AlarmFragment;
import com.matrix.myapplication.fragment.MonitorFragment;
import com.matrix.myapplication.fragment.PandectFragment;
import com.matrix.myapplication.fragment.PersonFragment;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

public class FragmentActivity extends BaseActivity {
    //要切换显示的Fragment
    private PandectFragment mPandectFragment;
    private AlarmFragment mAlarmFragment;
    private MonitorFragment mMonitorFragment;
    private PersonFragment mPersonFragment;
    //    private OtherFragment mOtherFragment;
    private int currentId = R.id.tv_main;// 当前选中id,默认是主页

    private TextView mTvMain, mTvDynamic, mTvMessage, mTvPerson, mTvOther;//底部TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mTvMain = (TextView) findViewById(R.id.tv_main);
        mTvMain.setSelected(true);//首页默认选中
        mTvDynamic = (TextView) findViewById(R.id.tv_dynamic);
        mTvMessage = (TextView) findViewById(R.id.tv_message);
        mTvPerson = (TextView) findViewById(R.id.tv_person);

        /**
         * 默认加载首页
         */
        mPandectFragment = new PandectFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, mPandectFragment).commit();

        mTvMain.setOnClickListener(tabClickListener);
        mTvDynamic.setOnClickListener(tabClickListener);
        mTvMessage.setOnClickListener(tabClickListener);
        mTvPerson.setOnClickListener(tabClickListener);

    }

    private View.OnClickListener tabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() != currentId) {//如果当前选中跟上次选中的一样,不需要处理
                changeSelect(v.getId());//改变图标跟文字颜色的选中
                changeFragment(v.getId());//fragment的切换
                currentId = v.getId();//设置选中id
                EventBus.getDefault().post(new MessageEvent(Config.mRefreash));
            }
        }
    };

    /**
     * 改变fragment的显示
     *
     * @param resId
     */
    private void changeFragment(int resId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();//开启一个Fragment事务
        hideFragments(transaction);//隐藏所有fragment
        if (resId == R.id.tv_main) {//主页
            if (mPandectFragment == null) {//如果为空先添加进来.不为空直接显示
                mPandectFragment = new PandectFragment();
                transaction.add(R.id.main_container, mPandectFragment);
            } else {
                transaction.show(mPandectFragment);
                mPandectFragment.reShowExecute();
            }
        } else if (resId == R.id.tv_dynamic) {//动态
            if (mAlarmFragment == null) {
                mAlarmFragment = new AlarmFragment();
                transaction.add(R.id.main_container, mAlarmFragment);
            } else {
                transaction.show(mAlarmFragment);
                mAlarmFragment.reShowExecute();
            }
        } else if (resId == R.id.tv_message) {//消息中心
            if (mMonitorFragment == null) {
                mMonitorFragment = new MonitorFragment();
                transaction.add(R.id.main_container, mMonitorFragment);
            } else {
                transaction.show(mMonitorFragment);
                mMonitorFragment.reShowExecute();
            }
        } else if (resId == R.id.tv_person) {//我
            if (mPersonFragment == null) {
                mPersonFragment = new PersonFragment();
                transaction.add(R.id.main_container, mPersonFragment);
            } else {
                transaction.show(mPersonFragment);
                mPersonFragment.reShowExecute();
            }
        }
        transaction.commit();//一定要记得提交事务
    }

    /**
     * 显示之前隐藏所有fragment
     *
     * @param transaction
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mPandectFragment != null)//不为空才隐藏,如果不判断第一次会有空指针异常
            transaction.hide(mPandectFragment);
        if (mAlarmFragment != null)
            transaction.hide(mAlarmFragment);
        if (mMonitorFragment != null)
            transaction.hide(mMonitorFragment);
        if (mPersonFragment != null)
            transaction.hide(mPersonFragment);
    }

    /**
     * 改变TextView选中颜色
     *
     * @param resId
     */
    private void changeSelect(int resId) {
        mTvMain.setSelected(false);
        mTvDynamic.setSelected(false);
        mTvMessage.setSelected(false);
        mTvPerson.setSelected(false);
//        mTvOther.setSelected(false);
        switch (resId) {
            case R.id.tv_main:
                mTvMain.setSelected(true);
                break;
            case R.id.tv_dynamic:
                mTvDynamic.setSelected(true);
                break;
            case R.id.tv_message:
                mTvMessage.setSelected(true);
                break;
            case R.id.tv_person:
                mTvPerson.setSelected(true);
                break;
        }
    }
    /**
     * 两次退出 start
     */
    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                if (System.currentTimeMillis() - mExitTime > 2000) {
                    ToastUtils.showShort("再按一次退出!");
                } else {
                    System.exit(0);
                }
                mExitTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    /** 两次退出 end*/
}
