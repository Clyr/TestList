package com.matrix.myapplication.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.matrix.myapplication.Config;
import com.matrix.myapplication.R;
import com.matrix.myapplication.MainActivity;
import com.matrix.myapplication.interfaceModel.HandlerCall;
import com.matrix.myapplication.model.UpDateModel;
import com.matrix.myapplication.receiver.HuaweiPushRevicer;
import com.matrix.myapplication.view.LoadingDialog;
import com.matrix.myapplication.view.TextDialog;
import com.matrix.myapplication.view.UpdataDialog;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Sender;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.commons.io.IOUtils;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.matrix.myapplication.receiver.HuaweiPushRevicer.ACTION_TOKEN;

/**
 * Created by M S I of clyr on 2019/6/10.
 */
public class MainHelper {
    static Activity activity;
    private static Integer notid = 0;

    public static void initialize(Activity act) {
        activity = act;
    }

    public static void button1() {
        Map<String, String> mapData = new HashMap<>();
        mapData.put("act", "version");
        mapData.put("oldVersionCode", "2017111111");
        OkHttpUtils.get().url("http://124.128.225.19:8081/hims/mobile").params(mapData).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("response", e.toString());
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("response", response);
                Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void button3() {
        Map<String, String> mapData = new HashMap<>();
        mapData.put("act", "login");
        mapData.put("email", "jn01");
        mapData.put("pwd", "000000");

        OkHttpUtils.get().url("http://192.168.0.80:8080/hims/mobile").params(mapData).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.d("response", e.toString());
                Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Log.d("response", response);
                Toast.makeText(activity, response, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static void button9() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS).build();
        FormBody.Builder body = new FormBody.Builder();
//                body.add("act", "login").add("email", "jn01").add("pwd", "000000");
//                body.add("act","loadmyinfo").add("loginname","jn01");
        Map<String, String> mapData = new HashMap<>();
        mapData.put("act", "loadmyinfo");
        mapData.put("pwd1", "");
        mapData.put("loginname", "jn01");
        String murl = "";
        for (Map.Entry<String, String> en : mapData.entrySet()) {
            if (en.getKey() == null || en.getValue() == null) {
                continue;
            }
            body.add(en.getKey(), en.getValue());
            murl += en.getKey() + "=" + en.getValue() + "&";
        }
        Log.d("okhttp3url", murl);
        Request.Builder builder = new Request.Builder().url("http://172.31.0.23:8080/hims/mobile").post(body.build());
        Call call = client.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (e instanceof SocketTimeoutException) {//判断超时异常
//                    hideLoadingDialog();

                }
                if (e instanceof ConnectException) {//判断连接异常，我这里是报Failed to connect to 10.7.5.144

                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String string = response.body().toString();
                String string2 = response.body().string();
                Log.d("okhttp3", string);
                Log.d("okhttp3", string2);
                ToastUtils.showShort(string);
            }
        });
    }

    public static TextDialog mDialog;

    public static void button4() {
        mDialog = new TextDialog(activity, R.layout.textdialog,
                new int[]{R.id.cancel, R.id.query}, bgAlpha -> {
            setBackground(bgAlpha);
        });
        mDialog.show();
        mDialog
                .setOnCenterItemClickListener((dialog, view) -> {
                    switch (view.getId()) {
                        case R.id.cancel:

                            break;
                        case R.id.query:
                            dialogShow();
                            break;
                    }
                    mDialog.dismiss();
                });
    }

    @SuppressLint("ResourceType")
    public static void setBackground(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        activity.getWindow().setWindowAnimations(R.anim.anim_cancel);
        activity.getWindow().setAttributes(lp);
    }

    public static void dialogShow() {
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

    //TODO 指纹验证
    //此类基于Java加密API的一个包装类,用于防止在指纹扫描中被第三方恶意攻击
    static FingerprintManager.CryptoObject cryptoObject;
    static FingerprintManager manager;//访问指纹硬件的类
    static KeyguardManager keyManager;//管理锁屏的类
    static CancellationSignal signal = new CancellationSignal();

    @TargetApi(Build.VERSION_CODES.M)
    public static void Fingerprint() {
        //通过V4包获得对象
        manager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);
        keyManager = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
        if (isFingerprint()) {
            startListen();
        }
    }

    public static int i = 0;

    /**
     * 开始指纹识别
     *
     * @param manager
     * @param cryptoObject
     * @param signal
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static void startListen() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LoadingDialog.showLoadingBall(activity, "指纹识别中");
        FingerprintManager.AuthenticationCallback callBack = new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                LoadingDialog.cancelLoading();
                ToastUtils.showShort("操作过于频繁,请稍后再试");
            }


            @Override
            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                super.onAuthenticationHelp(helpCode, helpString);
            }


            //指纹识别成功
            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                LoadingDialog.cancelLoading();
                ToastUtils.showShort("指纹识别成功");
            }


            //指纹识别失败
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                LoadingDialog.cancelLoading();
                ToastUtils.showShort("指纹识别失败");
                i++;
                if (i == 3) {
                    ToastUtils.showShort("失败次数过多,请输入锁屏密码");
                    showLockScreenPass(keyManager);
                    i = 0;
                }
            }
        };
        manager.authenticate(cryptoObject, signal, 0, callBack, null);


    }

    //取消指纹识别
    public void stopListening() {
        if (signal != null) {
            // selfCancelled = true;
            signal.cancel();
            signal = null;
            ToastUtils.showShort("您已经取消指纹识别");
        }
    }


    /**
     * 指纹识别错误次数过多,显示手机锁屏密码
     *
     * @param keyManager
     */
    public static final int REQUST_CODE = 1;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showLockScreenPass(KeyguardManager keyManager) {
        Intent intent = keyManager.createConfirmDeviceCredentialIntent("finger", "开启锁屏密码");
        if (intent != null) {
            activity.startActivityForResult(intent, REQUST_CODE);
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isFingerprint() {
        //此方法为了保证判断是否支持支持指纹不报错
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            ToastUtils.showShort("没有指纹解锁的权限");
            return false;
        }
        //硬件设备是否支持指纹解锁功能
        if (!manager.isHardwareDetected()) {
            ToastUtils.showShort("该手机不支持指纹解锁");
            return false;
        }
        //判断是否有锁屏密码
        if (!keyManager.isKeyguardSecure()) {
            ToastUtils.showShort("请设置锁屏密码");
            return false;
        }
        //判断是否录入指纹
        if (!manager.hasEnrolledFingerprints()) {
            ToastUtils.showShort("没有录入指纹");
            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.P)
    public static void BiometricPrint() {
        BiometricPrompt mBiometricPrompt;
        CancellationSignal mCancellationSignal;
        BiometricPrompt.AuthenticationCallback mAuthenticationCallback;
        mBiometricPrompt = new BiometricPrompt.Builder(activity)
                .setTitle("指纹验证")
//                .setDescription("描述")
                .setNegativeButton("取消", activity.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToastUtils.showShort("Cancel button clicked");
                    }
                })
                .build();

        mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //handle cancel result
                ToastUtils.showShort("Canceled");
            }
        });

        mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                ToastUtils.showShort("onAuthenticationError " + errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                ToastUtils.showShort("验证成功");
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                ToastUtils.showShort("onAuthenticationFailed ");
            }
        };

        mBiometricPrompt.authenticate(mCancellationSignal, activity.getMainExecutor(), mAuthenticationCallback);


    }

    public static void alertDialogDiy1() {
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

    public static String strFormat(List<UpDateModel.Log> str) {
        String result = "";
        if (str == null) {
            return result;
        }
        for (int i = 0; i < str.size(); i++) {
            if (!TextUtils.isEmpty(str.get(i).getLogContent())) {
                result += (i + 1) + "." + str.get(i).getLogContent() + "\n";
            }
        }

        return result;
    }

    @SuppressLint("ResourceAsColor")
    public void statusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = activity.getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(R.color.blue9);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }*/


    }

