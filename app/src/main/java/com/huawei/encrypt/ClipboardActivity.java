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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.anyoffice.sdk.sandbox.SDKClipboard;

import com.huawei.common.BaseActivity;
import com.huawei.svn.sdk.ui.SDKEditText;
import com.matrix.myapplication.R;

@SuppressLint("NewApi")
public class ClipboardActivity extends BaseActivity
{
    private static final String TAG = "ClipboardActivity";


    private ActionMode actionMode = null;


    private OnFocusChangeListener focusChanged = new OnFocusChangeListener()
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            // TODO Auto-generated method stub
            if (actionMode != null)
            {
                actionMode.finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipboard);

        SDKEditText editTextCustom = (SDKEditText) findViewById(R.id.et_clipboard_source_custom);
        editTextCustom.setText("text from custom clipboard");

        final StyleCallback callback = new StyleCallback(editTextCustom);
        editTextCustom.setOnLongClickListener(new View.OnLongClickListener()
        {

            @Override
            public boolean onLongClick(View p1)
            {
                // TODO: Implement this method
                actionMode = startActionMode(callback);
                return false;
            }

        });

        editTextCustom.setCustomSelectionActionModeCallback(callback);

        editTextCustom.setOnFocusChangeListener(focusChanged);


        SDKEditText editTextCustomDest = (SDKEditText) findViewById(R.id.et_clipboard_dest_custom);

        final StyleCallback callback2 = new StyleCallback(editTextCustomDest);
        editTextCustomDest.setOnLongClickListener(new View.OnLongClickListener()
        {

            @Override
            public boolean onLongClick(View p1)
            {
                // TODO: Implement this method
                actionMode = startActionMode(callback2);
                return false;
            }
        });

        editTextCustomDest.setCustomSelectionActionModeCallback(callback2);
        editTextCustomDest.setOnFocusChangeListener(focusChanged);


        EditText editText = (EditText) findViewById(R.id.et_clipboard_source);
        editText.setText("text from system clipboard");

        final TextView btnBack = (TextView) findViewById(R.id.btn_clipboard_back);
        btnBack.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v.getId() == btnBack.getId())


                {
                    ClipboardActivity.this.finish();
                }
            }
        });
    }


    class StyleCallback implements ActionMode.Callback
    {
        SDKEditText edit;

        public StyleCallback(SDKEditText edit)
        {
            this.edit = edit;
            //edit.setCustomSelectionActionModeCallback(this);
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            int start = edit.getSelectionStart();
            int end = edit.getSelectionEnd();
            Log.i(TAG, String.format("onCreateActionMode start:%d, end:%d", start, end));
            menu.removeGroup(0);
            if (start >= 0 && end > start)
            {
                menu.add(1, 1, 0, "Cut");
                menu.add(1, 2, 1, "Copy");
            }

            if (SDKClipboard.getInstance().hasText())
            {
                menu.add(1, 3, 2, "Paste");
            }

            return menu.hasVisibleItems();
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu)
        {
            return false;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            Log.d(TAG,
                    String.format("onActionItemClicked item=%s/%d",
                            item.toString(), item.getItemId()));

            switch (item.getItemId())
            {
                case 1:
                    onTextCut();
                    return true;
                case 2:
                    onTextCopy();
                    return true;
                case 3:
                    onTextPaste();
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode mode)
        {
            actionMode = null;
        }

        /**
         * Text was cut from this EditText.
         */
        public void onTextCut()
        {
            //Toast.makeText(context, "Cut!", Toast.LENGTH_SHORT).show();
            Editable editable = edit.getText();
            //找到最后一个手写字,并删除最后一个手写字
            int start = edit.getSelectionStart();
            int end = edit.getSelectionEnd();
            Log.i(TAG, String.format("cut start:%d, end:%d", start, end));
            if (start >= 0 && end > start)
            {
                String selectedText = editable.toString().substring(start, end);
                SDKClipboard.getInstance().setText(selectedText);
                editable.delete(start, end);
            }
        }

        /**
         * Text was copied from this EditText.
         */
        public void onTextCopy()
        {
            //Toast.makeText(context, "Copy!", Toast.LENGTH_SHORT).show();
            Editable editable = edit.getText();
            //找到最后一个手写字,并删除最后一个手写字
            int start = edit.getSelectionStart();
            int end = edit.getSelectionEnd();
            Log.i(TAG, String.format("copy start:%d, end:%d", start, end));
            if (start >= 0 && end > start)
            {
                String selectedText = editable.toString().substring(start, end);
                SDKClipboard.getInstance().setText(selectedText);
            }
        }

        /**
         * Text was pasted into the EditText.
         */
        public void onTextPaste()
        {
            //Toast.makeText(context, "Paste!", Toast.LENGTH_SHORT).show();
            Editable editable = edit.getText();
            //找到最后一个手写字,并删除最后一个手写字
            int start = edit.getSelectionStart();
            int end = edit.getSelectionEnd();
            Log.i(TAG, String.format("paste start:%d, end:%d", start, end));
            CharSequence text = SDKClipboard.getInstance().getText();
            if (start >= 0 && end >= start)
            {
                editable.replace(start, end, text);
            }
        }
    }

}
