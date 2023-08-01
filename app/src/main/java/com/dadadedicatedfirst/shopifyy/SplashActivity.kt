package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        WindowManager.LayoutParams.FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            val intent= Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        },2500)
    }
}