package com.huawei.http;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.huawei.ThemeUtil;
import com.huawei.common.Constants;
import com.huawei.widget.MyEditText;
import com.matrix.myapplication.R;


public class TcpTestFragment extends Fragment
{
    private View view;
    private MyEditText etUrl, etPort, etTimeout;
    private TextView btnDetect;
    private LinearLayout resultBlock;
    private TextView tcpRequest, tcpResponse;
    private String ipAddress;
    private int port, timeout;
    private String detectRequest, detectResponse;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_tcp_test, container, false);
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
        etUrl = (MyEditText) view.findViewById(R.id.et_tcp_url);
        etPort = (MyEditText) view.findViewById(R.id.et_tcp_port);
        etTimeout = (MyEditText) view.findViewById(R.id.et_tcp_timeout);
        btnDetect = (TextView) view.findViewById(R.id.btn_tcp_detect);
        resultBlock = (LinearLayout) view.findViewById(R.id.layout_tcp_result_block);
        tcpRequest = (TextView) view.findViewById(R.id.tv_tcp_request);
        tcpResponse = (TextView) view.findViewById(R.id.tv_tcp_response);

        etUrl.setItemBlockBackground(R.drawable.bg_edit);
        etPort.setItemBlockBackground(R.drawable.bg_edit);
        etTimeout.setItemBlockBackground(R.drawable.bg_edit);

        etUrl.setHintText(R.string.http_host);
        etPort.setHintText(R.string.http_port);
        etTimeout.setHintText(R.string.http_timeout);
        btnDetect.setEnabled(false);

        etUrl.setTextChangedListener(textWatcher);
        etPort.setTextChangedListener(textWatcher);
        etTimeout.setTextChangedListener(textWatcher);
        btnDetect.setOnClickListener(loginListener);
    }

    private void initData()
    {
        etUrl.setText(Constants.HTTP_TEST_SERVER);
        etPort.setText(Constants.HTTP_TEST_PORT);
        etTimeout.setText("10");
    }

    private OnClickListener loginListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            btnDetect.setText(R.string.ing);
            btnDetect.setEnabled(false);
            tcpRequest.setText("");
            tcpResponse.setText("");
            detect();
        }
    };

    private void detect()
    {
        String urlString = etUrl.getText().toString();
        String portString = etPort.getText().toString();
        String timeoutString = etTimeout.getText().toString();
        ipAddress = urlString.trim();
        port = 8080;
        if (portString.matches("[0-9]+"))
        {
            port = Integer.parseInt(portString);
        }
        timeout = 10;
        if (timeoutString.matches("[0-9]+"))
        {
            timeout = Integer.parseInt(timeoutString);
        }
        AsyncTask<Object, Integer, Integer> detectTask = new AsyncTask<Object, Integer, Integer>()
        {

            @Override
            protected Integer doInBackground(Object... params)
            {
//				int result = NetStatusUtils.detectTCP(ipAddress, port, timeout);
//				return result;
                return 0;
            }

            @Override
            protected void onPostExecute(Integer result)
            {
                detectRequest = "Request:\n    " + ipAddress + ":" + port;
                switch (result)
                {
                    case 0:
                        detectResponse = "success";
                        break;
                    case -1:
                        detectResponse = "探测失败";
                        break;
                    case -2:
                        detectResponse = "隧道不在线";
                        break;
                    case -3:
                        detectResponse = "当前网络不可用";
                        break;
                    case -10:
                        detectResponse = "参数错误";
                        break;
                    default:
                        detectResponse = "未知错误";
                        break;
                }
                detectResponse = "Response:\n    " + detectResponse;
                handleLoginResult(true, detectRequest, detectResponse);
            }
        };
        detectTask.execute(new Object());
    }

    private void handleLoginResult(Boolean status, String request, String response)
    {
        if (status)
        {
            btnDetect.setText(R.string.tcp_test);
            btnDetect.setEnabled(true);
            resultBlock.setVisibility(View.VISIBLE);
            tcpRequest.setText(request);
//			tcpResponse.setText(response);
            tcpResponse.setText("暂停使用");
        }
        else
        {
            btnDetect.setText(R.string.tcp_test);
            btnDetect.setEnabled(true);
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
                ThemeUtil.setBtnToEnable(btnDetect, getActivity());
            }
            else
            {
                // set login button to be not enable
                ThemeUtil.setBtnToUnable(btnDetect, getActivity());
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
        if (etUrl.hasContent() && etPort.hasContent() && etTimeout.hasContent())
        {
            return true;
        }
        return false;
    }
}