//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//        if(hasFocus&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
//            decorView.setSystemUiVisibility(option);
//        }
//    }

    /**
     * 通过设置全屏，设置状态栏透明
     *
     * @param activity
     */
    public void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }
    }

    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        // 透明状态栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 透明导航栏
        activity.getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        SystemStatusManager tintManager = new SystemStatusManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.blue9);
        activity.getWindow().getDecorView().setFitsSystemWindows(true);
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static String mString = "";

    public static String getUpdateLog() {

        OkHttpUtils.get().url("http://124.128.225.19:8081/hims/AndroidUpdate/UpdateLog.txt")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e("Exception", e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                mString = response;
                Log.e("response", response);
            }
        });
        return mString;
    }

    public static boolean isVpnUsed() {
        try {
            Enumeration<NetworkInterface> niList = NetworkInterface.getNetworkInterfaces();
            if (niList != null) {
                for (NetworkInterface intf : Collections.list(niList)) {
                    if (!intf.isUp() || intf.getInterfaceAddresses().size() == 0) {
                        continue;
                    }
                    MyLog.d("isVpnUsed() NetworkInterface Name: " + intf.getName());
                    if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName())) {
                        return true; // The VPN is up
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveString() {
        List<String> list = new ArrayList<>();
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        String string = JSONObject.toJSONString(list);
        SPUtils.saveString(activity, "Json", string);
    }


    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
     * 将得到的int类型的IP转换为String类型
     *
     * @param ip
     * @return
     */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }


    /**
     * 获取外网的IP(要访问Url，要放到后台线程里处理)
     *
     * @param @return
     * @return String
     * @throws
     * @Title: GetNetIp
     * @Description:
     */
    public static String GetNetIp() {
        URL infoUrl = null;
        InputStream inStream = null;
        String ipLine = "";
        HttpURLConnection httpConnection = null;
        try {
            infoUrl = new URL("http://ip168.com/");
            URLConnection connection = infoUrl.openConnection();
            httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inStream, "utf-8"));
                StringBuilder strber = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                    strber.append(line + "\n");

                Pattern pattern = Pattern
                        .compile("((?:(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(?:25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d))))");
                Matcher matcher = pattern.matcher(strber.toString());
                if (matcher.find()) {
                    ipLine = matcher.group();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null)
                    inStream.close();
                if (httpConnection != null)
                    httpConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ipLine;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> enIp = ni.getInetAddresses();
                while (enIp.hasMoreElements()) {
                    InetAddress inet = enIp.nextElement();
                    if (!inet.isLoopbackAddress()
                            && (inet instanceof Inet4Address)) {
                        return inet.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "0";
    }

    public static void getNetIp() {
        OkHttpUtils.get().url("http://pv.sohu.com/cityjson?ie=utf-8").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                MyLog.e(e.toString());
                ToastUtils.showShort(getLocalIpAddress());
            }

            @Override
            public void onResponse(String response, int id) {
                MyLog.d(response);
                if (response != null) {
                    int start = response.indexOf("{");
                    int end = response.indexOf("}") + 1;
                    String string = response.substring(start, end);
                    MyLog.d(string);
                    Gson gson = new Gson();
                    ipsohu ipsohu = gson.fromJson(string, ipsohu.class);
                    MyLog.d(ipsohu.cip);
                    ToastUtils.showLong("外网IP：" + ipsohu.cip);
                }
                ToastUtils.showShort(getLocalIpAddress());
            }
        });
    }

    class ipsohu {
        public String cid;//370100
        public String cip;//211.137.204.197
        public String cname;//	山东省济南市
    }

    public static String[] mPermission = {
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

    public static final int NOTIFICATION_ID = 1001;

    public static void sendNotification() {
        //1、NotificationManager
        NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Intent inte = new Intent(activity, MainActivity.class);
        PendingIntent contentIntent =
                PendingIntent.getActivity(activity, 0, inte, 0);
        long[] vibrate = new long[]{0, 500, 1000, 1500};

        Notification.Builder builder = new Notification.Builder(activity);
        builder.setContentInfo("Content info")
                .setContentTitle("TestListTitle")//设置通知标题
                .setContentText("Notification ContentText")//设置通知内容
                .setLargeIcon(BitmapFactory.decodeResource(activity.getResources(), R.drawable.test))
                .setSmallIcon(R.drawable.test)//不能缺少的一个属性 通知栏小图标 默认圆头安卓很丑
                .setSubText("Subtext")
                .setTicker("滚动消息......")
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001", "TestList通知测试", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification noti = builder.build();
        noti.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID, noti);
    }


    public static void clearNotification() {
        //单利的系统服务
        NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }

    public static final int NOTIFICATION_ID3 = 1005;

    public static void sendProgressNotification() {
        final NotificationManager manager = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        final Notification.Builder builder = new Notification.Builder(activity);
        builder.setSmallIcon(R.drawable.test)
                .setContentTitle("TestList")
                .setContentText("正在下载...")
                .setProgress(1000, 10, true);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel("005", "download_channel", NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
            builder.setChannelId("005");
        }

        Notification n = builder.build();
        n.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        manager.notify(NOTIFICATION_ID3, n);
        //每隔1秒更新进度条进度
        //启动工作线程
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Notification n = builder.build();
                //manager.notify(NOTIFICATION_ID3, n);
                for (int i = 1; i <= 1000; i++) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //发通知
                    builder.setProgress(1000, i * 1, false);
                    manager.notify(NOTIFICATION_ID3, n);
                }
                //更新通知内容
                manager.cancel(NOTIFICATION_ID3);
                builder.setProgress(0, 0, false);
                builder.setContentText("下载完毕");
