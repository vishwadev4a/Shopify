package com.dadadedicatedfirst.shopifyy

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.adapters.CartItemsListAdapter
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Cartitem
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.customtextview

class CartListActivity : BaseActivity() {
    private lateinit var mproductlist:ArrayList<Product>
    private lateinit var mcartlistitems:ArrayList<Cartitem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_cart_list)
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
        val button:Button=findViewById(R.id.btn_checkout)
        button.setOnClickListener{
            val intent= Intent(this@CartListActivity,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        //getcartitemslist()
        getproductlist()
    }

    fun successcartitemslist(cartlist:ArrayList<Cartitem>){

        hideprogressdialog()

        for(product in mproductlist){
            for(cart in cartlist){
                if(product.product_id==cart.product_id){
                    cart.stock_quantity=product.stock_quantity
                    if(product.stock_quantity.toInt()==0){
                        cart.cart_quantity=product.stock_quantity
                    }
                }
            }
        }


        mcartlistitems=cartlist





        val recyclerview:RecyclerView=findViewById(R.id.rv_cart_items_list)
        val button: LinearLayout=findViewById(R.id.ll_checkout)
        val tv:customtextview=findViewById(R.id.tv_no_cart_item_found)
        if(mcartlistitems.size>0){
            recyclerview.visibility=View.VISIBLE
            tv.visibility=View.GONE
            button.visibility=View.VISIBLE
            val cartlistadapter=CartItemsListAdapter(this,cartlist)
            recyclerview.layoutManager=LinearLayoutManager(this@CartListActivity)
            recyclerview.setHasFixedSize(true)
            recyclerview.adapter=cartlistadapter
            var subtotal:Double=0.0
            for(i in mcartlistitems){
                val availablequantity=i.stock_quantity.toInt()
                if(availablequantity>0){
                    val price=i.price.toDouble()
                    val quantity=i.cart_quantity.toInt()
                    subtotal+=(price*quantity)

                }
            }
            val subtotaltv:customtextview=findViewById(R.id.tv_sub_total)
            subtotaltv.text="₹${subtotal}"
            val shippingchargetv:customtextview=findViewById(R.id.tv_shipping_charge)
            shippingchargetv.text="₹55"
            if(subtotal>0){
                val total=subtotal+55
                val totaltv:TextView=findViewById(R.id.tv_total_amount)
                totaltv.text="₹${total}"
            }
            else{
                button.visibility=View.GONE

            }
        }
        else{
            recyclerview.visibility=View.GONE
            button.visibility=View.GONE
            tv.visibility=View.VISIBLE
        }

    }
    fun itemremovedsuccess(){
        hideprogressdialog()
        Toast.makeText(this@CartListActivity,"Item Removed Successfully",Toast.LENGTH_SHORT).show()
        getcartitemslist()
    }
fun getcartitemslist(){
    FirestoreClass().cartlistget(this)
}
    fun successproductslistfromfirestore(productlist:ArrayList<Product>){
        mproductlist=productlist
        getcartitemslist()
    }
    private fun getproductlist(){
        showprogressdialog()
        FirestoreClass().getallproductslist(this)
    }

    private fun setupactionbar(){
        val actionbar=supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }
        val toolbar=findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_cart_list_activity).setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun itemupdatesuccess(){
        hideprogressdialog()
        getcartitemslist()
    }
}