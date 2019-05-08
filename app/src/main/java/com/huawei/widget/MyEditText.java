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
package com.huawei.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huawei.StringUtil;
import com.matrix.myapplication.R;


/**
 * @author cWX223941
 */
public class MyEditText extends LinearLayout
{
    private TextView hintText;
    private EditText editText;
    private LinearLayout itemBlock;

    public MyEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        LayoutInflater.from(getContext()).inflate(R.layout.custom_edittext,
                this);
        hintText = (TextView) findViewById(R.id.et_custom_hint);
        editText = (EditText) findViewById(R.id.et_custom_edit);
        itemBlock = (LinearLayout) findViewById(R.id.layout_custom_et_block);
    }

    public void setTextSize(float size)
    {
        editText.setTextSize(size);
    }

    public void setSelection(int len)
    {
        editText.setSelection(len);
    }

    public void setHintText(int resId)
    {
        hintText.setText(resId);
    }

    public String getText()
    {
        return editText.getText().toString();
    }

    public void setText(String speedOuterSite)
    {
        editText.setText(speedOuterSite);
    }

    /**
     * forbid user input any thing
     */
    public void forbidInput()
    {
        editText.setEnabled(false);
    }

    public void allowInput()
    {
        editText.setEnabled(true);
    }

    public void setEditFocusChangeListener(
            OnFocusChangeListener focusChangeListener)
    {
        editText.setOnFocusChangeListener(focusChangeListener);
    }

    public void setTextChangedListener(TextWatcher textWatcher)
    {
        editText.addTextChangedListener(textWatcher);
    }

    public boolean hasContent()
    {
        if (!StringUtil.isEmpty(editText.getText().toString()))
        {
            return true;
        }
        return false;
    }

    public void setItemBlockBackground(int resId)
    {
        itemBlock.setBackgroundResource(resId);
    }

    public void hiddenHintText()
    {
        hintText.setVisibility(View.GONE);
    }
}
