package com.matrix.myapplication.mvp2;

import android.os.Bundle;

import com.matrix.myapplication.mvp2.m.MVPBaseModel;
import com.matrix.myapplication.mvp2.p.MVPBasePresenter;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by M S I of clyr on 2019/6/27.
 */
public abstract class MVPBaseActivty<P extends MVPBasePresenter,M extends MVPBaseModel> extends SwipeBackActivity {
    P mPresenter;
    M mModel;
    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());
        bind = ButterKnife.bind(this);
        init();
    }

    public abstract int setLayout();
    public abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mModel.unSubscribe();
        mPresenter.unSubscribe();
        bind.unbind();
    }
}
