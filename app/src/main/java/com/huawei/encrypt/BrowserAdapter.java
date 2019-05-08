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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import com.huawei.FileUtil;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.fsm.SvnFile;
import com.matrix.myapplication.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class BrowserAdapter extends BaseAdapter
{

    private String TAG = BrowserAdapter.class.getSimpleName();

    private String path = FileUtil.getSDPath() + "/" + Constants.FOLDER_ROOT;
    private List<FileSystemEntity> mergedBranches = new ArrayList<FileSystemEntity>();

    private Map<FileSystemEntity, List<FileSystemEntity>> unmergedBranches = new HashMap<FileSystemEntity, List<FileSystemEntity>>();

    private FileSystemEntity rootDirectory;

    Comparator<FileSystemEntity> sortComparator = new Comparator<FileSystemEntity>()
    {

        @Override
        public int compare(FileSystemEntity lhs, FileSystemEntity rhs)
        {
            if (lhs == null && rhs == null)
            {
                return 0;
            }

            if (lhs == null)
            {
                return -1;
            }

            if (rhs == null)
            {
                return 1;
            }

            if (lhs.getLevel() != rhs.getLevel())
            {
                return lhs.getLevel() - rhs.getLevel();
            }
            else if (lhs.getType() != rhs.getType())
            {
                return lhs.getType() - rhs.getType();
            }
            else
            {
                return lhs.getName().compareTo(rhs.getName());
            }

        }
    };

    public BrowserAdapter()
    {

        setRoot();
    }

    @Override
    public int getCount()
    {
        return mergedBranches.size();
    }

    /**
     * return a File Object
     */
    @Override
    public Object getItem(int position)
    {
        if (position >= mergedBranches.size()) return null;
        return mergedBranches.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.common_file_item, null);
        }

        FileSystemEntity fi = mergedBranches.get(position);

        LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.layout_file_item_indent);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins((fi.getLevel() + 1) * 40, 5, 5, 5);
        layout.setLayoutParams(params);


        ImageView iv = (ImageView) convertView.findViewById(R.id.iv_file_item_fileicon);

        iv.setImageResource(fi.getImage());

        TextView tv = (TextView) convertView.findViewById(R.id.tv_file_item_filename);
        tv.setText(fi.getName());

        return convertView;
    }

    private void setRoot()
    {
        SvnFile file = new SvnFile(path);
        if (!file.isDirectory())
        {
            Log.e(TAG, "path is not a directory:" + path);
        }
        mergedBranches.clear();

        List<String> fileNames = this.listDirectory(path);

        List<FileSystemEntity> filesAtRootLevel = new ArrayList<FileSystemEntity>();
        for (String fileName : fileNames)
        {
            FileSystemEntity aFile = new FileSystemEntity();
            aFile.setName(fileName);
            aFile.setParentPath(file.getPath());
            aFile.setLevel(0);
            filesAtRootLevel.add(aFile);
        }


        String parentPath = file.getParent();
        rootDirectory = new FileSystemEntity();
        rootDirectory.setName(file.getName());
        rootDirectory.setParentPath(parentPath);
        rootDirectory.setLevel(-1);
        rootDirectory.setExpanded(true);

        unmergedBranches.put(rootDirectory, filesAtRootLevel);


        sortBranches();
        mergeBranches();

        notifyDataSetChanged();

    }

    private List<String> listDirectory(String path)
    {
        List<String> result = new ArrayList<String>();
        SvnFile file = new SvnFile(path);
        if (file.isDirectory())
        {

            String[] files = file.list();

            result.addAll(Arrays.asList(files));
        }
        return result;
    }

    private void mergeBranches()
    {

        mergedBranches.addAll(unmergedBranches.get(rootDirectory));
        Set<FileSystemEntity> branchKeys = unmergedBranches.keySet();
        List<FileSystemEntity> branchKeysList = new ArrayList<FileSystemEntity>();
        branchKeysList.addAll(branchKeys);
        Collections.sort(branchKeysList, sortComparator);

        for (FileSystemEntity file : branchKeysList)
        {
            if (file == rootDirectory)
            {
                continue;
            }

            List<FileSystemEntity> valueEntities = unmergedBranches.get(file);

            int index = mergedBranches.indexOf(file);

            mergedBranches.addAll(index + 1, valueEntities);
        }

    }

    private void sortBranches()
    {
        for (List<FileSystemEntity> entries : unmergedBranches.values())
        {
            Collections.sort(entries, sortComparator);
        }

    }

    public void collapseBranchAtIndex(int position)
    {
        Log.i(TAG, "collapseBranchAtIndex:" + position);
        FileSystemEntity file = mergedBranches.get(position);
        if (file != null && file.isDirectory())
        {
            List<FileSystemEntity> children = unmergedBranches.get(file);
            for (int i = 0; i < children.size(); i++)
            {
                FileSystemEntity child = children.get(i);
                if (child.isDirectory() && child.isExpanded())
                {
                    collapseBranchAtIndex(position + i + 1);
                }
            }

            for (int i = children.size() - 1; i >= 0; i--)
            {
                mergedBranches.remove(position + i + 1);

                Log.i(TAG, "mergedBranches.remove:" + (position + i + 1));
            }

            unmergedBranches.remove(file);

        }

    }

    public void expandBranchAtIndex(int position)
    {
        Log.i(TAG, "expandBranchAtIndex:" + position);
        FileSystemEntity directoryFile = mergedBranches.get(position);
        if (directoryFile.isDirectory())
        {
            List<String> names = listDirectory(directoryFile.getFullPath());
            List<FileSystemEntity> newBranch = new ArrayList<FileSystemEntity>();
            for (String fileName : names)
            {
                FileSystemEntity aFile = new FileSystemEntity();
                aFile.setName(fileName);
                aFile.setParentPath(directoryFile.getFullPath());
                aFile.setLevel(directoryFile.getLevel() + 1);
                newBranch.add(aFile);
            }

            Collections.sort(newBranch, sortComparator);
            unmergedBranches.put(directoryFile, newBranch);

            mergedBranches.addAll(position + 1, newBranch);
        }

    }

}  