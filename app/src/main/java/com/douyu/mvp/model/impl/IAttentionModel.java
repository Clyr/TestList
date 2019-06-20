package com.douyu.mvp.model.impl;


import com.douyu.db.bean.Person;

import java.util.List;

import rx.Observable;

/**
 * Created by l on 17-1-14.
 */
public interface IAttentionModel {
    Observable<Person> insertData(Person person);

    Observable<Void> deleteData(Person person);

    Observable<List<Person>> queryAll();

    Observable<Boolean> query(Person person);
}
