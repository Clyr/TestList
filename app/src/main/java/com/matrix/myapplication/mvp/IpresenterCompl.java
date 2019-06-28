package com.matrix.myapplication.mvp;

/**
 * Created by M S I of clyr on 2019/5/15.
 */
public class IpresenterCompl implements IPresenter {
    private ILoginView iLoginView;
    private IUser iUser;

    public IpresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        //初始化数据
        initUser();
    }

    //初始化数据
    private void initUser() {
        iUser = new UserModel("username", "pwd");

    }

    @Override
    public void login(String user, String pwd) {
        boolean ret = iUser.checkLoginVisible(user, pwd);
        iLoginView.LoginResult(ret, "0");

    }
}
