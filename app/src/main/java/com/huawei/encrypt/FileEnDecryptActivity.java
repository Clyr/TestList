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

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.FileUtil;
import com.huawei.StringUtil;
import com.huawei.ThemeUtil;
import com.huawei.anyoffice.sdk.doc.OpenDocOption;
import com.huawei.anyoffice.sdk.doc.SecReader;
import com.huawei.anyoffice.sdk.exception.NoRMSAppFoundException;
import com.huawei.anyoffice.sdk.exception.NoRecommendedAppException;
import com.huawei.common.BaseActivity;
import com.huawei.common.Constants;
import com.huawei.svn.sdk.fsm.SvnFile;
import com.huawei.svn.sdk.fsm.SvnFileInputStream;
import com.huawei.svn.sdk.fsm.SvnFileOutputStream;
import com.huawei.svn.sdk.fsm.thirdpart.zip.SvnZipEntry;
import com.huawei.svn.sdk.fsm.thirdpart.zip.SvnZipFile;
import com.matrix.myapplication.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author cWX223941
 */
public class FileEnDecryptActivity extends BaseActivity
{
    private static final String TAG = "EnDecryptActivity";

    private static int BUFFER_SIZE = 4096;
    private EditText etFilePath, etOriginalPath, etEncryptPath;
    private TextView btnBrowse, btnBack, btnEncrypt, btnDecrypt, btnGuide, btnOpen, openZipResult;
    private TextView resultText;
    private final int FILE_SYSTEM_REQUEST_CODE = 1;
    private FileSystemEntity fileInfo;


