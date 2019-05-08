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
package com.huawei;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * @author cWX223941
 */
public class BaseUtil
{

    private static String TAG = "BaseUtil";
    /**
     * show toast
     *
     * @param resId
     * @param context
     */
    public static void showToast(int resId, Context context)
    {
        if(context == null)
        {
            Log.e(TAG, "showToast with a null context");
            return;
        }
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * show toast
     *
     * @param resId
     * @param context
     */
    public static void showToast(final String res, final Activity context)
    {
        if(context == null)
        {
            Log.e(TAG, "showToast with a null context");
            return;
        }

        if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
            Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
        } else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * if params is not null,then add the params into url for get request
     *
     * @param params
     * @param urlStr
     */
    public static URL getUrl(List<BasicNameValuePair> params, String urlStr)
            throws Exception
    {
        URL url = null;
        if (params != null)
        {
            String urlInfo = StringUtil.getGetUrl(params, urlStr);
            url = new URL(urlInfo);
        }
        else
        {
            url = new URL(urlStr);
        }
        return url;
    }

    /**
     * get content type from file
     *
     * @param file
     */
    public static String getContentType(File file)
    {
        String contentType = "";
        if (null != file)
        {
            String fileName = file.getName();
            fileName = fileName.toLowerCase(Locale.US);
            if (fileName.endsWith(".jpg"))
                contentType = "image/jpg";
            else if (fileName.endsWith(".png"))
                contentType = "image/png";
            else if (fileName.endsWith(".jpeg"))
                contentType = "image/jpeg";
            else if (fileName.endsWith(".gif"))
                contentType = "image/gif";
            else if (fileName.endsWith(".bmp"))
                contentType = "image/bmp";
            else
                contentType = "text/plain";
        }
        return contentType;
    }

    /**
     * parse to integer
     *
     * @param str
     */
    public static int getIntegerFromString(String str)
    {
        int num = -1;
        if (!StringUtil.isEmpty(str))
        {
            try
            {
                num = Integer.valueOf(str);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return num;
    }

    /**
     * get string from input stream
     *
     * @param inputStream
     */
    public static String getString(InputStream inputStream)
    {
        InputStreamReader inputStreamReader = null;
        try
        {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        }
        catch (UnsupportedEncodingException e1)
        {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
                sb.append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getContentFromAssetFile(String assetFilePath,
                                                 Context context)
    {
        String content = "";
        if (!StringUtil.isEmpty(assetFilePath))
        {
            try
            {
                content = getString(context.getAssets().open(assetFilePath));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return content;
    }

    private final static byte[] hex = "0123456789ABCDEF".getBytes();

    public static String Bytes2HexString(byte[] b)
    {
        byte[] buff = new byte[2 * b.length];
        for (int i = 0; i < b.length; i++)
        {
            buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
            buff[2 * i + 1] = hex[b[i] & 0x0f];
        }
        return new String(buff);
    }
}
