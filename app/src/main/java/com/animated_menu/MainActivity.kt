package com.animated_menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.lib.animated_menu.AnimatedMenu

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.open).setOnClickListener { findViewById<AnimatedMenu>(R.id.menu).switchMenu() }
    }
}