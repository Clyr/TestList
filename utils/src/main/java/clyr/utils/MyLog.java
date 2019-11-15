package clyr.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

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
                TDUtils.showShort(context, "请先安装QQ再进行尝试");
            else
                TDUtils.showShort(context, "分享失败");
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
