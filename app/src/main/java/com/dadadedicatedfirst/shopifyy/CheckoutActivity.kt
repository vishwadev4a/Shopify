package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dadadedicatedfirst.shopifyy.adapters.CartItemsListAdapter
import com.dadadedicatedfirst.shopifyy.databinding.ActivityCheckoutBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.myshoppal.models.Cart
import com.myshoppal.models.Order

class CheckoutActivity : BaseActivity() {
    private var maddressdetails: com.dadadedicatedfirst.shopifyy.models.Address? =null
    private lateinit var mproductlist:ArrayList<Product>
    private lateinit  var mcartitemslist:ArrayList<Cart>
    private var subtotal:Double=0.0
    private var totalamount:Double=0.0
    private lateinit var morderdetails:Order
    private lateinit var binding:ActivityCheckoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else {
            // Hide the status bar.
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
            actionBar?.hide()
        }

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
             maddressdetails = intent.getParcelableExtra<com.dadadedicatedfirst.shopifyy.models.Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }
        if(maddressdetails!=null){
            binding.tvCheckoutAddressType.text= maddressdetails!!.type
            binding.tvCheckoutFullName.text=maddressdetails!!.name
            binding.tvCheckoutAddress.text="${maddressdetails!!.address},${maddressdetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text=maddressdetails!!.additionalNote
            if(maddressdetails?.otherDetails!!.isNotEmpty()){
                binding.tvCheckoutOtherDetails.text=maddressdetails!!.otherDetails
            }
            binding.tvMobileNumber.text=maddressdetails!!.mobileNumber
        }
        getproductlist()

        binding.btnPlaceOrder.setOnClickListener{
            placeorder()
        }
    }

    private fun getproductlist(){
        FirestoreClass().getallproductslist(this@CheckoutActivity)

    }
    fun successproductlistfromfirestore(productlist:ArrayList<Product>){
        mproductlist=productlist
        getcartitemslist()

    }
    private fun getcartitemslist(){
        FirestoreClass().cartlistget(this@CheckoutActivity)
    }

    fun successcartitemslist(cartlist:ArrayList<Cart>){


        for(product in mproductlist){
            for(cart in cartlist){
                if(product.product_id==cart.product_id){
                    cart.stock_quantity=product.stock_quantity
                }
            }
        }
        mcartitemslist=cartlist
        binding.rvCartListItems.layoutManager=LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)
        val cartlistadapter=CartItemsListAdapter(this@CheckoutActivity, mcartitemslist!!,false)
        binding.rvCartListItems.adapter=cartlistadapter

        for(item in mcartitemslist!!){
            val availablequantity=item.stock_quantity.toInt()
            if(availablequantity>0){
                val price=item.price.toDouble()
                val quantity=item.cart_quantity.toInt()
                subtotal+=(price*quantity)
            }
        }

        binding.tvCheckoutSubTotal.text="₹${subtotal}"
        binding.tvCheckoutShippingCharge.text="₹50"
        if(subtotal>0){
            binding.llCheckoutPlaceOrder.visibility=View.VISIBLE
            totalamount=subtotal+50
            binding.tvCheckoutTotalAmount.text="${totalamount}"
        }

        else{
            binding.llCheckoutPlaceOrder.visibility=View.GONE
        }


    }

    private fun placeorder() {
        showprogressdialog()

        if (maddressdetails != null) {
             morderdetails = Order(

                FirestoreClass().getuserid(),
                mcartitemslist!!,
                maddressdetails!!,
                "My order ${System.currentTimeMillis()}",
                mcartitemslist!![0].image,
                subtotal.toString(),
                "50",
                System.currentTimeMillis(),
                totalamount.toString(),
            )

            FirestoreClass().placeorder(this@CheckoutActivity,morderdetails)
        }
    }

    fun orderplacedsuccess(){
        FirestoreClass().updatealldetails(this, mcartitemslist!!,morderdetails)

    }

    fun alldetailsupdatedsuccessfully(){
        hideprogressdialog()
        Toast.makeText(this@CheckoutActivity,"Your order placed successfully",Toast.LENGTH_SHORT).show()
        val intent=Intent(this@CheckoutActivity,DashboardActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()


    }



}