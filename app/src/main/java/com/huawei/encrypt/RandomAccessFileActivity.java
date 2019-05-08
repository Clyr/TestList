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

package com.huawei.encrypt;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.BaseUtil;
import com.huawei.FileUtil;
import com.huawei.StringUtil;
import com.huawei.ThemeUtil;
import com.huawei.common.BaseActivity;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.fsm.SvnFile;
import com.huawei.svn.sdk.fsm.thirdpart.zip.SvnRandomAccessFile;
import com.matrix.myapplication.R;

import java.io.IOException;

/**
 * Created by zWX305297 on 2016/2/2.
 */
public class RandomAccessFileActivity extends BaseActivity implements OnClickListener {
    private TextView btnRandomAccessFileBack;
    private TextView btnRandomAccessFileGuide;
    private EditText etRandomAccessFileFilepath;
    private TextView btnRandomAccessFileBrowse;
    private EditText etRandomAccessFileInput;
    private TextView btnRandomAccessFileInput;
    private EditText etRandomAccessFileOutput;
    private TextView btnRandomAccessFileOutput;
    private final int FILE_SYSTEM_REQUEST_CODE = 1;
    private FileSystemEntity fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_access_file);
        init();
    }

    private void init() {
        initView();
        initData();
    }

    private void initView() {
        btnRandomAccessFileBack = (TextView) findViewById(R.id.btn_random_access_file_back);
        btnRandomAccessFileGuide = (TextView) findViewById(R.id.btn_random_access_file_guide);
        etRandomAccessFileFilepath = (EditText) findViewById(R.id.et_random_access_file_filepath);
        btnRandomAccessFileBrowse = (TextView) findViewById(R.id.btn_random_access_file_browse);
        etRandomAccessFileInput = (EditText) findViewById(R.id.et_random_access_file_input);
        btnRandomAccessFileInput = (TextView) findViewById(R.id.btn_random_access_file_input);
        etRandomAccessFileOutput = (EditText) findViewById(R.id.et_random_access_file_output);
        btnRandomAccessFileOutput = (TextView) findViewById(R.id.btn_random_access_file_output);
    }

    private void initData() {
        etRandomAccessFileInput.addTextChangedListener(textWatcher);
        etRandomAccessFileFilepath.addTextChangedListener(textWatcher);
        btnRandomAccessFileBack.setOnClickListener(this);
        btnRandomAccessFileGuide.setOnClickListener(this);
        btnRandomAccessFileBrowse.setOnClickListener(this);
        btnRandomAccessFileInput.setOnClickListener(this);
        btnRandomAccessFileOutput.setOnClickListener(this);
        etRandomAccessFileFilepath.setText(FileUtil.getSDPath() + "/SvnSdkDemo/Original/test.txt");
        etRandomAccessFileInput.setText("This is HelloWorld");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_random_access_file_back:
                finish();
                break;
            case R.id.btn_random_access_file_guide:
                break;
            case R.id.btn_random_access_file_browse:
                Intent intent = new Intent(RandomAccessFileActivity.this, FileBrowserActivity.class);
                startActivityForResult(intent, FILE_SYSTEM_REQUEST_CODE);
                break;
            case R.id.btn_random_access_file_input:
                doInput();
                break;
            case R.id.btn_random_access_file_output:
                doOutput();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_SYSTEM_REQUEST_CODE) {
                fileInfo = (FileSystemEntity) data.getSerializableExtra(Constants.FILE_BROWSE_RESULT);
                etRandomAccessFileFilepath.setText(fileInfo.getFullPath());
            }
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (StringUtil.isEmpty(etRandomAccessFileFilepath.getText().toString())
                    || new SvnFile(etRandomAccessFileFilepath.getText().toString()).isDirectory()) {
                ThemeUtil.setBtnToUnable(btnRandomAccessFileOutput, RandomAccessFileActivity.this);
                ThemeUtil.setBtnToUnable(btnRandomAccessFileInput, RandomAccessFileActivity.this);
            } else {
                if (StringUtil.isEmpty(etRandomAccessFileInput.getText().toString())) {
                    ThemeUtil.setBtnToUnable(btnRandomAccessFileInput, RandomAccessFileActivity.this);
                } else {
                    ThemeUtil.setBtnToEnable(btnRandomAccessFileInput, RandomAccessFileActivity.this);
                }
                ThemeUtil.setBtnToEnable(btnRandomAccessFileOutput, RandomAccessFileActivity.this);
            }
        }
    };

    private void doInput() {
        if (btnRandomAccessFileInput.isEnabled()) {
            SvnFile file = new SvnFile(etRandomAccessFileFilepath.getText().toString());
            SvnRandomAccessFile svnRandomAccessFile = new SvnRandomAccessFile(file, "rw");
            try {
                svnRandomAccessFile.seek(0);
                String inputText = etRandomAccessFileInput.getText().toString();
                svnRandomAccessFile.write(inputText.getBytes());
                if (svnRandomAccessFile.getFd() != 0) {
                    svnRandomAccessFile.close();
                    BaseUtil.showToast("添加成功", this);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void doOutput() {
        if (btnRandomAccessFileOutput.isEnabled()) {
            SvnFile file = new SvnFile(etRandomAccessFileFilepath.getText().toString());
            SvnRandomAccessFile svnRandomAccessFile = new SvnRandomAccessFile(file, "r");
            try {
                svnRandomAccessFile.seek(0);
                int count = -1;
                byte[] bs = new byte[1024];
                StringBuffer buffer = new StringBuffer();
                while ((count = svnRandomAccessFile.read(bs)) != -1) {
                    String string = new String(bs, 0, count);
                    buffer.append(string);
                }
                etRandomAccessFileOutput.setText(buffer.toString());
                if (svnRandomAccessFile.getFd() != 0) {
                    svnRandomAccessFile.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
