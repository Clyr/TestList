package com.matrix.myapplication.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.cache.AboutActivity;
import com.matrix.myapplication.utils.MessageEvent;
import com.matrix.myapplication.utils.SPUtils;
import com.matrix.myapplication.utils.ToastUtils;
import com.matrix.myapplication.view.RoundImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 个人设置
 */
public class PersonFragment extends BaseFragment {

    @BindView(R.id.title_center)
    TextView mTitleCenter;//大标题
    @BindView(R.id.title_left)
    TextView mTitleLeft;//标题左
    @BindView(R.id.title_right)
    TextView mTitleRight;//标题右
    @BindView(R.id.person_text)
    TextView mPersonText;//欢迎你，管理员
    @BindView(R.id.person_image)
    RoundImageView mPersonImage;//图标
    @BindView(R.id.person_p1)
    LinearLayout mPersonP1;//第一条，个人信息
    @BindView(R.id.person_p2)
    LinearLayout mPersonP2;//第二条，修改密码
    @BindView(R.id.person_switch)
    ImageView mPersonSwitch;//推送开关
    @BindView(R.id.person_p3)
    LinearLayout mPersonP3;//第三条，推送设置
    @BindView(R.id.person_p4)
    LinearLayout mPersonP4;//第四条，关于
    @BindView(R.id.person_out)
    LinearLayout mPersonOut;//退出
    Unbinder unbinder;
    private boolean mPush = true;
    private Vibrator mVibrator;
//    long[] patter = {100, 100};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_person, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void init() {
        mTitleCenter.setText("个人设置");
        mTitleRight.setVisibility(View.GONE);
        mTitleLeft.setVisibility(View.GONE);
        mPush = SPUtils.getBoolean(getContext(), "Push", true);
//        if (mPush) {
//            mPersonSwitch.setImageResource(R.drawable.on);
//        } else {
//            mPersonSwitch.setImageResource(R.drawable.off);
//        }
        mVibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
    }

    @OnClick({R.id.title_center, R.id.title_left, R.id.person_p1, R.id.person_p2, R.id.person_switch, R.id.person_p3, R.id.person_p4, R.id.person_out})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.title_center:
                break;
            case R.id.title_left:
                break;
            case R.id.person_p1:
                //开启新页面 个人信息
                //intent.setClass(getContext(), PersonInfoActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.person_p2:
                //开启新页面 密码修改
                //intent.setClass(getContext(), PersonPasswordActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.person_switch:
            case R.id.person_p3:
                if (mPush) {
                   // mPersonSwitch.setImageResource(R.drawable.off);
                    ToastUtils.showShort("已关闭");
                    mPush = false;
                    mVibrator.vibrate(100);
                    mHandler.postDelayed(task, 100);

                } else {
                   // mPersonSwitch.setImageResource(R.drawable.on);
                    ToastUtils.showShort("已开启");
                    mPush = true;
                    mVibrator.vibrate(300);
                    mHandler.postDelayed(task, 300);
                }
                //存储设置
                SPUtils.saveBoolean(getContext(), "Push", mPush);
                break;
            case R.id.person_p4:
                //关于
                intent.setClass(getContext(), AboutActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.person_out:
                //退出登录-->进入登录界面，是否弹窗？
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("退出登录")
                        .setMessage("是否退出登录？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //进入登录界面，并且关闭MainActivity
                        SPUtils.saveString(getContext(), Config.mLoginName, "");
                        SPUtils.saveString(getContext(), Config.mPassword, "");
                       // getActivity().startActivity(new Intent(getContext(), LoginActivity.class));
                        EventBus.getDefault().post(new MessageEvent("closeAll"));
                    }
                }).show();
                break;
        }
    }

    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            if (mVibrator != null) {
                mVibrator.cancel();
            }
        }
    };
    @Override//TODO 被动刷新
    public void reShowExecute() {
        super.reShowExecute();

    }
}
