package com.matrix.myapplication.utils;

import android.content.ClipboardManager;
import android.content.Context;

/**
 * Created by clyr on 2018/1/17 0017.
 */

public class ClipboardUtils {//粘贴板
    public static String getText(Context context){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.getText().toString();
    }
    public static void setText(Context context, CharSequence string) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(string);
    }

}
