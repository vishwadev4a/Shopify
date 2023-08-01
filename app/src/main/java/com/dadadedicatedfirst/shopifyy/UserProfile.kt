package com.dadadedicatedfirst.shopifyy

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.Manifest
import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dadadedicatedfirst.shopifyy.databinding.ActivityUserProfileBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.User
import com.dadadedicatedfirst.shopifyy.utils.*
import java.lang.Exception

class UserProfile : LoginActivity() {
    private var mselectedimagefileuri:Uri?=null
    private var muserprofileimageurl:String=""
    private var muserdetails:User=User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        val binding=ActivityUserProfileBinding.inflate(layoutInflater)
        var profileimage=findViewById<ImageView>(R.id.ivuser)
        var userdetails: User =User()
        var titleprofileactivity=findViewById<MSPTextViewBold>(R.id.titleprofile)
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            userdetails= intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        if(muserdetails.profilecompleted==0){
            titleprofileactivity.text=resources.getString(R.string.completeprofile)
            val etfn=findViewById<customedittext>(R.id.etfnup)
            etfn.isEnabled=false
            etfn.setText(userdetails.firstname)
            val etln=findViewById<customedittext>(R.id.etlnup)
            etln.isEnabled=false
            etln.setText(userdetails.lastname)
            val etemail=findViewById<customedittext>(R.id.etemailup)
            etemail.isEnabled=false
            etemail.setText(userdetails.email)
        }
        else{
            setupactionbar()
            titleprofileactivity.text=getString(R.string.editprofile)
            GlideLoader(this@UserProfile).loaduserimage(muserdetails.image,profileimage)
            val fnedittext=findViewById<customedittext>(R.id.etfnup)
            fnedittext.setText(muserdetails.firstname)
            val lnedittext=findViewById<customedittext>(R.id.etlnup)
            lnedittext.setText(muserdetails.lastname)
            val etemailtext=findViewById<customedittext>(R.id.etemailup)
            val mobileedittext=findViewById<customedittext>(R.id.etmobilenumberup)
            etemailtext.setText(muserdetails.email)
            val rbmale=findViewById<customradiobutton>(R.id.rbmale)
            val rbfemale=findViewById<customradiobutton>(R.id.rbmale)
            etemailtext.isEnabled=false
            if(muserdetails.mobile!=0L){
                mobileedittext.setText(muserdetails.mobile.toString())
            }
            if(muserdetails.gender==Constants.MALE){
                rbmale.isChecked=true
            }
            else{
                rbfemale.isChecked=true
            }
            
        }
        profileimage.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){


                Constants.showImageChooser(this@UserProfile)

            }
            else{
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_CODE
                )
            }

        }

        val userprofilesave=findViewById<Button>(R.id.submitup).setOnClickListener {
            if(validateuserdetails()){
                showprogressdialog()
                if(mselectedimagefileuri!=null){

                    FirestoreClass().uploadimagetocloudstorage(this@UserProfile,mselectedimagefileuri,Constants.USER_PROFILE_IMAGE)
                }
                else{
                    updateuserprofile()
                }
            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
            Constants.showImageChooser(this@UserProfile)
        }
        else{
            Toast.makeText(this,getString(R.string.storagepermissiondenied),Toast.LENGTH_SHORT).show()
        }
        
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
                if(data!=null){
                    try{
                         mselectedimagefileuri= data.data
                        val userimg=findViewById<ImageView>(R.id.ivuser)
                        GlideLoader(this).loaduserimage(mselectedimagefileuri!!,userimg)
                    }
                    catch(e:Exception){
                        e.printStackTrace()
                        Toast.makeText(this@UserProfile,"Image selection failed",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
    private fun validateuserdetails():Boolean{
        val mobileno=findViewById<customedittext>(R.id.etmobilenumberup)
        return when  {
            TextUtils.isEmpty(mobileno.text.toString().trim{it<=' '})->{
                showerrorsnackbar(getString(R.string.emptymobileno),true)
                false
            }
            else->{
                true
            }
        }
    }
    private fun updateuserprofile(){
        val userhashmap=HashMap<String,Any>()
        val mobilenumber=findViewById<customedittext>(R.id.etmobilenumberup).text.toString().trim{it<=' '}
        val gendermale=findViewById<RadioButton>(R.id.rbmale)
        val genderfemale=findViewById<RadioButton>(R.id.rbfemale)
        var gender:String=""
        val firstname=findViewById<customedittext>(R.id.etfnup).text.toString().trim{it<=' '}
        if(firstname!=muserdetails.firstname){
            userhashmap[Constants.FIRSTNAME]=firstname
        }
        val lastname=findViewById<customedittext>(R.id.etlnup).text.toString().trim{it<=' '}
        if(lastname!=muserdetails.lastname){
            userhashmap[Constants.LASTINAME]=lastname
        }
        val mobileno=findViewById<customedittext>(R.id.etmobilenumberup).text.toString().trim{it<=' '}
        if(mobileno.isNotEmpty()&&mobileno!=muserdetails.mobile.toString()){
            userhashmap[Constants.MOBILE]=mobileno
        }
        if(gendermale.isChecked){
            gender=Constants.MALE
        }
        else{
            gender=Constants.FEMALE
        }
        if(gender.isNotEmpty()&&gender!=muserdetails.gender){
            userhashmap[Constants.GENDER]=gender
        }

        if(mobilenumber.isNotEmpty()) {
            userhashmap[Constants.MOBILE] = mobilenumber.toLong()
        }
        userhashmap[Constants.GENDER]=gender
        if (muserprofileimageurl.isNotEmpty()){
            userhashmap[Constants.IMAGE]=muserprofileimageurl
        }
        if(muserdetails.profilecompleted==0) {
            userhashmap[Constants.COMPLELTE_PROFILE] = 1
        }
        FirestoreClass().updateuserprofiledata(this@UserProfile,userhashmap,)
    }
    fun userprofileupdatesuccess() {
        hideprogressdialog()
        Toast.makeText(
            this@UserProfile,
            "Profile Update Success",
            Toast.LENGTH_SHORT
        ).show()


        // TODO Step 8: Redirect it to the DashboardActivity instead of MainActivity.
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfile, DashboardActivity::class.java))
        finish()

    }
    fun imageuploadsuccess(imageURL: String) {

        // TODO Step 10: Remove the hide progress dialog code
        // START
        // Hide the progress dialog
        /*hideProgressDialog()*/
        // END

        // TODO Step 2: Remove the Toast message and assign the value to the global variable.
        // START
        muserprofileimageurl= imageURL
        // END

        // TODO Step 9: Call the user update details function.
        // START
        updateuserprofile()
        // END
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
}