package com.dadadedicatedfirst.shopifyy

import android.app.Dialog
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private lateinit var mprogressdialog:Dialog
    private var doublebacktoexitpressedonce=false
    fun showerrorsnackbar(message:String,errormessage:Boolean){
        val snackbar= Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackbarview=snackbar.view
        if(errormessage){
            snackbarview.setBackgroundColor(

                ContextCompat.getColor(this@BaseActivity,R.color.colorsnackbarerror)
            )
        }
        else{
            snackbarview.setBackgroundColor(

                ContextCompat.getColor(this@BaseActivity,R.color.snackbarsuccess)
            )
        }
        snackbar.show()
    }
    fun showprogressdialog(){
        mprogressdialog=Dialog(this)
        mprogressdialog.setContentView(R.layout.dialog_progress)
        mprogressdialog.setCancelable(false)
        mprogressdialog.setCanceledOnTouchOutside(false)
        mprogressdialog.show()
    }
    fun hideprogressdialog(){
        mprogressdialog.dismiss()
    }
    fun doublebacktoexit(){

        if(doublebacktoexitpressedonce){
            super.onBackPressed()
            return
        }
        this.doublebacktoexitpressedonce=true
        Toast.makeText(this,resources.getString(R.string.please_click_back_again_to_exit),Toast.LENGTH_SHORT).show()
        Handler().postDelayed({doublebacktoexitpressedonce=false},2000)
    }

}