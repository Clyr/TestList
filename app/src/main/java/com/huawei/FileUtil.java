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

import android.os.Environment;
import android.util.Log;

import com.huawei.svn.sdk.fsm.SvnFileInputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * @author cWX223941
 */
public class FileUtil
{
    private static final String TAG = "FileUtil";

    private static final int BUFFER_SIZE = 4096;

    //	public static String getFolderNameFromPath(String folderPath){
    //	    String folderName = "";
    //	    if(!StringUtil.isEmpty(folderPath)){
    //	        File folderFile = new File(folderPath);
    //	        if(folderFile.isDirectory()){
    //	            folderName = folderPath.substring(folderPath.lastIndexOf("/") + 1, folderPath.length());
    //	            if(StringUtil.isEmpty(folderName)){
    //	                folderName = folderPath.substring(folderPath.lastIndexOf("\\") + 1, folderPath.length());
    //	            }
    //	        }
    //	    }
    //	    return folderName;
    //	}

    /**
     * check the filePath effectiveness
     *
     * @param filePath
     * @return true:exist;false:otherwise
     */
    public static boolean isFileExist(String filePath)
    {
        boolean isExist = false;
        if (!StringUtil.isEmpty(filePath))
        {
            File file = new File(filePath);
            isExist = file.exists();
        }
        return isExist;
    }

    /**
     * create file
     *
     * @param filePath
     * @return true:create success;false:otherwise
     */
    public static boolean createFile(String filePath)
    {
        boolean isSuccess = false;
        if (!StringUtil.isEmpty(filePath))
        {
            File file = new File(filePath);
            isSuccess = file.mkdirs();
        }
        return isSuccess;
    }

    public static boolean createFolder(String folderPath)
    {
        boolean isSuccess = false;
        if (!StringUtil.isEmpty(folderPath))
        {
            File file = new File(folderPath);
            if (!file.exists())
            {
                file.mkdirs();
            }
        }
        return isSuccess;
    }

    /**
     * delete all file or folder under the folderPath
     *
     * @param folderPath
     * @param deleteFolder true:should delete folder;false:otherwise
     * @return boolean true:delete success;false:otherwise
     */
    public static boolean deleteChild(String folderPath, boolean deleteFolder)
    {
        boolean deleteSuccess = false;
        if (!StringUtil.isEmpty(folderPath))
        {
            File folderFile = new File(folderPath);
            deleteSuccess = deleteChild(folderFile, deleteFolder);
        }
        return deleteSuccess;
    }

    /**
     * delete all file or folder under the folderPath
     *
     * @param folderFile
     * @param deleteFolder true:should delete folder;false:otherwise
     * @return boolean true:delete success;false:otherwise
     */
    public static boolean deleteChild(File folderFile, boolean deleteFolder)
    {
        boolean deleteSuccess = false;
        if (null != folderFile)
        {
            if (!folderFile.isDirectory())
            {
                return deleteSuccess;
            }
            File[] childFile = folderFile.listFiles();
            if (null == childFile || childFile.length == 0)
            {
                deleteSuccess = true;
                return deleteSuccess;
            }
            for (File file : childFile)
            {
                if (file.isDirectory())
                {
                    deleteSuccess = deleteChild(file, deleteFolder);
                    //judge deleteFile is true which means that should delete folder,
                    //and deleteSuccess is true which means that delete success for child.
                    if (deleteFolder && deleteSuccess)
                    {
                        deleteSuccess = file.delete();
                    }
                }
                else
                {
                    deleteSuccess = file.delete();
                }
                if (!deleteSuccess)
                {
                    break;
                }
            }
        }
        return deleteSuccess;
    }