    // private String enDecryptType;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_endecrypt);
        init();
    }

    private void init()
    {
        initView();
        initData();
    }

    private void initView()
    {
        etFilePath = (EditText) findViewById(R.id.et_encrypt_decrypt_filepath);
        etOriginalPath = (EditText) findViewById(R.id.et_encrypt_decrypt_originalpath);
        etEncryptPath = (EditText) findViewById(R.id.et_encrypt_decrypt_encryptpath);
        btnBrowse = (TextView) findViewById(R.id.btn_encrypt_decrypt_browse);
        btnBack = (TextView) findViewById(R.id.btn_endecrypt_back);
        btnEncrypt = (TextView) findViewById(R.id.btn_encrypt_decrypt_encrypt);
        btnDecrypt = (TextView) findViewById(R.id.btn_encrypt_decrypt_decrypt);
        btnGuide = (TextView) findViewById(R.id.btn_endecrypt_guide);
        resultText = (TextView) findViewById(R.id.tv_encrypt_decrypt_result);
        btnOpen = (TextView) findViewById(R.id.btn_encrypt_decrypt_open);
        openZipResult = (TextView) findViewById(R.id.open_zip_result);
        btnEncrypt.setEnabled(false);
        btnDecrypt.setEnabled(false);
        btnOpen.setEnabled(false);
        btnBrowse.setOnClickListener(onClickListener);
        btnBack.setOnClickListener(onClickListener);
        btnEncrypt.setOnClickListener(onClickListener);
        btnDecrypt.setOnClickListener(onClickListener);
        btnOpen.setOnClickListener(onClickListener);
        btnGuide.setOnClickListener(onClickListener);
        etFilePath.addTextChangedListener(textWatcher);
    }

    private void initData()
    {
        etOriginalPath.setText(Constants.FILE_PATH_ORIGINAL);
        etEncryptPath.setText(Constants.FILE_PATH_ENCRYPT);
        File originPath = new File(Constants.FILE_PATH_ORIGINAL);
        originPath.mkdirs();
        File encryptPath = new File(Constants.FILE_PATH_ENCRYPT);
        encryptPath.mkdirs();
    }

    private OnClickListener onClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v.getId() == btnBrowse.getId())
            {
                Intent intent = new Intent(FileEnDecryptActivity.this, FileBrowserActivity.class);
                FileEnDecryptActivity.this.startActivityForResult(intent, FILE_SYSTEM_REQUEST_CODE);
            }
            else if (v.getId() == btnBack.getId())
            {
                FileEnDecryptActivity.this.finish();
            }
            else if (v.getId() == btnEncrypt.getId())
            {
                viewWhenClickEncrypt();
                encrypt();
            }
            else if (v.getId() == btnDecrypt.getId())
            {
                // 1:pager prepare
                viewWhenClickDecrypt();
                decrypt();

            }
            else if (v.getId() == btnOpen.getId())
            {

                Log.i(TAG, "open file." + fileInfo.getFullPath());
                // open the file
                // openOriginFile(fileInfo.getFullPath());
                if (fileInfo.getFullPath().endsWith(".zip"))
                {
                    readZipFile(fileInfo.getFullPath());
                }
                else
                {
                    openFile(fileInfo.getFullPath());
                }
            }
        }
    };

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
                ThemeUtil.setBtnToEnable(btnEncrypt, FileEnDecryptActivity.this);
                ThemeUtil.setBtnToEnable(btnDecrypt, FileEnDecryptActivity.this);

                ThemeUtil.setBtnToEnable(btnOpen, FileEnDecryptActivity.this);

            }
            else
            {
                // set login button to be not enable
                ThemeUtil.setBtnToUnable(btnEncrypt, FileEnDecryptActivity.this);
                ThemeUtil.setBtnToUnable(btnDecrypt, FileEnDecryptActivity.this);
                ThemeUtil.setBtnToUnable(btnOpen, FileEnDecryptActivity.this);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (FILE_SYSTEM_REQUEST_CODE == requestCode)
        {
            if (RESULT_OK == resultCode)
            {
                fileInfo = (FileSystemEntity) data.getSerializableExtra(Constants.FILE_BROWSE_RESULT);
                initPageByFileInfo();
            }
        }
    }

    /**
     * if all edit text have content,then return true,otherwise return false
     *
     * @return true:all have content;false:otherwise
     */
    private boolean checkEditContent()
    {
        if (!StringUtil.isEmpty(etFilePath.getText().toString()))
        {
            return true;
        }
        return false;
    }

    private void viewWhenClickEncrypt()
    {
        btnEncrypt.setText(R.string.ing);
        btnEncrypt.setEnabled(false);
    }

    private void viewWhenClickDecrypt()
    {
        btnDecrypt.setText(R.string.ing);
        btnDecrypt.setEnabled(false);
    }

    private void handleEncryptResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "encrpty success.");
                btnEncrypt.setText(R.string.encrypt);
                btnEncrypt.setEnabled(true);
                resultText.setText(R.string.encrypt_success);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "encrpty failed.");
                btnEncrypt.setText(R.string.encrypt);
                btnEncrypt.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void handleDecryptResult(int status)
    {
        switch (status)
        {
            case Constants.STATUS_REQUEST_SUCCESS:
                Log.e(TAG, "encrpty success.");
                btnDecrypt.setText(R.string.decrypt);
                btnDecrypt.setEnabled(true);
                resultText.setText(R.string.decrypt_success);
                break;
            case Constants.STATUS_REQUEST_FALSE:
                Log.e(TAG, "encrpty failed.");
                btnDecrypt.setText(R.string.decrypt);
                btnDecrypt.setEnabled(true);
                break;
            default:
                break;
        }
    }

    private void initPageByFileInfo()
    {
        if (null == fileInfo)
        {
            btnEncrypt.setEnabled(false);
            btnDecrypt.setEnabled(false);
            return;
        }
        if (fileInfo.isEncrptedFile())
        {
            // the file is encrypt
            etFilePath.setText(fileInfo.getFullPath());
            btnEncrypt.setVisibility(View.GONE);
            btnDecrypt.setText(R.string.decrypt);
            btnDecrypt.setVisibility(View.VISIBLE);
        }
        else
        {
            // the file is original
            etFilePath.setText(fileInfo.getFullPath());
            btnEncrypt.setText(R.string.encrypt);
            btnEncrypt.setVisibility(View.VISIBLE);
            btnDecrypt.setVisibility(View.GONE);
        }
        resultText.setText("");
    }

    // 文档浏览
    private void openFile(String filePath)
    {
        if (filePath == null)
        {
            return;
        }

        int pos = filePath.indexOf('.');

        if (pos <= 0)
        {
            return;
        }

        // String pathExt = filePath.substring(pos + 1);
        //
        // String avFileType = "mp3|wav|wma|avi|mp4|mpg|wmv|3gp|m4a|aac";

        // if (pathExt.toLowerCase().matches(avFileType)) {
        // SvnMediaPlayer.getInstance().play(this, filePath);
        // return;
        // }

        SecReader reader = new SecReader();

//		reader.setRecommendedApp("com.kingsoft.moffice_pro_hw", SecReader.SDK_MIMETYPE_DOCUMENT);

        boolean ret = false;
        try
        {
            // 打开文件
            // ret = reader.openDocWithSDK(this, filePath, getPackageName(),
            // null);

            OpenDocOption option = new OpenDocOption();
            option.setContext(this);
            option.setFilePath(filePath);
            option.setPackageName(getPackageName());
            option.setOpenMode("r");
            ret = reader.openDocWithSDK(option);
        }
        catch (NoRMSAppFoundException e)
        {
            e.printStackTrace();
        }
        catch (NoRecommendedAppException e)
        {
            e.printStackTrace();
        }
        Log.e(TAG, "open file path:" + filePath + " res:" + ret);
    }

    private boolean isOffice(String withEnd)
    {
        boolean result = false;
        if (withEnd.endsWith(".doc") || withEnd.endsWith(".docx"))
        {
            result = true;
        }
        return result;
    }

    private void readZipFile(final String file)
    {

        openZipResult.setText("");

        if (file == null || !file.endsWith(".zip"))
        {
            return;
        }

        String destDirectoryPath = file.substring(0, file.length() - 4);

        final File destDirectory = new File(destDirectoryPath);
        if (destDirectory.exists())
        {
            destDirectory.delete();
        }

        destDirectory.mkdir();
        //解压缩

        AsyncTask<Object, Integer, String> zipDecompressTask = new AsyncTask<Object, Integer, String>()
        {
            @Override
            protected String doInBackground(Object... paramVarArgs)
            {
                boolean result = true;

                String zipResult = "";

                try
                {
                    decompress(destDirectory, new ZipInputStream(new SvnFileInputStream(file)));

                }
                catch (FileNotFoundException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();

                    result = false;
                }
                catch (Exception e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    result = false;
                }

                if (!result)
                {
                    return zipResult;
                }

                // 使用SVNZipFile 遍历
                try
                {
                    SvnZipFile svnZipFile = new SvnZipFile(file);


                    @SuppressWarnings("unchecked")
                    Enumeration<SvnZipEntry> enumeration = (Enumeration<SvnZipEntry>) svnZipFile.entries();
                    while (enumeration.hasMoreElements())
                    {
                        SvnZipEntry zipEntry = (SvnZipEntry) enumeration.nextElement();
                        zipResult += zipEntry.getName() + "\n";

                    }

                    svnZipFile.close();

                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    result = false;
                }
                return zipResult;
            }

            @Override
            protected void onPostExecute(String result)
            {
                openZipResult.setText(result);
            }
        };
        zipDecompressTask.execute(new Object());


    }


    /**
     * 文件 解压缩
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws Exception
     */
    private static void decompress(File destFile, ZipInputStream zis)
            throws Exception
    {

        ZipEntry entry = null;
        while ((entry = zis.getNextEntry()) != null)
        {

            System.out.println("entry:" + entry.getName());
            // 文件  
            String dir = destFile.getPath() + File.separator + entry.getName();

            File dirFile = new File(dir);

            // 文件检查  
            fileProber(dirFile);

            if (entry.isDirectory())
            {
                dirFile.mkdirs();
            }
            else
            {
                decompressFile(dirFile, zis);
            }

            zis.closeEntry();
        }
    }

    /**
     * 文件探针
     * <p/>
     * <p/>
     * 当父目录不存在时，创建目录！
     *
     * @param dirFile
     */
    private static void fileProber(File dirFile)
    {

        File parentFile = dirFile.getParentFile();
        if (!parentFile.exists())
        {

            // 递归寻找上级目录  
            fileProber(parentFile);

            parentFile.mkdir();
        }

    }

    /**
     * 文件解压缩
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws Exception
     */
    private static void decompressFile(File destFile, ZipInputStream zis)
            throws Exception
    {

        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(destFile));

        int count;
        byte data[] = new byte[BUFFER_SIZE];
        while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1)
        {
            bos.write(data, 0, count);
        }

        bos.close();
    }


    private void openOriginFile(String url) throws IOException
    {
        // Create URI
        File file = new File(url);
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url
        // with extensions.
        // When the if condition is matched, plugin sets the correct intent
        // (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx"))
        {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        }
        else if (url.toString().contains(".pdf"))
        {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        }
        else if (url.toString().contains(".ppt") || url.toString().contains(".pptx"))
        {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        }
        else if (url.toString().contains(".xls") || url.toString().contains(".xlsx"))
        {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        }
        else if (url.toString().contains(".zip") || url.toString().contains(".rar"))
        {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        }
        else if (url.toString().contains(".rtf"))
        {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        }
        else if (url.toString().contains(".wav") || url.toString().contains(".mp3"))
        {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        }
        else if (url.toString().contains(".gif"))
        {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        }
        else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png"))
        {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        }
        else if (url.toString().contains(".txt"))
        {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        }
        else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg")
                || url.toString().contains(".mpe") || url.toString().contains(".mp4")
                || url.toString().contains(".avi"))
        {
            // Video files
            intent.setDataAndType(uri, "video/*");
        }
        else
        {
            // if you want you can also define the intent type for any other
            // file

            // additionally use else clause below, to manage other unknown
            // extensions
            // in this case, Android will show all applications installed on the
            // device
            // so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    // 文件加密
    private void encrypt()
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        // ////////////////////////////加密////////////////////////////////////////////////
        SvnFileOutputStream fileOutStream = null;
        FileInputStream fileInStream = null;
        try
        {
            File originFile = new File(etFilePath.getText().toString().trim());
            if (originFile.exists() && originFile.isFile())
            {
                // 原始文件
                fileInStream = new FileInputStream(originFile);


                SvnFile dir = new SvnFile(Constants.FILE_PATH_ENCRYPT + "/" + originFile.getName());
                if (!dir.exists())
                {
                    try
                    {
                        // 在指定的文件夹中创建文件
                        dir.createNewFile();
                    }
                    catch (Exception e)
                    {
                    }
                }


                // 加密文件
                fileOutStream = new SvnFileOutputStream(Constants.FILE_PATH_ENCRYPT + "/" + originFile.getName());

                long startTime = 0;
                long endTime = 0;
                startTime = System.currentTimeMillis();
                // 加密写入，使用和OutputStream一致
                FileUtil.streamCopy(fileInStream, fileOutStream);

                endTime = System.currentTimeMillis();

                Log.i(TAG, "encrypt success!");
                Log.i(TAG, "fileSize:" + originFile.length());
                Log.i(TAG, "startTime:" + startTime + ", endTime:" + endTime + ", time used:" + (endTime - startTime));


                status = Constants.STATUS_REQUEST_SUCCESS;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "encrypt error:" + e.getMessage());
        }
        handleEncryptResult(status);
    }

    // 文件解密
    private void decrypt()
    {
        int status = Constants.STATUS_REQUEST_FALSE;
        // ///////////////////////解密///////////////////////////////////////////
        SvnFileInputStream fileInStream = null;
        FileOutputStream fileOutStream = null;
        try
        {
            SvnFile originFile = new SvnFile(etFilePath.getText().toString().trim());
            if (originFile.exists() && originFile.isFile() && SvnFile.isEncFile(originFile.getPath()))
            {
                // 加密文件
                fileInStream = new SvnFileInputStream(originFile);

                // 解密文件
                fileOutStream = new FileOutputStream(
                        new File(Constants.FILE_PATH_ORIGINAL + "/" + originFile.getName()));

                // 解密读，使用和InputStream一致
                FileUtil.write(fileInStream, fileOutStream);
                Log.i(TAG, "decipher success!");
                status = Constants.STATUS_REQUEST_SUCCESS;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG, "decipher error:" + e.getMessage());
        }
        handleDecryptResult(status);
    }
}