//                Notification n = builder.build();
                n.flags = Notification.FLAG_AUTO_CANCEL;//不能删除通知
                manager.notify(NOTIFICATION_ID3, n);
            }
        }.start();
    }

    private static UpdataDialog updataDialog;
    private static TextView tvmsg;
    private static TextView tvcode;

    public static void update(HandlerCall handlerCall) {
        //TODO 后需添加打包之后自动配置 update.json
        LoadingDialog.showLoadingBall(activity);
        String url = "https://TomcatIp/UpDate/update.json";
        Map<String, String> map = new HashMap<>();
        map.put("null", "null");
        OkHolder.get(url, map).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                handlerCall.handler();
//                LoadingDialog.cancelLoading();
                ToastUtils.showShort("获取更新失败！");
            }

            @Override
            public void onResponse(String response, int id) {
                handlerCall.handler();
                MyLog.d(response);
                UpDateModel model = new Gson().fromJson(response, UpDateModel.class);
                handlerCall.setModle(model);
//                MainActivity.setModel(model);
//                LoadingDialog.cancelLoading();

                String versionName = APKVersionUtils.getVersionName(activity);
                String versionCode = APKVersionUtils.getVersionCode(activity) + "";
                MyLog.d(versionCode + "--------" + versionName);
                try {
                    String[] split = versionName.split("\\.");

                    if (Integer.parseInt(model.getVersion_main()) > Integer.parseInt(split[0])) {
                        loadNewApk(model);
                    } else {
                        if (Integer.parseInt(model.getVersion_minor()) > Integer.parseInt(split[1])) {
                            loadNewApk(model);
                        } else {
                            if (Integer.parseInt(model.getVersion_revise()) > Integer.parseInt(split[2])) {
                                loadNewApk(model);
                            } else {

                                ToastUtils.showShort("当前已是最新版本！");


                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MyLog.d(e.toString());

                    ToastUtils.showShort("获取更新失败！");
                }

            }
        });

    }

    private static void loadNewApk(final UpDateModel model) {
        updataDialog = new UpdataDialog(activity,
                R.layout.dialog_updataversion,
                new int[]{R.id.dialog_sure});

        updataDialog.show();
        tvmsg = updataDialog.findViewById(R.id.updataversion_msg);
        tvcode = updataDialog.findViewById(R.id.updataversioncode);
        tvcode.setText(model.getVersion_main() + "." + model.getVersion_minor() + "." + model.getVersion_revise());
        tvmsg.setText(MainHelper.strFormat(model.getLog()));
        updataDialog
                .setOnCenterItemClickListener(new UpdataDialog.OnCenterItemClickListener() {
                    @Override
                    public void OnCenterItemClick(
                            UpdataDialog dialog, View view) {
                        switch (view.getId()) {
                            case R.id.dialog_sure:
                                if (ContextCompat.checkSelfPermission(activity, mPermission[1]) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(activity, new String[]{mPermission[1], mPermission[2]}, 5);

                                } else {
                                    DownloadAPK dl = new DownloadAPK(activity.getApplicationContext());
                                    dl.downloadAPK("http://192.168.32.111:8080/MyApplication/UpDate/" + model.getName(),
                                            model.getName());

                                    ToastUtils.showShort("正在后台下载...");
                                    SPUtils.saveBoolean(activity,
                                            "ISSHOWALERT", false);
                                }


                                break;
                        }
                        updataDialog.dismiss();
                    }
                });
    }

    //----- 废品代码参考区域 ----------------------------------------------------------------------------------------------------------------------
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendMsg() {
        Intent inte = new Intent(activity, MainActivity.class);
        // intent.setAction(Intent.ACTION_CALL);
        // intent.setData(Uri.parse("tel://17353461529"));
        //即将要执行的意图对象
        final PendingIntent contentIntent =
                PendingIntent.getActivity(activity, 0, inte, 0);

        //拿到系统的远程服务----AIDL
        final NotificationManager nm = (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        //建造者模式  -  链式调用
        long[] vibrate = new long[]{0, 500, 1000, 1500};
        final Notification notification = new Notification.Builder(activity)
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .build();
        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, "default")
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000);//闪光灯 呼吸灯
        //FLAG_AUTO_CANCEL-设置点击的时候取消当前通知
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.flags = Notification.FLAG_NO_CLEAR;//不能删除通知
        //显示一个通知
        //nm.notify(0, notification);
        //nm.notify(0,mBuilder.build());
