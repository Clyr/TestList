package com.mm131;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by M S I of clyr on 2019/5/24.
 */
public interface ServiceApi {

    //下载文件
    @GET
    Call<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    @GET
    @Streaming
    Observable<ResponseBody> downloadImg(@Url String  imgUrl);
}
