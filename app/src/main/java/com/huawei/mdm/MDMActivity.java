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
package com.huawei.mdm;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.BaseUtil;
import com.huawei.common.BaseActivity;
import com.huawei.svn.sdk.mdm.MDMCheckResult;
import com.huawei.svn.sdk.mdm.MdmCheck;
import com.huawei.widget.MyListView;
import com.matrix.myapplication.R;

import java.util.ArrayList;

/**
 * @author cWX223941
 */
public class MDMActivity extends BaseActivity
{
    private static final String TAG = "MDMCheck";
    private TextView btnCheck, btnBack;
    private TextView tvCheckTitle;
    private MyListView resultListView;
    private MdmCheckListAdapter checkAdapter;
    private ArrayList<MdmCheckResultEntity> resultList;
    private LinearLayout resultBlock;

    // private MDMCheckResult mdmCheckResult;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mdm);
        init();
    }

    private void init()
    {
        initView();
        initData();
        initListener();
    }

    private void initView()
    {
        btnBack = (TextView) findViewById(R.id.btn_mdm_back);
        btnCheck = (TextView) findViewById(R.id.btn_mdm_check);
        tvCheckTitle = (TextView) findViewById(R.id.tv_mdm_check_title);
        resultListView = (MyListView) findViewById(R.id.lv_mdm_check_list);
        resultBlock = (LinearLayout) findViewById(R.id.layout_mdm_check_result_block);
    }

    private void initData()
    {
        resultList = new ArrayList<MdmCheckResultEntity>();
    }

    private void initListener()
    {
        btnBack.setOnClickListener(onClickListener);
        btnCheck.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnBack.getId())
            {
                MDMActivity.this.finish();
            }
            else if (v.getId() == btnCheck.getId())
            {
                doMDMCheck();
            }
        }
    };

    private void doMDMCheck()
    {
        // set button enable
        btnCheck.setText(R.string.ing);
        btnCheck.setEnabled(false);
        tvCheckTitle.setText(R.string.checking);
        tvCheckTitle.setVisibility(View.VISIBLE);
        resultBlock.setVisibility(View.VISIBLE);
        // start a thread to check
        // login task
        AsyncTask<Object, Integer, MDMCheckResult> mdmCheckTask = new AsyncTask<Object, Integer, MDMCheckResult>()
        {
            @Override
            protected MDMCheckResult doInBackground(Object... paramVarArgs)
            {
                // MDM检查
                Log.i(TAG, "mdm check");
                return MdmCheck.checkMdmSpecific(MDMActivity.this, "com.huawei.svn.hiwork",
                        "com.huawei.svn.hiwork.mdm.manager.DefenseAdminReceiver");
            }

            @Override
            protected void onPreExecute()
            {
            }

            @Override
            protected void onPostExecute(MDMCheckResult mdmCheckResult)
            {
                showCheckResult(mdmCheckResult);
            }
        };
        // start login task
        mdmCheckTask.execute(new Object[]{});
    }

    private void showCheckResult(MDMCheckResult mdmCheckResult)
    {
        if (null == mdmCheckResult)
        {
            return;
        }
        MdmCheckResultEntity successResult = new MdmCheckResultEntity("Success",
                mdmCheckResult.getIsSuccess() == 0 ? true : false);
        if (mdmCheckResult.getIsSuccess() == 0)
        {
            BaseUtil.showToast(R.string.mdm_check_success, this);
        }
        else
        {
            BaseUtil.showToast(R.string.mdm_check_false, this);
        }
        MdmCheckResultEntity mdmEnableResult = new MdmCheckResultEntity("MDMEnabled", mdmCheckResult.isMDMEnabled());
        MdmCheckResultEntity bindResult = new MdmCheckResultEntity("bindResult",
                mdmCheckResult.getBindResult() == 1 || mdmCheckResult.getBindResult() == 10 ? true : false);

        MdmCheckResultEntity rootResult = null;
        MdmCheckResultEntity pwdCheckOk = null;
        MdmCheckResultEntity appCheckOk = null;
        // MdmCheckResultEntity longTimeNoLogin = new MdmCheckResultEntity("is
        // LongTimeNoLogin",mdmActivity.mdmCheckResult.isLongTimeNoLogin());
        MdmCheckResultEntity otherCheckOk = null;

        if (mdmCheckResult.getBindResult() == 1 || mdmCheckResult.getBindResult() == 10)
        {
            rootResult = new MdmCheckResultEntity("RootCheck", !mdmCheckResult.isRoot());
            pwdCheckOk = new MdmCheckResultEntity("PwdCheckOK", mdmCheckResult.isPwdCheckOK());
            appCheckOk = new MdmCheckResultEntity("AppCheckOK", mdmCheckResult.isAppCheckOK());
            // MdmCheckResultEntity longTimeNoLogin = new
            // MdmCheckResultEntity("is
            // LongTimeNoLogin",mdmActivity.mdmCheckResult.isLongTimeNoLogin());
            otherCheckOk = new MdmCheckResultEntity("OtherCheckOK", mdmCheckResult.isOtherCheckOK());
        }
        else
        {
            rootResult = new MdmCheckResultEntity("RootCheck", false);
            pwdCheckOk = new MdmCheckResultEntity("PwdCheckOK", false);
            appCheckOk = new MdmCheckResultEntity("AppCheckOK", false);
            // MdmCheckResultEntity longTimeNoLogin = new
            // MdmCheckResultEntity("is
            // LongTimeNoLogin",mdmActivity.mdmCheckResult.isLongTimeNoLogin());
            otherCheckOk = new MdmCheckResultEntity("OtherCheckOK", false);
        }

        resultList.clear();
        resultList.add(successResult);
        resultList.add(mdmEnableResult);
        resultList.add(bindResult);
        resultList.add(rootResult);
        resultList.add(pwdCheckOk);
        resultList.add(appCheckOk);
        // mdmActivity.resultList.add(longTimeNoLogin);
        resultList.add(otherCheckOk);
        tvCheckTitle.setText(R.string.result);
        checkAdapter = new MdmCheckListAdapter(this, resultList);
        resultListView.setAdapter(checkAdapter);
        btnCheck.setText(R.string.mdm_check);
        btnCheck.setEnabled(true);
    }

    private void getResultListFromMDMResult()
    {
    }
}
