package com.dadadedicatedfirst.shopifyy.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.AddEditAddressActivity
import com.dadadedicatedfirst.shopifyy.CheckoutActivity
import com.dadadedicatedfirst.shopifyy.LoginActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.models.Address
import com.dadadedicatedfirst.shopifyy.utils.Constants
import kotlinx.coroutines.selects.select

open class AddressListAdapter(private val context:Context,private val list:ArrayList<Address>,private val selectedaddress:Boolean):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_address_layout,parent,false)
        )

    }
    fun notifyedititem(activity:Activity,position: Int){
        val intent= Intent(context,AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)

    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder){
            holder.itemView.findViewById<TextView>(R.id.tv_address_full_name).text=model.name
            holder.itemView.findViewById<TextView>(R.id.tv_address_type).text=model.type
            holder.itemView.findViewById<TextView>(R.id.tv_address_details).text="${model.address},${model.zipCode}"
            holder.itemView.findViewById<TextView>(R.id.tv_address_mobile_number).text=model.mobileNumber
            if(selectedaddress){
                holder.itemView.setOnClickListener{


                    val intent=Intent(context,CheckoutActivity::class.java)
                    intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS,model)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    private class MyViewHolder(view:View):RecyclerView.ViewHolder(view)
}