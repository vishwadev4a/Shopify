package com.dadadedicatedfirst.shopifyy

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dadadedicatedfirst.shopifyy.adapters.CartItemsListAdapter
import com.dadadedicatedfirst.shopifyy.databinding.ActivityCheckoutBinding
import com.dadadedicatedfirst.shopifyy.databinding.ActivityMyListOrderBinding
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.myshoppal.models.Order
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MyListOrder : AppCompatActivity() {
    private lateinit var binding:ActivityMyListOrderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMyListOrderBinding.inflate(layoutInflater)
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
        setContentView(binding.root)

        var myorderdetails: Order=Order()
        if(intent.hasExtra(Constants.EXTRA_MY_ODER_DETAILS)){
            myorderdetails=intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ODER_DETAILS)!!

        }

        setupui(myorderdetails)







    }

    private fun setupui(orderdetails:Order){

        binding.tvOrderDetailsId.text=orderdetails.title
        val dateformat="dd MM yyyy HH:mm"
        val formatter=SimpleDateFormat(dateformat, Locale.getDefault())
        val calender:Calendar=Calendar.getInstance()
        calender.timeInMillis=orderdetails.orderdatetime
        val orderdatetime=formatter.format(calender.time)
        binding.tvOrderDetailsDate.text=orderdatetime
        val diffinmilli:Long=System.currentTimeMillis()-orderdetails.orderdatetime
        val diffinhourse:Long=TimeUnit.MILLISECONDS.toHours(diffinmilli)
        when{
            diffinhourse<1->{
                binding.tvOrderStatus.text=resources.getString(R.string.order_status_pending)
                binding.tvOrderStatus.setTextColor(ContextCompat.getColor(this@MyListOrder,R.color.colorAccent))
            }
            diffinhourse<2->{
                binding.tvOrderStatus.text=resources.getString(R.string.orderstatusinprocess)
                binding.tvOrderStatus.setTextColor(ContextCompat.getColor(this@MyListOrder,R.color.orderinprocess))
            }
            else->{
                binding.tvOrderStatus.text=resources.getString(R.string.orderstatusdelivered)
                binding.tvOrderStatus.setTextColor(ContextCompat.getColor(this@MyListOrder,R.color.orderdelivered))
            }
        }
        binding.rvMyOrderItemsList.layoutManager=LinearLayoutManager(this@MyListOrder)
        binding.rvMyOrderItemsList.setHasFixedSize(true)
        val cartListAdapter=CartItemsListAdapter(this@MyListOrder,orderdetails.items,false)
        binding.rvMyOrderItemsList.adapter=cartListAdapter

        binding.tvMyOrderDetailsAddressType.text=orderdetails.address.type
        binding.tvMyOrderDetailsFullName.text=orderdetails.address.name
        binding.tvMyOrderDetailsAddress.text="${orderdetails.address.address},${orderdetails.address.zipCode}"
        binding.tvMyOrderDetailsAdditionalNote.text=orderdetails.address.additionalNote

        if(orderdetails.address.otherDetails.isNotEmpty()){
            binding.tvMyOrderDetailsOtherDetails.visibility=View.VISIBLE
            binding.tvMyOrderDetailsOtherDetails.text=orderdetails.address.otherDetails
        }
        else{
            binding.tvMyOrderDetailsOtherDetails.visibility=View.GONE
        }

        binding.tvMyOrderDetailsMobileNumber.text=orderdetails.address.mobileNumber
        binding.tvOrderDetailsSubTotal.text=orderdetails.sub_total_amount
        binding.tvOrderDetailsShippingCharge.text=orderdetails.shipping_charge
        binding.tvOrderDetailsTotalAmount.text=orderdetails.total_amount

    }
}