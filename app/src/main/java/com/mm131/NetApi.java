package com.mm131;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by M S I of clyr on 2019/5/24.
 */
public interface NetApi {
    @GET
    @Streaming
    Call<ResponseBody> downloadImg(@Url String  imgUrl);

    @GET("bimg/338/{fileName}")  //{fileName}是动态码
    @Streaming//GET下载文件必须结合@Streaming使用
    Observable<ResponseBody> downLoadImg(@Path("fileName") String fileName);
}
