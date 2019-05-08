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

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


/**
 * @author cWX223941
 */
public class UploadFragment extends Fragment
{
    private static final String TAG = "UploadFragment";
    private static final String BOUNDARY = "---------------!@#1234567890"; // 分割符号
    private View v;
    private MyEditText etUrl, etSubUrl, etFilePath;
    private TextView btnBrowse, btnUpload;
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
        v = inflater.inflate(R.layout.fragment_http_upload, container, false);
        init();
        return v;
    }

    private void init()
    {
        initView();
        initData();

//        System.setProperty("jsse.enableSNIExtension", "false");
//    	System.setProperty("jsse.enableSNIExtension", "true");
//        System.setProperty("https.protocols", "TLSv1.2");//2008
//        System.setProperty("https.protocols", "TLSv1.1");//2006
//        System.setProperty("https.protocols", "TLSv1");//1999
//        System.setProperty("https.protocols", "SSLv3");//1996
//        System.setProperty("https.protocols", "TLS");//1996
//        System.setProperty("https.protocols", "SSL");//1996
//        System.setProperty("com.sun.net.ssl.rsaPreMasterSecretFix","true");

//        System.setProperty("javax.net.debug", "all");//设置问题查看，
//        System.setProperty("javax.net.debug", "help");//设置问题查看，

    }

    private void initView()
    {
        etUrl = (MyEditText) v.findViewById(R.id.et_http_upload_url);
        etSubUrl = (MyEditText) v.findViewById(R.id.et_http_upload_suburl);
        etFilePath = (MyEditText) v.findViewById(R.id.et_http_upload_filepath);
        btnBrowse = (TextView) v.findViewById(R.id.btn_http_upload_browse);
        btnUpload = (TextView) v.findViewById(R.id.btn_http_upload_request);
        resultBlock = (LinearLayout) v
                .findViewById(R.id.layout_http_upload_result_block);
        tvRequest = (TextView) v.findViewById(R.id.tv_http_upload_request);
        tvResponse = (TextView) v.findViewById(R.id.tv_http_upload_response);

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etSubUrl.setItemBlockBackground(R.drawable.bg_edit);
        etFilePath.setItemBlockBackground(R.drawable.bg_edit);
        etUrl.setHintText(R.string.http_http);
        etSubUrl.setHintText(R.string.http_subhttp);
        etFilePath.setHintText(R.string.http_upload_filepath);
        btnUpload.setEnabled(false);
        btnBrowse.setEnabled(false);
        ThemeUtil.setBtnToEnable(btnBrowse, getActivity());

        btnBrowse.setOnClickListener(onClickListener);
        btnUpload.setOnClickListener(onClickListener);
        etUrl.setTextChangedListener(textWatcher);
        etFilePath.setTextChangedListener(textWatcher);
    }

    private void initData()
    {
        etUrl.setText(Constants.HTTP_TEST_BASE_URL);
        etSubUrl.setText(Constants.HTTP_TEST_UPLOAD_URL);
    }


    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnBrowse.getId())
            {
                //open file browse
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
            else if (v.getId() == btnUpload.getId())
            {
                if(getActivity() == null)
                {
                    return;
                }

                btnUpload.setText(R.string.ing);
                btnUpload.setEnabled(false);
                tvRequest.setText("");
                tvResponse.setText("");

                if (Constants.ACTIVITY_SEND_HTTPTYPE_URLCONNECTION
                        .equals(((HttpActivity) getActivity()).getHttpType()))
                {
                    doUploadThroughURLConnection();
                }
                else if (Constants.ACTIVITY_SEND_HTTPTYPE_HTTPCLIENT
                        .equals(((HttpActivity) getActivity()).getHttpType()))
                {
                    doUploadThroughHttpClient();
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
                ThemeUtil.setBtnToEnable(btnUpload, getActivity());
            }
            else
            {
                //set login button to be not enable
                ThemeUtil.setBtnToUnable(btnUpload, getActivity());
            }
        }
    };

    public String getAbsolutePath(Uri uri)
    {
        if (uri == null)
        {
            return null;
        }

        if (uri.toString().startsWith("file"))
        {
            return uri.toString();
        }

        if (Build.VERSION.SDK_INT >= 19)
        {
            String id = uri.getLastPathSegment().split(":")[0];
            final String[] imageColumns = {MediaStore.Images.Media.DATA};
            final String imageOrderBy = null;
            // Uri tempUri = getActivity().getUri();
            Cursor imageCursor = getActivity().getContentResolver().query(uri, imageColumns,
                    MediaStore.Images.Media._ID + "=" + id, null, imageOrderBy);
            if (imageCursor.moveToFirst())
            {
                return imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            else
            {
                return null;
            }
        }
        else
        {
            String[] projection = {MediaColumns.DATA};

            Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null)
            {
                int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            else
            {
                return null;
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            //            UploadController.setBrowseFilePath(data, UploadFragment.this);
            Uri uri = data.getData();

            String filePath = getAbsolutePath(uri);
            if (filePath != null)
            {
                etFilePath.setText(filePath);
            }
            else
            {
                etFilePath.setText("");
            }


//            Cursor cursor = getActivity().getContentResolver().query(uri, null,
//                    null, null, null);
//            cursor.moveToFirst();
//            etFilePath
//                    .setText(cursor.getString(cursor.getColumnIndex("_data")));
            //            for (int i = 0; i < cursor.getColumnCount(); i++)  
            //            {
            //                System.out.println(i + "-" + cursor.getColumnName(i) + "-" + cursor.getString(i));
            //            }
        }
    }

    /**
     * if all edit text have content,then return true,otherwise return false
     *
     * @return true:all have content;false:otherwise
     */
    private boolean checkEditContent()
    {
        if (etUrl.hasContent() && etFilePath.hasContent())
        {
            return true;
        }
        return false;
    }

    private void handleUploadResult(boolean status, String request,
                                    String response)
    {
        endTime = new Date().getTime();
        System.out.println("用时=：" + (endTime - startTime) / 1000);
//    	System.out.println("使用 Protocal :" + System.getProperty("https.protocols"));
//    	try {
//			System.out.println("SSLContext使用 Protocal :" + SSLContext.getDefault().getProtocol());
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
        if (status)
        {
            Log.e(TAG, "upload success.");
            //            uploadFragment.btnUpload.setVisibility(View.VISIBLE);
            btnUpload.setText(R.string.upload);
            btnUpload.setEnabled(true);
            resultBlock.setVisibility(View.VISIBLE);
            tvRequest.setText(request);
            tvResponse.setText(response);
        }
        else
        {
            Log.e(TAG, "upload false.");
            btnUpload.setText(R.string.upload);
            btnUpload.setEnabled(true);
        }

    }

    private void doUploadThroughURLConnection()
    {
        final String urlStr = etUrl.getText().trim()
                + etSubUrl.getText().trim();
        final File toUploadFile = new File(etFilePath.getText().trim());
        if (!toUploadFile.exists())
        {
            Log.e(TAG, "file is not exist.");
        }

        final String fileName = toUploadFile.getName();

        AsyncTask<Object, Integer, Boolean> uploadTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = false;
                try
                {
                    startTime = new Date().getTime();
                    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
                    {
                        @Override
                        public boolean verify(String hostname, SSLSession session)
                        {
                            return true;
                        }
                    });
                    URLConnectionFactoryHelper.setURLStreamHandlerFactory();

                    String name = "image";
                    URL urlPost = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) urlPost
                            .openConnection();
                    StringBuilder sb = new StringBuilder();
                    String dataStart = sb
                            .append("--" + BOUNDARY + "\r\n")
                            .append("Content-Disposition: form-data; name=\""
                                    + name + "\"; filename=\""
                                    + fileName + "\"\r\n")
                            .append("Content-Type: "
                                    + BaseUtil.getContentType(toUploadFile)
                                    + "\r\n\r\n").toString();
                    String dataEnd = "\r\n--" + BOUNDARY + "--\r\n";
                    int size = dataStart.getBytes().length + (int) toUploadFile.length()
                            + dataEnd.length();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setFixedLengthStreamingMode(size);
                    connection.setUseCaches(false);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(10000);
//                    if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
//                    	connection.setRequestProperty("Connection", "close");
//                    }else{
                    connection.setRequestProperty("Connection", "Keep-Alive");
//                    }
                    connection.setRequestProperty("Content-Type",
                            "multipart/form-data; boundary=" + BOUNDARY + "; charset=UTF-8");


                    connection.connect();
                    OutputStream os = connection.getOutputStream();
                    os.write(dataStart.getBytes());
                    //write file
                    FileUtil.write(os, toUploadFile);
                    os.write(dataEnd.getBytes());
                    os.flush();
                    os.close();
                    InputStream is = connection.getInputStream();
                    httpRequest = urlStr;
                    httpResponse = FileUtil.read(is);
                    result = true;
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
                handleUploadResult(result, httpRequest, httpResponse);
            }
        };
        uploadTask.execute(new Object());
    }

    long startTime = 0L;
    long endTime = 0L;

    private void doUploadThroughHttpClient()
    {

        final String urlStr = etUrl.getText().trim()
                + etSubUrl.getText().trim();
        final File toUploadFile = new File(etFilePath.getText().trim());
        if (!toUploadFile.exists())
        {
            Log.e(TAG, "file is not exist.");
        }
        AsyncTask<Object, Integer, Boolean> uploadTask = new AsyncTask<Object, Integer, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object... paramVarArgs)
            {
                boolean result = false;
                try
                {
                    startTime = (new Date()).getTime();
                    HttpResponse response = null;
                    //HttpClient hc =  new SvnHttpClient();
                    HttpClient hc = HttpClientHelp.getInstance();
                    HttpPost postMethod = new HttpPost(urlStr);

                    postMethod.addHeader("Connnection", "close");

                    MultipartEntity mpEntity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE, null,
                            Charset.forName("UTF-8"));

                    FileBody file = new FileBody(toUploadFile);
                    mpEntity.addPart("image", file);
                    postMethod.setEntity(mpEntity);
                    response = hc.execute(postMethod);
                    Log.i(TAG, "resCode = "
                            + response.getStatusLine().getStatusCode());
                    httpRequest = postMethod.getRequestLine().toString();
                    httpResponse = EntityUtils.toString(response.getEntity(),
                            "utf-8");
                    result = true;
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
                handleUploadResult(result, httpRequest, httpResponse);
            }
        };
        uploadTask.execute(new Object());
    }
}
