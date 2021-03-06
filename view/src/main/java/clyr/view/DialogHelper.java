package clyr.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import clyr.view.loadingdialog.LoadingDialog;
import clyr.view.textdialog.Setback;
import clyr.view.textdialog.TextDialog;

/**
 * Created by M S I of clyr on 2019/11/19.
 */
public class DialogHelper {
    public static void alertDialogDiy(final Context activity) {
        AlertDialog.Builder ab = new AlertDialog.Builder(activity);
        ab.setMessage("message-第一个弹窗样式")
                .setTitle("title")
                .setPositiveButton("第二个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //使用message可以显示全部字符串
                        AlertDialog.Builder ab1 = new AlertDialog.Builder(activity);
                        ab1.setMessage("message-这是第二个弹窗样式加长（Please install the Android Support Repository from the Android SDK Manager.\n" +
                                "<a href=\"openAndroidSdkManager\">Open Android SDK Manager</a>）\n")
                                .setPositiveButton("确定", null)
                                .setNegativeButton("取消", null)
                                .show();
                    }
                })
                .setNegativeButton("第三个", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //使用title字体略大但是只能显示部分信息大约char=31
                        AlertDialog.Builder ab1 = new AlertDialog.Builder(activity);
                        ab1.setTitle("message-这是第三个弹窗样式加长（Please install the Android Support Repository from the Android SDK Manager.\n" +
                                "<a href=\"openAndroidSdkManager\">Open Android SDK Manager</a>）\n")
                                .setPositiveButton("确定", null)
                                .setNegativeButton("取消", null)
                                .show();
                    }
                })
                .show();
    }


    public static void textDialog(final Activity activity) {
        final TextDialog mDialog;
        mDialog = new TextDialog(activity, R.layout.textdialog,
                new int[]{R.id.cancel, R.id.query}, new Setback() {
            @Override
            public void backgroundAlpha(float bgAlpha) {
                setBackground(bgAlpha, activity);
            }
        });
        mDialog.show();
        mDialog
                .setOnCenterItemClickListener(new TextDialog.OnCenterItemClickListener() {
                    @Override
                    public void OnCenterItemClick(TextDialog dialog, View view) {
                        int id = view.getId();
                        if (id == R.id.cancel) {

                        } else if (id == R.id.query) {
                            dialogShow(activity);
                        }
                        mDialog.dismiss();
                    }
                });
    }

    @SuppressLint("ResourceType")
    public static void setBackground(float bgAlpha, Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setWindowAnimations(R.anim.anim_cancel);
        activity.getWindow().setAttributes(lp);
    }

    public static void dialogShow(Activity activity) {
        final View view = (LinearLayout) activity.getLayoutInflater().inflate(
                R.layout.dialog_edittext, null);
        EditText editText = (EditText) view.findViewById(R.id.edittext);
        editText.setText("1111111111111");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("这是第二个")
                .setMessage("这是第二个")
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }
    Handler mHandler = new Handler();
    Runnable task = new Runnable() {
        @Override
        public void run() {
            LoadingDialog.cancelLoading();
        }
    };
    public void loadingDialog(Activity activity){
        LoadingDialog.showLoadingBall(activity,R.style.ProgressDialogTransparent);
        mHandler.postDelayed(task, 3000);
    }
}
