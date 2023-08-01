package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.User
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.customtextview
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

open class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else{
            // Hide the status bar.
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
            actionBar?.hide()
        }
        setContentView(R.layout.activity_login)
        val loginbutton=findViewById<Button>(R.id.btn_login)
        loginbutton.setOnClickListener{
            signin()
        }
        val regbutton=findViewById<TextView>(R.id.tv_register)
        regbutton.setOnClickListener{
            val intent: Intent =Intent(this@LoginActivity,RegisterActivty::class.java)
            startActivity(intent)

        }
        val fpbutton=findViewById<customtextview>(R.id.tv_forgot_password)
        fpbutton.setOnClickListener{
            val intent: Intent =Intent(this@LoginActivity,ForgotPassword::class.java)
            startActivity(intent)
        }

    }
    private fun signin(){

        if(logdetails()){
            showprogressdialog()
            val email=findViewById<EditText>(R.id.usr_email).text.toString().trim{it<=' '}
            val password=findViewById<EditText>(R.id.usr_password).text.toString().trim{it<=' '}
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{
                    task->
                    if(task.isSuccessful){
                        FirestoreClass().getcurrentuser(this@LoginActivity)
                    }
                    else{
                        hideprogressdialog()
                        showerrorsnackbar(task.exception!!.message.toString(),true)
                    }
                }

            )
        }
    }
    private fun logdetails():Boolean{
        val email=findViewById<EditText>(R.id.usr_email)
        val password=findViewById<EditText>(R.id.usr_password)
        return when{
            TextUtils.isEmpty(email.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.emptyemail),true)
                false
            }
            TextUtils.isEmpty(password.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.emptypassword),true)
                false
            }
            else->{
                true
            }
        }
    }
    fun userloggedinsuccess(user:User){
        hideprogressdialog()
        if(user.profilecompleted==0){
            val intent=Intent(this@LoginActivity,UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            val intent=Intent(this@LoginActivity,DashboardActivity::class.java)
            startActivity(intent)

        }
        finish()
    }
}