package com.matrix.myapplication.mvp2;

import com.matrix.myapplication.model.TestData;
import com.matrix.myapplication.model.User;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhoufazhan on 2017/7/27.
 */

public interface ApiService {
    /**
     * 登录 2.4.1
     */
    @GET("json/login.action")
    Observable<TestData<User>> reLogin(@Query("tel") String tel, @Query("password") String password);
}