//                NotificationManager manager=
//                        (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//                Notification notification =new NotificationCompat.Builder(MainActivity.this,"default")
//                        .setContentTitle("测试notification")
//                        .setContentText("This is content text")
//                        .setWhen(System.currentTimeMillis())
//                        .setSmallIcon(R.mipmap.ic_launcher_round)
//                        .build();
//                manager.notify(1,notification);

        NotificationManager manager =
                (NotificationManager) activity.getSystemService(NOTIFICATION_SERVICE);
        //ChannelId为"001",ChannelName为"my_channel"
        NotificationChannel channel = new NotificationChannel("1",
                "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        manager.createNotificationChannel(channel);

        //同时，Notification.Builder需要多设置一个
        //new Notification.Builder(MainActivity.this).setChannelId("001");
        Notification notification2 = new Notification.Builder(activity)
                .setTicker("有新的消息到来。。。")//提示内容文本
                .setAutoCancel(true)//点击通知后自动清除
                .setContentTitle("TestList")
                .setContentText("Notification")
                .setSmallIcon(R.drawable.test)
                .setContentIntent(contentIntent)//设置通知栏被点击时的操作-由PendingIntent意图来表示
                .setSound(Uri.parse("android.resource://com.matrix.myapplication/" + R.raw.fadeout))
                .setVibrate(vibrate)//振动
                .setLights(0xFF0000, 3000, 3000)//闪光灯 呼吸灯
                .setChannelId("001")
                .build();
        manager.notify(1, notification2);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0手机以上
            if (((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        String CHECK_OP_NO_THROW = "checkOpNoThrow";
        String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;

        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void huaweiPushInit() {
        /**
         * SDK连接HMS

         */

        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                MyLog.d("HMS connect end:" + rst);
            }
        });
        HuaweiPushRevicer.registerPushCallback(new HuaweiPushRevicer.IPushCallback() {
            @Override
            public void onReceive(Intent intent) {
                String token = intent.getStringExtra(ACTION_TOKEN);
                MyLog.d(token);

            }
        });
    }

    private static String appSecret = "5b041e2dc2f1c2436664e5010c0c9de9e65bff3f2c7185a796107562e5329cc9";
    private static String appId = "100853197";//用户在华为开发者联盟申请的appId和appSecret（会员中心->应用管理，点击应用名称的链接）
    private static String tokenUrl = "https://login.vmall.com/oauth2/token"; //获取认证Token的URL
    private static String apiUrl = "https://api.push.hicloud.com/pushsend.do"; //应用级消息下发API
    private static String accessToken;//下发通知消息的认证Token
    private static long tokenExpiredTime;  //accessToken的过期时间

    public static void sendHuaweiMsg(String token) throws IOException {
        refreshToken();
        sendPushMessage(token);
    }

    //获取下发通知消息的认证Token
    private static void refreshToken() throws IOException {
        String msgBody = MessageFormat.format("grant_type=client_credentials&client_secret={0}&client_id={1}",
                URLEncoder.encode(appSecret, "UTF-8"), appId);

        String response = httpPost(tokenUrl, msgBody, 5000, 5000);
        JSONObject obj = JSONObject.parseObject(response);
        accessToken = obj.getString("access_token");
        tokenExpiredTime = System.currentTimeMillis() + obj.getLong("expires_in") - 5 * 60 * 1000;
    }

    //发送Push消息
    private static void sendPushMessage(String token) throws IOException {
        if (tokenExpiredTime <= System.currentTimeMillis()) {
            refreshToken();
        }
        /*PushManager.requestToken为客户端申请token的方法，可以调用多次以防止申请token失败*/
        /*PushToken不支持手动编写，需使用客户端的onToken方法获取*/
        JSONArray deviceTokens = new JSONArray();//目标设备Token
        deviceTokens.add("ADduy1O0R2_59NihMJYzw4TYUk5cGoND4RiRKIg_ARDltGGmIRUaVZHkXEDwAGt-ODKeQZZthkac2YCz0jneJqxThPOEd3Ldp8SbyfpZFpFZ7tY47DDYCCyDDf4c9Pz97Q");
        deviceTokens.add("AFPYS0T2vSuhXV8Bouw5cf7QQtrteCTA-gRJ-9eJeZOgEqrgsbLcs8ifzOfrn5q_mRGZYX3QE4m-DLeYhAzezUdfGLZd92kW6iEW9AsfxV_pbruPMwkyHkm0nQH8IPC6_Q");
        deviceTokens.add(token);

        JSONObject body = new JSONObject();//仅通知栏消息需要设置标题和内容，透传消息key和value为用户自定义
        body.put("title", "华为 Push message title");//消息标题
        body.put("content", "华为 Push message content");//消息内容体

        JSONObject param = new JSONObject();
        param.put("appPkgName", "com.matrix.myapplication");//定义需要打开的appPkgName

        JSONObject action = new JSONObject();
        action.put("type", 3);//类型3为打开APP，其他行为请参考接口文档设置
        action.put("param", param);//消息点击动作参数

        JSONObject msg = new JSONObject();
        msg.put("type", 3);//3: 通知栏消息，异步透传消息请根据接口文档设置
        msg.put("action", action);//消息点击动作
        msg.put("body", body);//通知栏消息body内容

        JSONObject ext = new JSONObject();//扩展信息，含BI消息统计，特定展示风格，消息折叠。
        ext.put("biTag", "Trump");//设置消息标签，如果带了这个标签，会在回执中推送给CP用于检测某种类型消息的到达率和状态
        ext.put("icon", "http://pic.qiantucdn.com/58pic/12/38/18/13758PIC4GV.jpg");//自定义推送消息在通知栏的图标,value为一个公网可以访问的URL

        JSONObject hps = new JSONObject();//华为PUSH消息总结构体
        hps.put("msg", msg);
        hps.put("ext", ext);

        JSONObject payload = new JSONObject();
        payload.put("hps", hps);

        String postBody = MessageFormat.format(
                "access_token={0}&nsp_svc={1}&nsp_ts={2}&device_token_list={3}&payload={4}",
                URLEncoder.encode(accessToken, "UTF-8"),
                URLEncoder.encode("openpush.message.api.send", "UTF-8"),
                URLEncoder.encode(String.valueOf(System.currentTimeMillis() / 1000), "UTF-8"),
                URLEncoder.encode(deviceTokens.toString(), "UTF-8"),
                URLEncoder.encode(payload.toString(), "UTF-8"));

        String postUrl = apiUrl + "?nsp_ctx=" + URLEncoder.encode("{\"ver\":\"1\", \"appId\":\"" + appId + "\"}", "UTF-8");
        httpPost(postUrl, postBody, 5000, 5000);
    }

    public static String httpPost(String httpUrl, String data, int connectTimeout, int readTimeout) throws IOException {
        OutputStream outPut = null;
        HttpURLConnection urlConnection = null;
        InputStream in = null;

        try {
            URL url = new URL(httpUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            urlConnection.setConnectTimeout(connectTimeout);
            urlConnection.setReadTimeout(readTimeout);
            urlConnection.connect();

            // POST data
            outPut = urlConnection.getOutputStream();
            outPut.write(data.getBytes("UTF-8"));
            outPut.flush();

            // read response
            if (urlConnection.getResponseCode() < 400) {
                in = urlConnection.getInputStream();
            } else {
                in = urlConnection.getErrorStream();
            }

            List<String> lines = IOUtils.readLines(in, urlConnection.getContentEncoding());
            StringBuffer strBuf = new StringBuffer();
            for (String line : lines) {
                strBuf.append(line);
            }
            System.out.println(strBuf.toString());
            return strBuf.toString();
        } finally {
            IOUtils.closeQuietly(outPut);
            IOUtils.closeQuietly(in);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }


    // 小米推送
    public static void sendMessage() throws Exception {
        /*1：使用默认提示音提示
        2：使用默认震动提示
        4：使用默认led灯光提示
        -1（系统默认值）：以上三种效果都有
        0：以上三种效果都无，即静默推送*/
        Constants.useOfficial();
        Sender sender = new Sender(Config.APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "TestList 小米推送";
        String description = "推送内容 略略略 " + notid;
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(Config.MY_PACKAGE_NAME)
                .notifyType(-1)     // 使用默认提示音提示
                .notifyId(notid++)
                .build();
        new Thread(() -> {
            try {
                sender.broadcastAll(message, 3);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }).start();
        /*String regId = "001";
        Result result = sender.send(message, regId, 3);
        Log.v("Server response: ", "MessageId: " + result.getMessageId()
                + " ErrorCode: " + result.getErrorCode().toString()
                + " Reason: " + result.getReason());*/
    }

    private void sendMessageToAlias() throws Exception {
        Constants.useOfficial();
        Sender sender = new Sender(Config.APP_SECRET_KEY);
        String messagePayload = "This is a message";
        String title = "notification title";
        String description = "notification description";
        String alias = "001";    //alias非空白, 不能包含逗号, 长度小于128
        Message message = new Message.Builder()
                .title(title)
                .description(description).payload(messagePayload)
                .restrictedPackageName(Config.MY_PACKAGE_NAME)
                .notifyType(1)     // 使用默认提示音提示
                .build();
        new Thread(() -> {
            try {
                sender.sendToAlias(message, alias, 3); //根据alias, 发送消息到指定设备上
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static void getView(View view) {
    }

    public void getMsg(View view) {

    }

}
