package com.matrix.myapplication.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.matrix.myapplication.BuildConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by M S I of clyr on 2018/9/20.
 */

public final class MyLog {
    private static final String TAG = "------MyLog------";
    //开关
    private final static boolean flag1 = true;//true 测试   false  上线
    private final static boolean flag = BuildConfig.DEBUG;//true 测试   false  上线

    public static void v(String tag, String msg) {
        if (flag) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (flag) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (flag) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (flag) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (flag) {
            Log.e(tag, msg);
        }
    }

    //------下面的是单参数(当你迷茫的时候懒得写的时候)------//
    public static void v(String msg) {
        v(TAG, msg);
    }

    public static void d(String msg) {
        d(TAG, msg);
    }

    public static void i(String msg) {
        i(TAG, msg);
    }

    public static void w(String msg) {
        w(TAG, msg);
    }

    public static void e(String msg) {
        e(TAG, msg);
    }


    //因为String的length是字符数量不是字节数量所以为了防止中文字符过多，
    //  把4*1024的MAX字节打印长度改为2001字符数
    public static void loge(String tag, String str) {
        int max_str_length = 2001 - tag.length();
        //大于4000时
        while (str.length() > max_str_length) {
            Log.e(tag, str.substring(0, max_str_length));
            str = str.substring(max_str_length);
        }
        //剩余部分
        Log.e(tag, str);
    }

    public static void loge(String str) {
        int max_str_length = 2001 - TAG.length();
        //大于4000时
        while (str.length() > max_str_length) {
            Log.e(TAG, str.substring(0, max_str_length));
            str = str.substring(max_str_length);
        }
        //剩余部分
        Log.e(TAG, str);
    }

    /**
     * 截断输出日志
     *
     * @param msg
     */
    public void loges(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(tag, "-------------------" + logContent);
            }
            Log.e(tag, "-------------------" + msg);// 打印剩余日志
        }
    }

    /**
     * 日志保存
     *
     * @param bfile    文件的byte流
     * @param filePath 文件要保存的路径
     * @param fileName 文件保存的名字
     */
    /*try {
        MyLog.saveFile(token.getBytes(),
                Environment.getExternalStorageDirectory().getPath(),
                "hwToken.txt");
    } catch (Exception e) {
        e.printStackTrace();
    }*/
    public static void saveFile(byte[] bfile, String filePath, String fileName) {
        if (flag) {
            BufferedOutputStream bos = null;
            FileOutputStream fos = null;

            File file = null;
            try {
                filePath = filePath == null ? Environment.getExternalStorageDirectory().getPath() : filePath;
                //通过创建对应路径的下是否有相应的文件夹。
                File dir = new File(filePath);
                if (!dir.exists()) {// 判断文件目录是否存在
                    //如果文件存在则删除已存在的文件夹。
                    dir.mkdirs();
                }

                //如果文件存在则删除文件
                file = new File(filePath, fileName);
                if (file.exists()) {
                    file.delete();
                }
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                //把需要保存的文件保存到SD卡中
                bos.write(bfile);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param context 上下文
     * @param str     内容分
     */
    public static void ShareQQ(Context context, String str) {
        try {
            Intent qqIntent = new Intent(Intent.ACTION_SEND);
            qqIntent.setPackage("com.tencent.mobileqq");
            qqIntent.setType("text/plain");
            qqIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            qqIntent.putExtra(Intent.EXTRA_TEXT, str);
            context.startActivity(qqIntent);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof ActivityNotFoundException)
                ToastUtils.showShort( "请先安装QQ再进行尝试");
            else
                ToastUtils.showShort( "分享失败");
        }
    }

    /*Gson gson = new Gson();

        MyLog.loge("DssLog",gson.toJson(channelInfos));
        try {
            MyLog.saveFile(gson.toJson(channelInfos).getBytes(),
                    Environment.getExternalStorageDirectory().getPath(),
                    "dss.txt");
        } catch (Exception e) {
            e.printStackTrace();
            toast("baocunshibai");
        }*/
}
