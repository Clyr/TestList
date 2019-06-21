package com.matrix.myapplication.retrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.matrix.myapplication.R;
import com.matrix.myapplication.activity.BaseActivity;
import com.matrix.myapplication.utils.MyLog;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitActivity extends BaseActivity {

    @BindView(R.id.getNot)
    Button getNot;
    @BindView(R.id.getHave)
    Button getHave;
    @BindView(R.id.postNot)
    Button postNot;
    @BindView(R.id.postHave)
    Button postHave;

    //    public String mUrl = "https://api.github.com/users/";
    public String mUrl = "http://218.58.194.194:9000/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.getNot, R.id.getHave, R.id.postNot, R.id.postHave})
    public void onViewClicked(View view) {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()//okhttp设置部分，此处还可再设置网络参数
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mUrl)
                .client(HttpLog.getLogClient())//此client是为了打印信息
                .build();
        ORService orService = retrofit.create(ORService.class);
        switch (view.getId()) {
            case R.id.getNot:
                Call<ResponseBody> call = orService.get("basil2style");
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null && response.isSuccessful()) {
                            try {
                                MyLog.d(response.body().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MyLog.e(t.getMessage());
                    }
                });
                break;
            case R.id.getHave:
                break;
            case R.id.postNot:
                Call<ResponseBody> calln = orService.post("cuGetDevTreeEx");
                calln.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null && response.isSuccessful()) {
                            try {
                                MyLog.d(response.body().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MyLog.e(t.getMessage());
                    }
                });
                break;
            case R.id.postHave:
                /*"userName":"admin",
                    "clientType":"android",
                    "ipAddress":""*/
                Map<String, String> map = new HashMap<>();
                map.put("userName", "admin");
                map.put("clientType", "android");
                map.put("ipAddress", "");
                Call<ResponseBody> callph = orService.post("videoService/accounts/authorize", map);
                callph.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response != null && response.isSuccessful()) {
                            try {
                                MyLog.d(response.body().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MyLog.e(t.getMessage());
                    }
                });
                break;
        }
    }

    /*
     **打印retrofit信息部分
     */
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
            //打印retrofit日志
            Log.e("RetrofitLog", "retrofitBack = " + message)

    );

}
