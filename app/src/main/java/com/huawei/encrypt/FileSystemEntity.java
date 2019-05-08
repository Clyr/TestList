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


import com.huawei.svn.sdk.fsm.SvnFile;
import com.matrix.myapplication.R;

import java.io.Serializable;

public class FileSystemEntity implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String name;
    private String parentPath;

    private boolean isExpanded;

    private int level;


    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getParentPath()
    {
        return parentPath;
    }

    public void setParentPath(String fullPath)
    {
        this.parentPath = fullPath;
    }

    public boolean isDirectory()
    {
        String fullPath = getFullPath();
        SvnFile file = new SvnFile(fullPath);

        return file.isDirectory();
    }

    public String getFullPath()
    {
        if (parentPath == null)
        {
            return null;
        }

        if (parentPath.endsWith("/"))
        {
            return parentPath + name;
        }

        return parentPath + "/" + name;
    }

    public boolean isEncrptedFile()
    {
        String fullPath = getFullPath();
        return SvnFile.isEncFile(fullPath);
    }


    public boolean isExpanded()
    {
        return isExpanded;
    }

    public void setExpanded(boolean isExpanded)
    {
        this.isExpanded = isExpanded;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public int getImage()
    {
        if (this.isDirectory())
        {
            if (isExpanded())
            {
                return R.drawable.icon_folder_opened;
            }
            else
            {
                return R.drawable.icon_folder;
            }
        }


        if (!this.isEncrptedFile())
        {
            return R.drawable.icon_file_unencrypted;
        }
        else
        {
            return R.drawable.icon_file_encrypted;
        }
    }


    public int getType()
    {
        if (isDirectory())
        {
            return 0;
        }
        else if (isEncrptedFile())
        {
            return 1;
        }
        else
        {
            return 2;
        }
    }
}
