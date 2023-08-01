package com.dadadedicatedfirst.shopifyy

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import com.dadadedicatedfirst.shopifyy.utils.customedittext
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else{
            // Hide the status bar.
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
            actionBar?.hide()
        }
        val fpsubmit=findViewById<Button>(R.id.fpsubmit)
        fpsubmit.setOnClickListener{
            val email=findViewById<customedittext>(R.id.fpemail).text.toString().trim{it<=' '}
            if(TextUtils.isEmpty(email)){
                Toast.makeText(this@ForgotPassword,R.string.emptyemailfp,Toast.LENGTH_SHORT).show()
            }
            else{
                showprogressdialog()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{
                    task->
                    hideprogressdialog()
                        if(task.isSuccessful){
                            Toast.makeText(this@ForgotPassword,getString(R.string.resetemailfp),Toast.LENGTH_SHORT).show()
                        }
                    else{
                        showerrorsnackbar(task.exception!!.message.toString(),true)
                        }
                }
            }
        }
    }
}
