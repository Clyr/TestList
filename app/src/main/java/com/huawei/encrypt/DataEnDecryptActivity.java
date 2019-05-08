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
package com.huawei.encrypt;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huawei.BaseUtil;
import com.huawei.StringUtil;
import com.huawei.ThemeUtil;
import com.huawei.common.BaseActivity;
import com.matrix.myapplication.R;


/**
 * @author cWX223941
 */
public class DataEnDecryptActivity extends BaseActivity {
    private static final String TAG = "DataEnDecryptActivity";
    private TextView btnEncrypt, btnDecrypt, btnBack, btnGuide;
    private EditText etOriginData;
    private TextView tvEncryptedData;
    private ImageView iconLock;
    byte[] encryptedData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_endecrypt);
        init();
    }

    private void init() {
        initView();
        initData();
        initListener();
    }

    private void initView() {
        btnEncrypt = (TextView) findViewById(R.id.btn_dataencrypt_encrypt);
        btnDecrypt = (TextView) findViewById(R.id.btn_dataencrypt_decrypt);
        btnBack = (TextView) findViewById(R.id.btn_dataencrypt_back);
        btnGuide = (TextView) findViewById(R.id.btn_dataencrypt_guide);
        etOriginData = (EditText) findViewById(R.id.et_dataencrypt_data);
        tvEncryptedData = (TextView) findViewById(R.id.tv_dataencrypt_result);
        iconLock = (ImageView) findViewById(R.id.iv_dataencrypt_lock);
    }

    private void initData() {
        btnEncrypt.setEnabled(false);
        btnDecrypt.setEnabled(false);
    }

    private void initListener() {
        btnEncrypt.setOnClickListener(onClickListener);
        btnDecrypt.setOnClickListener(onClickListener);
        btnBack.setOnClickListener(onClickListener);
        btnGuide.setOnClickListener(onClickListener);
        etOriginData.addTextChangedListener(encryptTextWatcher);
        tvEncryptedData.addTextChangedListener(decryptTextWatcher);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == btnEncrypt.getId()) {
                doEncrypt();
            } else if (v.getId() == btnDecrypt.getId()) {
                doDecrypt();
            } else if (v.getId() == btnBack.getId()) {
                DataEnDecryptActivity.this.finish();
            }
        }
    };
    private TextWatcher encryptTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (checkEncryptEditContent()) {
                //set login button to be enable
                ThemeUtil
                        .setBtnToEnable(btnEncrypt, DataEnDecryptActivity.this);
            } else {
                //set login button to be not enable
                ThemeUtil
                        .setBtnToUnable(btnEncrypt, DataEnDecryptActivity.this);
            }
        }
    };
    private TextWatcher decryptTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (checkDecryptEditContent()) {
                //set login button to be enable
                ThemeUtil
                        .setBtnToEnable(btnDecrypt, DataEnDecryptActivity.this);
            } else {
                //set login button to be not enable
                ThemeUtil
                        .setBtnToUnable(btnDecrypt, DataEnDecryptActivity.this);
            }
        }
    };

    /**
     * if Encrypt Data edit text have content,then return true,otherwise return false
     *
     * @return true:all have content;false:otherwise
     */
    private boolean checkEncryptEditContent() {
        if (!StringUtil.isEmpty(etOriginData.getText().toString())) {
            return true;
        }
        return false;
    }

    /**
     * if Decrypt Data edit text have content,then return true,otherwise return false
     *
     * @return true:all have content;false:otherwise
     */
    private boolean checkDecryptEditContent() {
        if (!StringUtil.isEmpty(tvEncryptedData.getText().toString())) {
            return true;
        }
        return false;
    }

    //数据加密
    private void doEncrypt() {
        String originText = etOriginData.getText().toString().trim();
        if (StringUtil.isEmpty(originText)) {
            Log.e(TAG, "DecryptData is empty");
            return;
        }

        //加密接口
//        encryptedData = EncryptTool.Encrypt(originText);

        tvEncryptedData.setText(BaseUtil.Bytes2HexString(encryptedData));

        iconLock.setImageResource(R.drawable.icon_encrypt_disabled);
        btnDecrypt.setVisibility(View.VISIBLE);
    }


    //数据解密
    private void doDecrypt() {
        if (null == encryptedData) {
            Log.e(TAG, "Encrypt Byte is null.");
            return;
        }

        //解密接口
//        String result = EncryptTool.Decrypt(encryptedData);
//        etOriginData.setText(result);

        iconLock.setImageResource(R.drawable.icon_decrypt_disabled);
        btnEncrypt.setVisibility(View.VISIBLE);
    }


}
