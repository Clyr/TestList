package com.matrix.myapplication.mvp;

/**
 * Created by M S I of clyr on 2019/5/15.
 */
public interface IUser {
    public String getUsername();
    public String getPWD();
    public boolean checkLoginVisible(String username,String pwd);
}
