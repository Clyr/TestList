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
import com.huawei.svn.sdk.socket.SvnSocket;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

public class SvnSocketFragment extends Fragment
{
    private View view;
    private MyEditText etUrl, etPort, etContent;
    private TextView btnConnect;
    private LinearLayout resultBlock;
    private TextView socketRequest, socketResponse;
    private String ipAddress;
    private int port;
    private String content;
    private String connectRequest, connectResponse;

    private static final String TAG = "SvnSocketFragment";
    private String httpRequest;
    private String httpResponse;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_svn_socket, container, false);
        init();
        return view;
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        etUrl = (MyEditText) view.findViewById(R.id.et_socket_url);
        etPort = (MyEditText) view.findViewById(R.id.et_socket_port);
        etContent = (MyEditText) view.findViewById(R.id.et_socket_content);
        btnConnect = (TextView) view.findViewById(R.id.btn_socket_connect);
        resultBlock = (LinearLayout) view.findViewById(R.id.layout_socket_result_block);
        socketRequest = (TextView) view.findViewById(R.id.tv_socket_request);
        socketResponse = (TextView) view.findViewById(R.id.tv_socket_response);

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etPort.setItemBlockBackground(R.drawable.bg_edit);
        etContent.setItemBlockBackground(R.drawable.bg_edit);

        etUrl.setHintText(R.string.http_host);
        etPort.setHintText(R.string.http_port);
        etContent.setHintText(R.string.http_content);
        btnConnect.setEnabled(false);

        etUrl.setTextChangedListener(textWatcher);
        etPort.setTextChangedListener(textWatcher);
        etContent.setTextChangedListener(textWatcher);
        btnConnect.setOnClickListener(loginListener);
    }

    private void initData()
    {
        etUrl.setText(Constants.HTTP_TEST_SERVER);
        etPort.setText(Constants.HTTP_TEST_PORT);
        etContent.setText("GET / HTTP/1.1\r\nHost:" + etUrl.getText().trim() + ":" + etPort.getText() + "\r\nConnection:close\r\n\r\n");
    }

    private OnClickListener loginListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            btnConnect.setText(R.string.ing);
            btnConnect.setEnabled(false);
            socketRequest.setText("");
            socketResponse.setText("");
            doHTTPRequest();

            //doUpload();
        }
    };

    long startTime = 0L;
    long endTime = 0L;

    private void doUpload()
    {

        String urlString = etUrl.getText().toString();
        String portString = etPort.getText().toString();
        //String contentString = etContent.getText().toString();
        ipAddress = urlString.trim();
        //content = "GET / HTTP/1.1\r\nHost:10.170.26.248:8180\r\nConnection:Close\r\n\r\n";

        //content = contentString;
        port = 8080;
        if (portString.matches("[0-9]+"))
        {
            port = Integer.parseInt(portString);
        }


        final String BOUNDARY = "---------------!@#1234567890"; // 分割符号
        final String urlStr = Constants.HTTP_TEST_URL_PREFIX + Constants.HTTP_TEST_UPLOAD_URL;

        final File toUploadFile = new File("/sdcard/SDK.rar");
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


                    String name = "image";

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

                    StringBuilder header = new StringBuilder();
                    header.append("POST ").append(urlStr).append(" HTTP/1.1\r\n");
                    header.append("Host: ").append(Constants.HTTP_TEST_SERVER).append(":").append(Constants.HTTP_TEST_PORT).append("\r\n");
                    header.append("Content-Length: ").append(size).append("\r\n");
                    header.append("User-Agent: ").append("Dalvik/1.6.0").append("\r\n");

                    header.append("Connection: close").append("\r\n");

//              
//                    	connection.setRequestProperty("Connection", "close");
//                   
                    header.append("Content-Type: ").append("multipart/form-data; boundary=" + BOUNDARY + "; charset=UTF-8").append("\r\n");
                    header.append("\r\n");
                    //Socket svnSocket = new SvnSocket("172.22.8.206", port);

                    Socket svnSocket = new SvnSocket(ipAddress, port);
                    OutputStream os = svnSocket.getOutputStream();
                    InputStream is = svnSocket.getInputStream();
                    os.write(header.toString().getBytes());
                    os.write(dataStart.getBytes());
                    //write file
                    FileUtil.write(os, toUploadFile);
                    os.write(dataEnd.getBytes());
                    os.flush();

                    httpRequest = urlStr;

                    byte[] buffer = new byte[1024];
                    int count = 0;

                    sb = new StringBuilder();
                    while ((count = is.read(buffer)) > 0)
                    {
                        sb.append(new String(buffer, 0, count));
                    }
                    httpResponse = sb.toString();


                    os.close();


                    result = true;

                    svnSocket.close();
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
                // handleUploadResult(result, httpRequest, httpResponse);

                endTime = new Date().getTime();

                System.out.println("httpResponse时=：" + httpResponse);
                System.out.println("用时=：" + (endTime - startTime) / 1000);

                btnConnect.setText(R.string.svn_socket_connect);
                btnConnect.setEnabled(true);
            }
        };
        uploadTask.execute(new Object());
    }

    private void doHTTPRequest()
    {
        String urlString = etUrl.getText().toString();
        String portString = etPort.getText().toString();
        String contentString = etContent.getText().toString();
        ipAddress = urlString.trim();
        //content = "GET / HTTP/1.1\r\nHost:10.170.26.248:8180\r\nConnection:Close\r\n\r\n";

        content = contentString;
        port = 8080;
        if (portString.matches("[0-9]+"))
        {
            port = Integer.parseInt(portString);
        }
        AsyncTask<Object, Integer, Boolean> connectTask = new AsyncTask<Object, Integer, Boolean>()
        {

            @Override
            protected Boolean doInBackground(Object... params)
            {
                try
                {

                    Socket svnSocket = new SvnSocket(ipAddress, port);

                    //Socket svnSocket = new Socket(ipAddress, port);

                    OutputStream outputStream = svnSocket.getOutputStream();
                    byte[] buffer = content.getBytes();
                    outputStream.write(buffer);
                    InputStream inputStream = svnSocket.getInputStream();
                    StringBuffer out = new StringBuffer();
                    byte[] b = new byte[4096];
                    int count = -1;
                    while ((count = inputStream.read(b)) > 0)
                    {
                        out.append(new String(b, 0, count));
                    }
                    connectResponse = out.toString();

                    svnSocket.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    connectResponse = "SvnSocket Request Error!";
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean result)
            {
                connectRequest = "Request:\n    " + ipAddress + ":" + port;
                connectResponse = "Response:\n    " + connectResponse;
                handleLoginResult(result, connectRequest, connectResponse);
            }
        };
        connectTask.execute(new Object());
    }

    private void handleLoginResult(Boolean status, String request, String response)
    {
        if (status)
        {
            btnConnect.setText(R.string.svn_socket_connect);
            btnConnect.setEnabled(true);
            resultBlock.setVisibility(View.VISIBLE);
            socketRequest.setText(request);
            socketResponse.setText(response);
        }
        else
        {
            btnConnect.setText(R.string.svn_socket_connect);
            btnConnect.setEnabled(true);
        }
    }

    private TextWatcher textWatcher = new TextWatcher()
    {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (checkEditContent())
            {
                // set login button to be enable
                ThemeUtil.setBtnToEnable(btnConnect, getActivity());
            }
            else
            {
                // set login button to be not enable
                ThemeUtil.setBtnToUnable(btnConnect, getActivity());
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
        if (etUrl.hasContent() && etPort.hasContent() && etContent.hasContent())
        {
            return true;
        }
        return false;
    }
}
