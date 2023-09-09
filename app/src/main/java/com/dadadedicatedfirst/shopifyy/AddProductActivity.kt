package com.dadadedicatedfirst.shopifyy

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dadadedicatedfirst.shopifyy.databinding.ActivityAddProductBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import java.lang.Math.abs
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.Exception

class AddProductActivity : BaseActivity(), View.OnClickListener {
    private var mselectedimagefileuri: Uri?=null
    private var mproductimageurl:String=""
    private lateinit var bindingg:ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingg=ActivityAddProductBinding.inflate(layoutInflater)
        val view:View=bindingg.getRoot()
        setContentView(view)
        supportActionBar?.hide()
        setupactionbar()
        bindingg!!.btnSubmit.setOnClickListener{
            if(validateproductdetails(bindingg)){
                uploadproductimage()
            }
        }
        val imageselector=findViewById<ImageView>(R.id.iv_add_update_product)
        imageselector.setOnClickListener{
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this@AddProductActivity)
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),Constants.READ_STORAGE_CODE)
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
            Constants.showImageChooser(this@AddProductActivity)
        }
        else{
            Toast.makeText(this,getString(R.string.storagepermissiondenied), Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==Constants.PICK_IMAGE_REQUEST_CODE){
                if(data!=null){
                    val addupdateproduct=findViewById<ImageView>(R.id.iv_add_update_product)
                    addupdateproduct.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_vector_edit))
                     mselectedimagefileuri=data.data!!
                    val ivproductimage=findViewById<ImageView>(R.id.iv_product_image)

                    try{
                        GlideLoader(this).loaduserimage(mselectedimagefileuri!!,ivproductimage)
                    }
                    catch(e:Exception){
                        e.printStackTrace()
                    }
                }
            }

        }
    }
    private fun setupactionbar(){
        val actionbar=supportActionBar
        if(actionbar==null){
            actionbar?.setDisplayHomeAsUpEnabled(true)
            actionbar?.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbabr_add_product).setNavigationOnClickListener{
            onBackPressed()
        }
    }

    private fun validateproductdetails(bindingg: ActivityAddProductBinding):Boolean{
        return when{
            mselectedimagefileuri==null->{
                showerrorsnackbar(getString(R.string.emptyProductImage),true)
                false
            }
            TextUtils.isEmpty(bindingg.etProductTitle.text.toString().trim{it<=' '})->{
                showerrorsnackbar(getString(R.string.emptyproducttitle),true)
                false
            }
            TextUtils.isEmpty(bindingg.etProductDescription.text.toString().trim{it<=' '})->{
                showerrorsnackbar(getString(R.string.productdescriptionempty),true)
                false
            }
            TextUtils.isEmpty(bindingg.etProductQuantity.text.toString().trim{it<=' '})->{
                showerrorsnackbar(getString(R.string.emptyproductquantity),true)
                false
            }
            else->{
                true
            }
        }
    }
private fun uploadproductimage(){
    showprogressdialog()
    FirestoreClass().uploadimagetocloudstorage(
        this@AddProductActivity,mselectedimagefileuri,Constants.PRODUCT_IMAGE
    )
}
    fun imageuploadsuccess(imageurl:String){
        mproductimageurl=imageurl
        uploadproductdetails()

    }
    private fun uploadproductdetails(){
        val username=this.getSharedPreferences(Constants.SHOPIFY_PREFERENCES,Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME,"")!!
        val product= Product(
            FirestoreClass().getuserid(),
            username,
            bindingg.etProductTitle.text.toString().trim{it<= ' '},
            bindingg.etProductPrice.text.toString().trim{it<=' '},
            bindingg.etProductDescription.text.toString().trim{it<=' '},
            bindingg.etProductQuantity.text.toString().trim{it<= ' '},
            mproductimageurl,
            abs(Random(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).nextLong()).toString()+"Product"
        )
        FirestoreClass().uploadproductdetails(this@AddProductActivity,product)

    }
    fun productuploadsuccess(){
        hideprogressdialog()
        Toast.makeText(this@AddProductActivity,"Product uploaded successfully",Toast.LENGTH_SHORT).show()
    }
    override fun onClick(v: View?) {

    }
}