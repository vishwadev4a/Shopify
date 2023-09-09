package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dadadedicatedfirst.shopifyy.databinding.ActivityProductDetailsBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import com.myshoppal.models.Cart

class ProductDetailsActivity : BaseActivity() {
    private var mproductid:String=""
    private lateinit var mproductdetails:Product
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            Toast.makeText(this,mproductid,Toast.LENGTH_SHORT).show()
            mproductid=intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productownerid:String=""
        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productownerid=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
            Log.e("productdebug","${productownerid}")
            if(FirestoreClass().getuserid()==productownerid){
                binding.btnAddToCart.visibility=View.GONE
                binding.btnGoToCart.visibility=View.GONE
            }
            else{
                binding.btnAddToCart.visibility=View.VISIBLE
            }
        }
        getproductdetails()
        binding.btnAddToCart.setOnClickListener{
            addtocart()
        }
        binding.btnGoToCart.setOnClickListener{
            startActivity(Intent(this@ProductDetailsActivity,CartListActivity::class.java))
        }
    }
    fun productexistsincart(){
        hideprogressdialog()
        binding.btnAddToCart.visibility=View.GONE
        binding.btnGoToCart.visibility=View.VISIBLE
    }
    private fun addtocart(){
        val addtocart= Cart(
            FirestoreClass().getuserid(),
            mproductid,
            mproductdetails.title,
            mproductdetails.price,
            mproductdetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        FirestoreClass().addcartitems(this,addtocart)
    }
    fun addtocartsuccess(){
        hideprogressdialog()
        Toast.makeText(this@ProductDetailsActivity,getString(R.string.itemaddedtocart),Toast.LENGTH_SHORT).show()
        binding.btnAddToCart.visibility=View.GONE
        binding.btnGoToCart.visibility=View.VISIBLE
    }
    private fun getproductdetails(){
        showprogressdialog()
        FirestoreClass().getproductdetails(this, mproductid)
    }
    fun productdetailsuccess(product: Product){
        mproductdetails=product
       GlideLoader(
           this@ProductDetailsActivity,
       ).loadproductpicture(
           product.image,
           binding.ivProductDetailImage
       )
        binding.tvProductDetailsTitle.text=product.title
        binding.tvProductDetailsPrice.text="₹${product.price}"
        binding.tvProductDetailsDescription.text=product.description
        binding.tvProductDetailsAvailableQuantity.text=product.stock_quantity

        if(product.stock_quantity.toInt()==0){
            hideprogressdialog()
            binding.btnAddToCart.visibility=View.GONE
            binding.tvProductDetailsAvailableQuantity.text=resources.getString(R.string.lbl_out_of_stock)
            binding.tvProductDetailsAvailableQuantity.setTextColor(ContextCompat.getColor(this@ProductDetailsActivity,R.color.colorsnackbarerror))
        }
        else{
            if(FirestoreClass().getuserid()==product.user_id){
                hideprogressdialog()
            }
            else{
                FirestoreClass().checkifitemexistincart(this@ProductDetailsActivity,mproductid)
            }

        }


    }
}