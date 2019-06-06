package com.matrix.myapplication.baidumap.demo.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * 演示MapView的基本用法
 */
public class BaseMapDemo extends Activity {

    @SuppressWarnings("unused")
    private static final String LTAG = BaseMapDemo.class.getSimpleName();

    private MapView mMapView;

    FrameLayout layout;

    private boolean mEnableCustomStyle = true;

    private static final int OPEN_ID = 0;
    private static final int CLOSE_ID = 1;

    //用于设置个性化地图的样式文件
    private static final String CUSTOM_FILE_NAME = "custom_map_config.json";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * MapView (TextureMapView)的
         * {@link MapView.setCustomMapStylePath(String customMapStylePath)}
         * 方法一定要在MapView(TextureMapView)创建之前调用。
         * 如果是setContentView方法通过布局加载MapView(TextureMapView), 那么一定要放置在
         * MapView.setCustomMapStylePath方法之后执行，否则个性化地图不会显示
         */
        setMapCustomFile(this, CUSTOM_FILE_NAME);

        mMapView = new MapView(this, new BaiduMapOptions());
        initView(this);
        setContentView(layout);

        MapStatus.Builder builder = new MapStatus.Builder();
        LatLng center = new LatLng(39.998152, 116.276973); // 颐和园
        float zoom = 16.5f; // 地图缩放级别
        builder.target(center).zoom(zoom);
        mMapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        MapView.setMapCustomEnable(true);
    }

    // 初始化View
    private void initView(Context context) {
        layout = new FrameLayout(this);
        layout.addView(mMapView);

        RadioGroup group = new RadioGroup(context);
        group.setBackgroundColor(Color.BLACK);
        // 个性化开关水平排列
        group.setOrientation(LinearLayout.HORIZONTAL);
        group.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

        final RadioButton openBtn = new RadioButton(context);
        openBtn.setText("开启个性化地图");
        openBtn.setId(OPEN_ID);
        openBtn.setTextColor(Color.WHITE);
        group.addView(openBtn, params);

        final RadioButton closeBtn = new RadioButton(context);
        closeBtn.setText("关闭个性化地图");
        closeBtn.setTextColor(Color.WHITE);
        closeBtn.setId(CLOSE_ID);
        group.addView(closeBtn, params);

        if (mEnableCustomStyle) {
            openBtn.setChecked(true);
        } else {
            closeBtn.setChecked(true);
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == OPEN_ID) {
                    MapView.setMapCustomEnable(true);
                } else if (checkedId == CLOSE_ID) {
                    MapView.setMapCustomEnable(false);
                }
            }
        });

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.addView(group, layoutParams);
    }

    // 设置个性化地图config文件路径
    private void setMapCustomFile(Context context, String PATH) {
        FileOutputStream out = null;
        InputStream inputStream = null;
        String moduleName = null;

        try {
            inputStream = context.getAssets().open("customConfigdir/" + PATH);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);

            moduleName = context.getFilesDir().getAbsolutePath();
            File f = new File(moduleName + "/" + PATH);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();

            out = new FileOutputStream(f);
            out.write(b);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MapView.setCustomMapStylePath(moduleName + "/" + PATH);

    }

    /**
     * 设置个性化icon
     *
     * @param context
     * @param icon_themeId
     */
    private void setIconCustom(Context context, int icon_themeId){

       MapView.setIconCustom(icon_themeId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // activity 暂停时同时暂停地图控件
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // activity 恢复时同时恢复地图控件
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 销毁地图前线关闭个性化地图，否则会出现资源无法释放
        MapView.setMapCustomEnable(false);
        // activity 销毁时同时销毁地图控件
        mMapView.onDestroy();
    }

}
