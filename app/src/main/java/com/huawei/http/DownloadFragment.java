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

import com.huawei.BaseUtil;
import com.huawei.FileUtil;
import com.huawei.ThemeUtil;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.thirdpart.httpurlconnection.URLConnectionFactoryHelper;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

/**
 * @author cWX223941
 */
public class DownloadFragment extends Fragment
{
    private static final String TAG = "DownloadFragment";
    private View v;
    private MyEditText etUrl, etSubUrl, etFileName;
    private TextView btnDownload;
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
        v = inflater.inflate(R.layout.fragment_http_download, container, false);
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
        etUrl = (MyEditText) v.findViewById(R.id.et_http_download_url);
        etSubUrl = (MyEditText) v.findViewById(R.id.et_http_download_suburl);
        etFileName = (MyEditText) v
                .findViewById(R.id.et_http_download_filename);
        btnDownload = (TextView) v.findViewById(R.id.btn_http_download_request);
        resultBlock = (LinearLayout) v
                .findViewById(R.id.layout_http_download_result_block);
        tvRequest = (TextView) v.findViewById(R.id.tv_http_download_request);
        tvResponse = (TextView) v.findViewById(R.id.tv_http_download_response);

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etSubUrl.setItemBlockBackground(R.drawable.bg_edit);
        etFileName.setItemBlockBackground(R.drawable.bg_edit);
        etUrl.setHintText(R.string.http_http);
        etSubUrl.setHintText(R.string.http_subhttp);
        etFileName.setHintText(R.string.http_download_filename);
        btnDownload.setEnabled(false);

        etUrl.setTextChangedListener(textWatcher);
        etFileName.setTextChangedListener(textWatcher);
        btnDownload.setOnClickListener(onClickListener);
    }

    private void initData()
    {
        etUrl.setText(Constants.HTTP_TEST_BASE_URL);
        etSubUrl.setText(Constants.HTTP_TEST_DOWNLOAD_URL);
        etFileName.setText("demo.jpg");
    }


    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {


            if(getActivity() == null)
            {
                return;
            }


            btnDownload.setText(R.string.ing);
            btnDownload.setEnabled(false);
            tvRequest.setText("");
            tvResponse.setText("");



            if (Constants.ACTIVITY_SEND_HTTPTYPE_URLCONNECTION
                    .equals(((HttpActivity) getActivity()).getHttpType()))
            {
                urlConnectionDownload();
            }
            else if (Constants.ACTIVITY_SEND_HTTPTYPE_HTTPCLIENT
                    .equals(((HttpActivity) getActivity()).getHttpType()))
            {
                httpClientDownload();
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
                ThemeUtil.setBtnToEnable(btnDownload, getActivity());
            }
            else
            {
                //set login button to be not enable
                ThemeUtil.setBtnToUnable(btnDownload, getActivity());
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
        if (etUrl.hasContent() && etFileName.hasContent())
        {
            return true;
        }
        return false;
    }

    private void handleDownloadResult(boolean status, String request,
                                      String response)
    {
        if (status)
        {
            BaseUtil.showToast(R.string.download_ok, getActivity());
            //            downloadFragment.btnDownload.setVisibility(View.VISIBLE);
            btnDownload.setText(R.string.download);
            btnDownload.setEnabled(true);
            resultBlock.setVisibility(View.VISIBLE);
            tvRequest.setText(request);
            tvResponse.setText(response);
        }
        else
        {
            btnDownload.setText(R.string.download);
            btnDownload.setEnabled(true);
            BaseUtil.showToast(R.string.download_error, getActivity());
        }
    }

    private void urlConnectionDownload()
    {
        final String fileName = etFileName.getText().trim();
        String encodedFileName = fileName;
        try
        {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            encodedFileName = fileName;
        }


        final String urlStr = etUrl.getText().trim()
                + etSubUrl.getText().trim() + "?fileName=" + encodedFileName;


        AsyncTask<Object, Integer, Boolean> downloadTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = false;
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
                    InputStream stream = null;
                    FileOutputStream fstream = null;
                    //init the request data
                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.connect();
                    stream = connection.getInputStream();
                    Log.e(TAG, "URLConnection request success, res = "
                            + connection.getResponseCode());
                    if (connection.getResponseCode() == 200)
                    {
                        String localPath = Constants.FOLDER_PATH_DOWNLOAD + "/"
                                + fileName;
                        File folder = new File(Constants.FOLDER_PATH_DOWNLOAD);
                        if (!folder.exists())
                        {
                            folder.mkdirs();
                        }
                        Log.d(TAG, "Response OK,filePath = " + localPath);
                        fstream = new FileOutputStream(new File(localPath));
                        FileUtil.streamCopy(stream, fstream);
                        result = true;
                    }
                    //get response and request
                    httpRequest = urlStr;
                    httpResponse = connection.getHeaderFields().toString();
                }
                catch (Exception e)
                {
                    result = false;
                    e.printStackTrace();
                    Log.e(TAG,
                            "URLConnection request fail, res = "
                                    + e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                handleDownloadResult(result, httpRequest, httpResponse);
            }
        };
        downloadTask.execute(new Object());
    }

    private void httpClientDownload()
    {
        final String fileName = etFileName.getText().trim();

        String encodedFileName = fileName;
        try
        {
            encodedFileName = URLEncoder.encode(fileName, "utf-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            encodedFileName = fileName;
        }


        final String urlStr = etUrl.getText().trim()
                + etSubUrl.getText().trim() + "?fileName=" + encodedFileName;
        AsyncTask<Object, Integer, Boolean> downloadTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = false;
                try
                {
                    InputStream fileInStream = null;
                    FileOutputStream fileOutStream = null;
                    //HttpClient hc = new SvnHttpClient();
                    HttpClient hc = HttpClientHelp.getInstance();
                    HttpGet getMethod = new HttpGet(urlStr);
                    HttpResponse response = hc.execute(getMethod);
                    Log.e(TAG, "HttpClient request success, res = "
                            + response.getStatusLine().getStatusCode());
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                    {
                        HttpEntity respEnt = response.getEntity();
                        Boolean isStream = respEnt.isStreaming();
                        if (isStream)
                        {
                            fileInStream = respEnt.getContent();
                            String localPath = Constants.FOLDER_PATH_DOWNLOAD
                                    + "/" + fileName;
                            File folder = new File(
                                    Constants.FOLDER_PATH_DOWNLOAD);
                            if (!folder.exists())
                            {
                                folder.mkdirs();
                            }
                            fileOutStream = new FileOutputStream(new File(
                                    localPath));
                            FileUtil.streamCopy(fileInStream, fileOutStream);
                            //get response and request

                            result = true;
                        }
                    }
                    httpRequest = urlStr;
                    httpResponse = response.getStatusLine().toString();
                }
                catch (Exception e)
                {
                    result = false;
                    e.printStackTrace();
                    Log.e(TAG,
                            "HttpClient request fail, res = " + e.getMessage());
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                handleDownloadResult(result, httpRequest, httpResponse);
            }
        };
        downloadTask.execute(new Object());
    }
}