    /**
     * read info from InputStream,return as string
     *
     * @param in
     */
    public static String read(InputStream in) throws Exception
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);
        try
        {
            for (String line = r.readLine(); line != null; line = r.readLine())
            {
                sb.append(line);
            }
            in.close();
        }
        catch (Exception e)
        {
            r.close();
            throw new Exception("read InputStream error.");
        }
        return sb.toString();
    }

    /**
     * write file into OutputStream
     *
     * @param os
     * @param file
     */
    public static void write(OutputStream os, File file) throws Exception
    {

        byte[] buf = new byte[BUFFER_SIZE];


        if (null != os && null != file)
        {
            FileInputStream fis = null;
            try
            {
                fis = new FileInputStream(file);
                int rn2;

                while ((rn2 = fis.read(buf, 0, BUFFER_SIZE)) > 0)
                {
                    os.write(buf, 0, rn2);
                }
                fis.close();
            }
            catch (Exception e)
            {
                if (fis != null)
                {
                    fis.close();
                }
                e.printStackTrace();
                throw new Exception("write file error.");
            }
        }
    }
//
//    /**
//     * read from input stream,write into output stream
//     * @param fileInStream InputStream
//     * @param fileOutStream SvnFileOutputStream
//     * */
//    public static void write(InputStream fileInStream,
//            SvnFileOutputStream fileOutStream) throws Exception
//    {
//        if (null != fileInStream && null != fileOutStream)
//        {
//            try
//            {
//                byte[] tmpBuf = new byte[BUFFER_SIZE];
//                int tmpLen = 0;
//                while ((tmpLen = fileInStream.read(tmpBuf)) > 0)
//                {
//                    fileOutStream.write(tmpBuf, 0, tmpLen);
//                }
//                fileOutStream.flush();
//                fileOutStream.close();
//                fileOutStream = null;
//                fileInStream.close();
//                fileInStream = null;
//            }
//            catch (Exception e)
//            {
//                if (null != fileInStream)
//                {
//                    fileInStream.close();
//                    fileInStream = null;
//                }
//                if (null != fileOutStream)
//                {
//                    fileOutStream.flush();
//                    fileOutStream.close();
//                    fileOutStream = null;
//                }
//                throw new Exception("error in write into output stream.");
//            }
//        }
//    }

    /**
     * read from input stream,write into output stream
     *
     * @param fileInStream  InputStream
     * @param fileOutStream SvnFileOutputStream
     */
    public static void streamCopy(InputStream fileInStream,
                                  FileOutputStream fileOutStream) throws Exception
    {
        if (null != fileInStream && null != fileOutStream)
        {
            try
            {
                byte[] tmpBuf = new byte[BUFFER_SIZE];
                int tmpLen = 0;
                while ((tmpLen = fileInStream.read(tmpBuf)) > 0)
                {
                    fileOutStream.write(tmpBuf, 0, tmpLen);
                }
                fileOutStream.flush();
                fileOutStream.close();
                fileOutStream = null;
                fileInStream.close();
                fileInStream = null;
            }
            catch (Exception e)
            {
                if (null != fileInStream)
                {
                    fileInStream.close();
                    fileInStream = null;
                }
                if (null != fileOutStream)
                {
                    fileOutStream.flush();
                    fileOutStream.close();
                    fileOutStream = null;
                }
                throw new Exception("error in write into output stream.");
            }
        }
    }

    /**
     * read from input stream,write into output stream
     *
     * @param fileInStream
     * @param fileOutStream
     */
    public static void write(SvnFileInputStream fileInStream,
                             FileOutputStream fileOutStream) throws Exception
    {
        if (null != fileInStream && null != fileOutStream)
        {
            try
            {
                byte[] tmp = new byte[BUFFER_SIZE];
                int k = 0;
                while ((k = fileInStream.read(tmp)) > -1)
                {
                    fileOutStream.write(tmp, 0, k);
                }
                fileOutStream.flush();
                fileOutStream.close();
                fileOutStream = null;
                fileInStream.close();
                fileInStream = null;
            }
            catch (Exception e)
            {
                if (null != fileInStream)
                {
                    fileInStream.close();
                    fileInStream = null;
                }
                if (null != fileOutStream)
                {
                    fileOutStream.flush();
                    fileOutStream.close();
                    fileOutStream = null;
                }
                throw new Exception("error in write into output stream.");
            }
        }
    }

    public static String getSDPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();
            return sdDir.toString();
        }
        return "";
    }

    /**
     * get file's size,can not be Directory
     *
     * @param filePath
     */
    public static long getFileSize(String filePath)
    {
        long size = 0;
        if (!StringUtil.isEmpty(filePath))
        {
            File file = new File(filePath);
            Log.d("data", "file path:" + file.getPath());
            if (file.exists() && file.isFile())
            {
                size = file.length();
            }
        }
        return size;
    }

    /**
     * get folder size,include all child under it
     *
     * @param folderPath String
     */
    public static long getFolderSize(String folderPath)
    {
        long size = 0;
        if (!StringUtil.isEmpty(folderPath))
        {
            File folderFile = new File(folderPath);
            size = getFolderSize(folderFile);
        }
        return size;
    }

    /**
     * get folder size,include all child under it
     *
     * @param folderFile File
     */
    public static long getFolderSize(File folderFile)
    {
        long size = 0;
        if (null != folderFile)
        {
            if (folderFile.isDirectory())
            {
                File[] childFile = folderFile.listFiles();
                if (null == childFile || childFile.length == 0)
                {
                    return size;
                }
                for (File file : childFile)
                {
                    if (file.isDirectory())
                    {
                        //recurrence to get all children's sum
                        size = size + getFolderSize(file);
                    }
                    else
                    {
                        size = size + file.length();
                    }
                }
            }
            else
            {
                size = folderFile.length();
            }
        }
        return size;
    }

    /**
     * get file percent which had been download
     *
     * @param filePath
     * @param url
     */
    public static int getPercentOfFileDownload(String filePath, String url)
    {
        int percent = 0;
        if (!StringUtil.isEmpty(filePath) && !StringUtil.isEmpty(url))
        {
            try
            {
                long localSize = getFileSize(filePath);
                HttpClient client = new DefaultHttpClient();
                Log.d(TAG, "URL" + url);
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                //get the web file size
                long webSize = response.getEntity().getContentLength();
                Log.d(TAG, "localSize:" + localSize + ";webSize:" + webSize);
                percent = (int) (((double) localSize / webSize) * 100);
            }
            catch (Exception e)
            {
                Log.e(TAG, "getPercentOfFileDownload error:" + e);
            }
        }
        return percent;
    }

    /**
     * just fileName can be CH,folderPath must be EN
     *
     * @param url
     */
    public static String getUTF8Url(String url) throws Exception
    {
        String utf8Url = "";
        if (!StringUtil.isEmpty(url))
        {
            String fileName = StringUtil.getFileNameFromFilePath(url);
            String folderPath = StringUtil.getFolderPathFromFilePath(url);
            utf8Url = folderPath + "/" + URLEncoder.encode(fileName, "UTF-8");
        }
        return utf8Url;
    }


    /**
     * re name file to new file
     *
     * @param oldFilePath
     * @param newFilePath
     */
    public static void changeFileName(String oldFilePath, String newFilePath)
    {
        if (!StringUtil.isEmpty(oldFilePath)
                && !StringUtil.isEmpty(newFilePath))
        {
            File oldFile = new File(oldFilePath);
            if (oldFile.exists())
            {
                oldFile.renameTo(new File(newFilePath));
            }
        }
    }

    /**
     * judge whether the file is video
     *
     * @param filePath
     */
    public static boolean isVideoFile(String filePath)
    {
        boolean isVideo = false;
        if (StringUtil.isEmpty(filePath))
        {
            return isVideo;
        }
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1,
                filePath.length());
        if (suffix.equals("mp4") || suffix.equals("MP4")
                || suffix.equals("3gp") || suffix.equals("3GP"))
        {
            isVideo = true;
        }
        return isVideo;
    }
}
