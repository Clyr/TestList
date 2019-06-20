package com.douyu.mvp.presenter.impl;


import com.douyu.db.bean.Person;

/**
 * Created by l on 17-1-10.
 */
public interface IPlayPresenter {
    void getSystemTime();

    void unRigister();

    void autoHidePanel();

    void getPlayerInfo(int roomId);

    void dismissVolAlpha();

    void showAttention(Person person);

    void insertPerson(Person person);

    void deletePerson(Person person);
}
