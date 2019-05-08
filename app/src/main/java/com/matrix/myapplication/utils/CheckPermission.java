package com.matrix.myapplication.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by clyr on 2017/12/27 0027.
 * 获取权限
 */

public class CheckPermission {
    private static String[] mPermission = {
            android.Manifest.permission.CAMERA,//相机
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//存储写
            android.Manifest.permission.READ_EXTERNAL_STORAGE,//存储读
            android.Manifest.permission.RECORD_AUDIO,//录音
            android.Manifest.permission.ACCESS_FINE_LOCATION//GPS

    };

    public static void getPermission(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, mPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[0]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[1], mPermission[2]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[3]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[3]}, 5);
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[4]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{mPermission[4]}, 5);
        }
    }

    public static boolean getPerOk(Activity activity) {
        boolean isGet = true;
        if (ContextCompat.checkSelfPermission(activity, mPermission[0]) != PackageManager.PERMISSION_GRANTED) {
            isGet = false;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[1]) != PackageManager.PERMISSION_GRANTED) {
            isGet = false;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[3]) != PackageManager.PERMISSION_GRANTED) {
            isGet = false;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[4]) != PackageManager.PERMISSION_GRANTED) {
            isGet = false;
        }
        return isGet;
    }
    /** 判断权限是否被拒绝*/
    public static int perDenied(Activity activity, int num) {
        if(num>mPermission.length||num<0){
            //SDToast.showToast("标记设置错误");
            return -1;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[num]) == PackageManager.PERMISSION_DENIED)
          return 0;
        return 1;
    }
    public static int perGranted(Activity activity, int num) {
        if(num>mPermission.length||num<0){
            //SDToast.showToast("标记设置错误");
            return -1;
        }
        if (ContextCompat.checkSelfPermission(activity, mPermission[num]) == PackageManager.PERMISSION_GRANTED)
            return 0;
        return 1;
    }
}
