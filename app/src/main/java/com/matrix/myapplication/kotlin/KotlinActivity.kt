package com.matrix.myapplication.kotlin

import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.matrix.myapplication.R
import com.matrix.myapplication.utils.ToastUtils
import kotlinx.android.synthetic.main.activity_kotlin.*

class KotlinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)
        init();
    }

    fun init() {
//        val textView = findViewById<TextView>(R.id.text)
        /*text.setOnClickListener({
            text.setText("WTK")
        })*/
        btnAlert.setOnClickListener({
            text.setText("WTK")
            ToastUtils.showLong("btnAlert");
        })
        toast.setOnClickListener({
            Toast.makeText(this,"toast",Toast.LENGTH_SHORT).show();
        })
    }

    fun onClickshortcutsAdd(v: View) {
        var shortcutManager = getSystemService(ShortcutManager::class.java) as ShortcutManager
        var intent = Intent(this, this::class.java)
        intent.action = Intent.ACTION_VIEW
        var shortcut = ShortcutInfo.Builder(this, "noti_channel_demo")
                .setIcon(Icon.createWithResource(this, R.drawable.ic_launcher))
                .setShortLabel("通知渠道")
                .setLongLabel("通知渠道演示")
                .setIntent(intent)
                .build()
        shortcutManager.addDynamicShortcuts(listOf(shortcut))
    }
}
