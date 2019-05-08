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

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.SDKConfiguration;
import com.huawei.ThemeUtil;
import com.huawei.anyoffice.sdk.ui.ISDKWebViewSSO;
import com.huawei.anyoffice.sdk.ui.SDKWebview;
import com.huawei.anyoffice.sdk.web.WebApp;
import com.huawei.common.BaseActivity;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.webview.SvnWebViewProxy;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;

/**
 * @author cWX223941
 */
public class WebViewActivity extends BaseActivity implements ISDKWebViewSSO
{
    private static final String TAG = "WebViewActivity";
    private MyEditText etUrl;
    private TextView btnWebView, btnBack;
    private WebView webView;
    private SDKWebview sdkWebview;

    private TextView btnSdkWebView;
    private TextView btnWebApp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        init();
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        etUrl = (MyEditText) findViewById(R.id.et_webview_url);
        btnWebView = (TextView) findViewById(R.id.btn_webview);
        btnBack = (TextView) findViewById(R.id.btn_webview_back);
        webView = (WebView) findViewById(R.id.wv_webview);
        sdkWebview = (SDKWebview) findViewById(R.id.wv_sdkwebview);

        btnSdkWebView = (TextView) findViewById(R.id.btn_sdkwebview);
        btnWebApp = (TextView) findViewById(R.id.btn_webapp);

        btnWebView.setOnClickListener(onClickListener);
        btnSdkWebView.setOnClickListener(onClickListener);
        btnWebApp.setOnClickListener(onClickListener);

        btnBack.setOnClickListener(onClickListener);
        etUrl.setTextChangedListener(textWatcher);
        webView.setDownloadListener(new DownLoadAction());

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etUrl.setHintText(R.string.http_http);
        btnWebView.setEnabled(false);
        btnSdkWebView.setEnabled(false);
        btnWebApp.setEnabled(false);

        if (SDKConfiguration.getInstance().sdkMode == SDKConfiguration.SDK_MODE_L4VPN)
            SvnWebViewProxy.getInstance().setExceptionAddressList(true, "*.huawei.com;10.170.*");

        //设置webview走安全隧道
        //boolean res = SvnWebViewProxy.getInstance().setWebViewUseSVN(webView);
        //System.out.println("setWebViewUseSVN:" + res);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings()
                .setUserAgentString(
                        "Mozilla/5.0 (Linux; U; Android 2.2; en-gb; Nexus One Build/FRF50) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");
//        webView.getSettings().setUserAgentString(webView.getSettings().getUserAgentString());
        webView.setWebViewClient(new WebViewClient()
        {
            @SuppressWarnings("unused")
            final static int BUFFER_SIZE = 4096;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon)
            {
                super.onPageStarted(view, url, favicon);
            }

           @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                Log.i(TAG, "shouldOverrideUrlLoading:" + url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url)
            {

                super.onPageFinished(view, url);
            }

            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error)
            {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });

        SDKWebview.setSSOCallback(WebViewActivity.this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initData()
    {
        if (SDKConfiguration.getInstance().sdkMode == SDKConfiguration.SDK_MODE_APPVPN)
            etUrl.setText(Constants.HTTP_TEST_ORIG_WEBVIEW_URL);
        else
            etUrl.setText(Constants.HTTP_TEST_WEBVIEW_URL);
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btn_webview:
                {
                    //WebViewController.doWebViewPrepareAction(WebViewActivity.this);
                    webView.setVisibility(View.VISIBLE);
                    sdkWebview.setVisibility(View.GONE);
                    //load web view url
                    String url = etUrl.getText().trim();
                    webView.loadUrl(url);//must be called in UI thread
                    break;
                }
                case R.id.btn_sdkwebview:
                {
                    //                WebViewController.doWebViewPrepareAction(WebViewActivity.this);
                    webView.setVisibility(View.GONE);
                    sdkWebview.setVisibility(View.VISIBLE);
                    //load web view url
                    String url = etUrl.getText().trim();

                    sdkWebview.loadUrl(url);//must be called in UI thread
                    break;
                }
                case R.id.btn_webapp:
                {
                    String url = etUrl.getText().trim();

                    //use address input view
                    //WebApp.setUseInputView(true);
                    // default not use sso, not disable screenshot
                    WebApp.openUrl(WebViewActivity.this, url, false, false);
                    break;
                }
                case R.id.btn_webview_back:
                {
                    WebViewActivity.this.finish();
                    break;
                }
                default:
                {
                    throw new RuntimeException("Event not processed! view=" + v);
                }
            }
        }
    };

    class DownLoadAction implements DownloadListener
    {
        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength)
        {
            Log.i(TAG, "DownloadListener:" + url + ", mimeType:"
                    + mimetype + ",contentLength:" + contentLength);
        }
    }

    ;
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
                ThemeUtil.setBtnToEnable(btnWebView, WebViewActivity.this);
                ThemeUtil.setBtnToEnable(btnSdkWebView, WebViewActivity.this);
                ThemeUtil.setBtnToEnable(btnWebApp, WebViewActivity.this);

            }
            else
            {
                //set login button to be not enable
                ThemeUtil.setBtnToUnable(btnWebView, WebViewActivity.this);
                ThemeUtil.setBtnToUnable(btnSdkWebView, WebViewActivity.this);
                ThemeUtil.setBtnToUnable(btnWebApp, WebViewActivity.this);
            }
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private boolean checkEditContent()
    {
        if (etUrl.hasContent())
        {
            return true;
        }
        return false;
    }

    @Override
    public void ssoCallback(final String url)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(
                new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(WebViewActivity.this, "callback:" + url, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public boolean shouldExcuteCallback(String paramString)
    {
        // TODO Auto-generated method stub
        return true;
    }
}
