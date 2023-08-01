package com.dadadedicatedfirst.shopifyy

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dadadedicatedfirst.shopifyy.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val loggedinusername=getSharedPreferences(Constants.SHOPIFY_PREFERENCES, MODE_PRIVATE)
        val username=loggedinusername.getString(Constants.LOGGED_IN_USERNAME,"")
        val loggedinuser=findViewById<TextView>(R.id.tvloggedinuser)
        loggedinuser.text="Hello ${username}"
    }
}