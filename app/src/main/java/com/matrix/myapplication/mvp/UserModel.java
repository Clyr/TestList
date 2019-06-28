package com.matrix.myapplication.mvp;

/**
 * Created by M S I of clyr on 2019/5/15.
 */
public class UserModel implements IUser{
    private String username;
    private String pwd;

    public UserModel(String username, String pwd) {
        this.username = username;
        this.pwd = pwd;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPWD() {
        return pwd;
    }

    @Override
    public boolean checkLoginVisible(String username, String pwd) {
        if (this.username.equals(username)&&this.pwd.equals(pwd)){
            return true;
        }
        return false;
    }
}
