package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.utils.*
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : BaseActivity() {
    private lateinit var muserdetails:com.dadadedicatedfirst.shopifyy.models.User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setupactionbar()
        val logoutbutton=findViewById<custombutton>(R.id.btn_logout)
        logoutbutton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(this@SettingsActivity,LoginActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        val editprofile=findViewById<customtextview>(R.id.tv_edit).setOnClickListener {
            val intent=Intent(this@SettingsActivity,UserProfile::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,muserdetails)
            startActivity(intent)
        }
        val addressbutton=findViewById<LinearLayout>(R.id.ll_address)
        addressbutton.setOnClickListener{
            startActivity(Intent(this@SettingsActivity,AddressListActivity::class.java))
        }
    }

    private fun setupactionbar(){
        val actionbar=supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_settings_activity).setNavigationOnClickListener{
            onBackPressed()
        }
    }
    private fun getuserdetails() {

        showprogressdialog()
        FirestoreClass().getcurrentuser(this)
    }
    fun userdetailssuccess(user:com.dadadedicatedfirst.shopifyy.models.User){
        muserdetails=user
        hideprogressdialog()
        val settingsuserimage=findViewById<ImageView>(R.id.iv_user_photo)
        GlideLoader(this@SettingsActivity).loaduserimage(user.image,settingsuserimage)
        val tvname=findViewById<MSPTextViewBold>(R.id.tv_name)
        val tvgender=findViewById<customtextview>(R.id.tv_gender)
        val tvemail=findViewById<customtextview>(R.id.tv_email)
        val tvphone=findViewById<customtextview>(R.id.tv_mobile_number)
        tvname.text="${user.firstname} ${user.lastname}"
       tvgender.text=user.gender
      tvemail.text=user.email
        tvphone.text="${user.mobile}"
    }
    override fun onResume(){
        super.onResume()
        getuserdetails()
    }
}