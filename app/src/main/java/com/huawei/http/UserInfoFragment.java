/*
 * Copyright 2015 Huawei Technologies Co., Ltd. All rights reserved.
 * eSDK is licensed under the Apache License, Version 2.0 ^(the "License"^);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package com.huawei.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.huawei.FileUtil;
import com.huawei.ThemeUtil;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.thirdpart.httpurlconnection.URLConnectionFactoryHelper;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * @author cWX223941
 */
public class UserInfoFragment extends Fragment
{
    private static final String TAG = "UserInfoFragment";
    private View v;
    private MyEditText etUserName, etLastTime, etUrl, etSubUrl;
    private TextView btnUserInfo;
    //    private UserInfoEntity                      userInfo;
    private LinearLayout resultBlock;
    private TextView tvRequest, tvResponse;
    private String httpRequest;
    private String httpResponse;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        v = inflater.inflate(R.layout.fragment_http_userinfo, container, false);
        init();
        return v;
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        etUserName = (MyEditText) v
                .findViewById(R.id.et_http_userinfo_username);
        etLastTime = (MyEditText) v
                .findViewById(R.id.et_http_userinfo_lasttime);
        etUrl = (MyEditText) v.findViewById(R.id.et_http_userinfo_url);
        etSubUrl = (MyEditText) v.findViewById(R.id.et_http_userinfo_suburl);
        btnUserInfo = (TextView) v.findViewById(R.id.btn_http_userinfo_request);
        resultBlock = (LinearLayout) v
                .findViewById(R.id.layout_http_userinfo_result_block);
        tvRequest = (TextView) v.findViewById(R.id.tv_http_userinfo_request);
        tvResponse = (TextView) v.findViewById(R.id.tv_http_userinfo_response);

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etSubUrl.setItemBlockBackground(R.drawable.bg_edit);
        etUserName.setItemBlockBackground(R.drawable.bg_edit);
        etLastTime.setItemBlockBackground(R.drawable.bg_edit);
        etUrl.setHintText(R.string.http_http);
        etSubUrl.setHintText(R.string.http_subhttp);
        etUserName.setHintText(R.string.http_userinfo_username);
        etLastTime.setHintText(R.string.http_userinfo_lastlogintime);
        //        UserInfoController.setUserInfoData(this);
        btnUserInfo.setEnabled(false);
        //no user name and last time
        etUserName.setVisibility(View.GONE);
        etLastTime.setVisibility(View.GONE);


        etUrl.setTextChangedListener(textWatcher);
        btnUserInfo.setOnClickListener(onClickListener);


    }

    private void initData()
    {
        etUrl.setText(Constants.HTTP_TEST_BASE_URL);
        etSubUrl.setText(Constants.HTTP_TEST_USERINFO_URL);
        etUserName.setText(Constants.AUTHENTICATE_USERNAME);
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnUserInfo.getId())
            {
                if(getActivity() == null)
                {
                    return;
                }

                btnUserInfo.setText(R.string.ing);
                btnUserInfo.setEnabled(false);
                tvRequest.setText("");
                tvResponse.setText("");

                if (Constants.ACTIVITY_SEND_HTTPTYPE_URLCONNECTION
                        .equals(((HttpActivity) getActivity()).getHttpType()))
                {
                    urlConnectionGetUserInfo();
                }
                else if (Constants.ACTIVITY_SEND_HTTPTYPE_HTTPCLIENT
                        .equals(((HttpActivity) getActivity()).getHttpType()))
                {
                    httpClientGetUserInfo();
                }
            }
        }
    };
    private TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count)
        {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (checkEditContent())
            {
                //set login button to be enable
                ThemeUtil.setBtnToEnable(btnUserInfo, getActivity());
            }
            else
            {
                //set login button to be not enable
                ThemeUtil.setBtnToUnable(btnUserInfo, getActivity());
            }
        }
    };

    /**
     * if all edit text have content,then return true,otherwise return false
     *
     * @return true:all have content;false:otherwise
     */
    private boolean checkEditContent()
    {
        if (etUrl.hasContent())
        {
            return true;
        }
        return false;
    }

    private void handleUserInfoResult(boolean status, String request,
                                      String response)
    {
        if (status)
        {
            Log.e(TAG, "get user info success.");
            //            userInfo = new UserInfoEntity("zero","2012-03-04");
            //            setUserInfoData(userInfoFragment);
            btnUserInfo.setText(R.string.userinfo);
            btnUserInfo.setEnabled(true);
            resultBlock.setVisibility(View.VISIBLE);
            tvRequest.setText(request);
            tvResponse.setText(response);
        }
        else
        {
            Log.e(TAG, "get user info false.");
            btnUserInfo.setText(R.string.userinfo);
            btnUserInfo.setEnabled(true);
        }
    }

    private void urlConnectionGetUserInfo()
    {
        AsyncTask<Object, Integer, Boolean> userInfoTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = true;
                try
                {
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
                    {
                        @Override
                        public boolean verify(String hostname, SSLSession session)
                        {
                            return true;
                        }
                    });
                    URLConnectionFactoryHelper.setURLStreamHandlerFactory();
                    //String firstCookie = "";
                    StringBuilder sb = new StringBuilder();
                    URL url = new URL(etUrl.getText().trim()
                            + etSubUrl.getText().trim());
                    Log.d(TAG, "url = " + url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();

                    httpRequest = connection.getHeaderFields().toString();
                    connection.connect();
                    httpResponse = getResponseFronUrlConnection(connection);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    result = false;
                }

                return result;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                handleUserInfoResult(result, httpRequest, httpResponse);
            }
        };
        userInfoTask.execute(new Object());
    }

    private void httpClientGetUserInfo()
    {
        AsyncTask<Object, Integer, Boolean> userInfoTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = true;
                try
                {
                    HttpGet getMethod = new HttpGet(etUrl.getText().trim()
                            + etSubUrl.getText().trim());
                    HttpClient hc = HttpClientHelp.getInstance();
                    HttpResponse response = hc.execute(getMethod);
                    httpRequest = getMethod.getRequestLine().toString();
                    Log.i(TAG, "resCode = "
                            + response.getStatusLine().getStatusCode());
                    httpResponse = EntityUtils.toString(response.getEntity(),
                            "utf-8");
                    //get response and request
                    Log.i(TAG, "httpClient request success.");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Log.e(TAG,
                            "httpClient request fail. exception = "
                                    + e.getMessage());
                    result = false;
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                handleUserInfoResult(result, httpRequest, httpResponse);
            }
        };
        userInfoTask.execute(new Object());
    }

    /**
     * get response from connection
     *
     * @param conn HttpURLConnection
     */
    private String getResponseFronUrlConnection(HttpURLConnection conn)
            throws Exception
    {
        StringBuffer builder = new StringBuffer();
        InputStream stream = conn.getInputStream();
        builder.append(FileUtil.read(stream));
        return builder.toString();
    }
}
