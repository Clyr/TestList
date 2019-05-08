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

package com.huawei.http;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.FileUtil;
import com.huawei.common.BaseActivity;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.thirdpart.httpurlconnection.URLConnectionFactoryHelper;
import com.huawei.svn.sdk.webview.SvnWebViewProxy;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class SpeedCompareActivity extends BaseActivity
{
    private TextView btnBack;
    private TextView speedInnerNet;
    private TextView speedOuterNet;
    private TextView speedTestResult;
    private MyEditText speedCurrentSite;
    private Spinner speedSpinner;
    private Button speedTest;
    private TextView currentView;
    private TextView httpRequest;
    private TextView httpResponse;
    private WebView speedWebView;
    private ScrollView httpResultContainer;
    private int http = 0;
    private int net = 0;
    private StringBuffer sb;

    private String httpRequestResult;
    private String httpResponseResult;

    private long start = 0L;
    private long end = 0L;

    private List<Long> httpClientInner = new ArrayList<Long>();
    private List<Long> httpClientOuter = new ArrayList<Long>();
    private List<Long> urlConnectInner = new ArrayList<Long>();
    private List<Long> urlConnectOuter = new ArrayList<Long>();
    private List<Long> webViewInner = new ArrayList<Long>();
    private List<Long> webViewOuter = new ArrayList<Long>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_speed_compare);
        init();
        registerForContextMenu(speedTestResult);
    }

    //上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo)
    {
        menu.setHeaderIcon(R.drawable.icon_20x20);
        menu.setHeaderTitle("File Operate");
        menu.add(0, 1, Menu.NONE, "Delete");
        menu.add(0, 2, Menu.NONE, "Copy");
    }

    //上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case 1:
                sb.delete(0, sb.length());
                speedTestResult.setText("");
                break;
            case 2:

                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    private void init()
    {
        initView();
        initData();
        initWebView();
    }

    private void initView()
    {
        btnBack = (TextView) findViewById(R.id.btn_speed_back);
        speedInnerNet = (TextView) findViewById(R.id.btn_speed_inner);
        speedOuterNet = (TextView) findViewById(R.id.btn_speed_outer);
        speedTestResult = (TextView) findViewById(R.id.speed_test_result);
        httpRequest = (TextView) findViewById(R.id.http_request);
        httpResponse = (TextView) findViewById(R.id.http_response);

        httpResultContainer = (ScrollView) findViewById(R.id.http_result_container);
        speedCurrentSite = (MyEditText) findViewById(R.id.current_speed_test_site);
        speedCurrentSite.setItemBlockBackground(R.drawable.bg_edit);
        speedCurrentSite.setTextSize(12);

        speedSpinner = (Spinner) findViewById(R.id.httpSpinner);
        speedTest = (Button) findViewById(R.id.btn_speed_test);
        speedWebView = (WebView) findViewById(R.id.speed_webview_container);

        speedInnerNet
                .setBackgroundResource(R.drawable.bg_navigate_left_selected);
        speedInnerNet.setTextColor(getResources().getColor(R.color.white));
        currentView = speedInnerNet;

    }

    private void initData()
    {
        if (net == 0 && http == 0)
        {
            speedCurrentSite.setText(Constants.HTTP_TEST_BASE_URL
                    + Constants.HTTP_TEST_LOGIN_URL);
        }
        btnBack.setOnClickListener(onClickListener);
        speedTest.setOnClickListener(onClickListener);
        speedInnerNet.setOnClickListener(onClickListener);
        speedOuterNet.setOnClickListener(onClickListener);
        speedSpinner.setOnItemSelectedListener(onItemListener);
        //speedCurrentSite.setTextChangedListener(textWatcher);
        sb = new StringBuffer();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebView()
    {

        speedWebView.getSettings().setJavaScriptEnabled(true);
        speedWebView.getSettings().setDomStorageEnabled(true);
        speedWebView.getSettings().setUserAgentString(
                "Mozilla/5.0 (Linux; U; Android 2.2; en-gb;"
                        + " Nexus One Build/FRF50) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
        speedWebView.setWebViewClient(new WebViewClient()
        {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                start = System.currentTimeMillis();
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                end = System.currentTimeMillis();
                if (net == 0)
                {
                    webViewInner.add(end - start);
                }
                else
                {
                    webViewOuter.add(end - start);
                }
                speedTestResult.setText(sb
                        .append("WebView--->")
                        .append(net == 0 ? "内网" : "外网")
                        .append("---> time=" + (end - start))
                        .append("\t")
                        .append((net == 0 ? webViewInner.size() : webViewOuter.size()) + "次")
                        .append("\t")
                        .append("平均值：" + (net == 0 ? averValue(webViewInner) : averValue(webViewOuter)))
                        .append("\n"));
            }
        });
    }

    private void btnStatusClean()
    {
        speedInnerNet.setBackgroundResource(0);
        speedOuterNet.setBackgroundResource(0);
        speedInnerNet.setTextColor(getResources().getColor(
                R.color.btn_content));
        speedOuterNet.setTextColor(getResources().getColor(
                R.color.btn_content));
    }

    private long averValue(List<Long> list)
    {
        long sum = 0L;
        for (int i = 0; i < list.size(); i++)
        {
            sum += list.get(i);
        }
        return sum / list.size();
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btn_speed_test:
                    speedTest();
                    break;
                case R.id.btn_speed_inner:
                    net = 0;
                    btnStatusClean();
                    urlInfo();
                    currentView = speedInnerNet;
                    ((TextView) v)
                            .setBackgroundResource(R.drawable.bg_navigate_left_selected);
                    ((TextView) v).setTextColor(getResources().getColor(
                            R.color.white));
                    break;
                case R.id.btn_speed_outer:
                    net = 1;
                    btnStatusClean();
                    urlInfo();
                    currentView = speedOuterNet;
                    ((TextView) v)
                            .setBackgroundResource(R.drawable.bg_navigate_right_selected);
                    ((TextView) v).setTextColor(getResources().getColor(
                            R.color.white));
                    break;
                default:
                    SpeedCompareActivity.this.finish();
                    break;
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher()
    {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after)
        {
            speedCurrentSite.setSelection(speedCurrentSite.getText().length());
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            speedCurrentSite.setSelection(speedCurrentSite.getText().length());
        }
    };

    private OnItemSelectedListener onItemListener = new OnItemSelectedListener()
    {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id)
        {
            http = position;
            urlInfo();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent)
        {

        }

    };

    private void urlInfo()
    {
        if (net == 0)
        {
            switch (http)
            {
                case 0:

//            	 String urlStr = Constants.HTTP_TEST_BASE_URL + Constants.HTTP_TEST_DOWNLOAD_URL
//                 + "?fileName=aa.jpg" ;
//            	 
//            	 speedCurrentSite.setText(urlStr);
                    speedCurrentSite.setText("http://172.22.10.59:8080");
                    break;
                case 1:
                    speedCurrentSite.setText("http://172.22.10.59:8080");
                    break;
                default:
                    speedCurrentSite.setText(Constants.HTTP_TEST_WEBVIEW_URL);
                    break;
            }
        }
        else
        {
            speedCurrentSite.setText("http://10.170.26.43:8080");
        }
        if (http == 2)
        {
            speedWebView.setVisibility(View.VISIBLE);
            httpResultContainer.setVisibility(View.GONE);
        }
        else
        {
            speedWebView.setVisibility(View.GONE);
            httpResultContainer.setVisibility(View.VISIBLE);
        }
    }

    public void speedTest()
    {
        if (currentView.getId() == speedInnerNet.getId())
        {
            switch (http){
                case 0:
                    doHttpClient(true);
                    break;
                case 1:
                    doURLConnection(true);
                    break;
                default:
                    doWebView(true);
                    break;
            }
        }
        else
        {
            switch (http)
            {
                case 0:
                    doHttpClient(false);
                    break;
                case 1:
                    doURLConnection(false);
                    break;
                default:
                    doWebView(false);
                    break;
            }
        }

    }

    // 通过隧道访问
    public void doHttpClient(boolean useSvn)
    {
        final boolean useSVN = useSvn;
        final String urlinfo = speedCurrentSite.getText().trim();

        AsyncTask<Object, Integer, Boolean> loginTask = new AsyncTask<Object, Integer, Boolean>()
        {

            @Override
            protected void onPreExecute()
            {
                System.gc();
            }

            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                start = System.currentTimeMillis();
                boolean result = false;
                try
                {
                    HttpClient hc = null;
                    if (useSVN)
                    {
                        hc = HttpClientHelp.getInstance();
                    }
                    else
                    {
                        hc = new DefaultHttpClient();
                    }
                    HttpPost postMethod = new HttpPost(urlinfo);
                    hc.getParams().setIntParameter(
                            CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
                    HttpResponse response = hc.execute(postMethod);
                    httpRequestResult = postMethod.getRequestLine().toString();
                    httpResponseResult = EntityUtils.toString(response.getEntity(),
                            "utf-8");
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
                    {
                        result = true;
                        publishProgress(0);
                    }
                    else
                    {
                        publishProgress(1);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    publishProgress(-1);
                }
                return result;
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                switch (values[0])
                {
                    case 0:
                        Toast.makeText(SpeedCompareActivity.this, "连接成功",
                                Toast.LENGTH_SHORT).show();
                        httpRequest.setText(httpRequestResult);
                        httpResponse.setText(httpResponseResult);
                        break;
                    case 1:
                        Toast.makeText(SpeedCompareActivity.this, "连接失败",
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(SpeedCompareActivity.this, "连接异常",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                if (result)
                {
                    end = System.currentTimeMillis();
                    if (net == 0)
                    {
                        httpClientInner.add(end - start);
                    }
                    else
                    {
                        httpClientOuter.add(end - start);
                    }
                    speedTestResult.setText(sb
                            .append("HttpClient--->")
                            .append(net == 0 ? "内网" : "外网")
                            .append("---> time=" + (end - start))
                            .append("\t")
                            .append((net == 0 ? httpClientInner.size() : httpClientOuter.size()) + "次")
                            .append("\t")
                            .append("平均值" + (net == 0 ? averValue(httpClientInner) : averValue(httpClientOuter)))
                            .append("\n"));
                }
            }
        };
        loginTask.execute(new Object());
    }

    public void doURLConnection(boolean useSvn)
    {
        final boolean useSVN = useSvn;
        final String urlinfo = speedCurrentSite.getText().toString().trim();

        AsyncTask<Object, Integer, Boolean> loginTask = new AsyncTask<Object, Integer, Boolean>()
        {

            @Override
            protected void onPreExecute()
            {
                System.gc();
            }

            @Override
            protected void onProgressUpdate(Integer... values)
            {
                switch (values[0])
                {
                    case 0:
                        Toast.makeText(SpeedCompareActivity.this, "连接成功",
                                Toast.LENGTH_SHORT).show();
                        httpRequest.setText(httpRequestResult);
                        httpResponse.setText(httpResponseResult);
                        break;
                    default:
                        Toast.makeText(SpeedCompareActivity.this, "连接异常",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                start = System.currentTimeMillis();
                boolean result = true;
                try
                {
                    if (useSVN)
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
                    }
                    URL url = new URL(urlinfo);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    httpRequestResult = connection.getHeaderFields().toString();
                    httpResponseResult = new String(new StringBuffer().append(FileUtil.read(connection.getInputStream())));
                    publishProgress(0);

                }
                catch (Exception e)
                {
                    publishProgress(-1);
                    result = false;
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                if (result)
                {
                    end = System.currentTimeMillis();
                    if (net == 0)
                    {
                        urlConnectInner.add(end - start);
                    }
                    else
                    {
                        urlConnectOuter.add(end - start);
                    }
                    speedTestResult.setText(sb
                            .append("URLConnection--->")
                            .append(net == 0 ? "内网" : "外网")
                            .append("---> time=" + (end - start))
                            .append("\t")
                            .append((net == 0 ? urlConnectInner.size()
                                    : urlConnectOuter.size()) + "次")
                            .append("\t")
                            .append("平均值"
                                    + (net == 0 ? averValue(urlConnectInner)
                                    : averValue(urlConnectOuter)))
                            .append("\n"));
                }
            }
        };
        loginTask.execute(new Object());
    }

    public void doWebView(boolean useSvn)
    {
        System.gc();
        String url = speedCurrentSite.getText().trim();
        if (useSvn)
        {
            SvnWebViewProxy.getInstance().setWebViewUseSVN(speedWebView);
        }
        speedWebView.loadUrl(url);
    }
}
