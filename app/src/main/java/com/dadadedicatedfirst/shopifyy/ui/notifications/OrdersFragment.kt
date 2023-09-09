package com.dadadedicatedfirst.shopifyy.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.BaseActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.adapters.myorderlist
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.myshoppal.models.Order
import com.myshoppal.ui.fragments.BaseFragment

    class OrdersFragment : BaseFragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_orders, container, false)

            return root
        }
        fun populateorderslistinui(orderslist:ArrayList<Order>){
            if(orderslist.size>0){
                val rv= activity?.findViewById<RecyclerView>(R.id.rv_my_order_items)
                rv!!.visibility=View.VISIBLE
                val tv=activity?.findViewById<TextView>(R.id.tv_no_orders_found)
                tv!!.visibility=View.GONE
                rv.layoutManager=LinearLayoutManager(activity)
                rv.setHasFixedSize(true)

                val myorderlistadapter=myorderlist(requireActivity(),orderslist)
                rv.adapter=myorderlistadapter
            }
            else{
                val rv= activity?.findViewById<RecyclerView>(R.id.rv_my_order_items)
                rv!!.visibility=View.GONE
                val tv=activity?.findViewById<TextView>(R.id.tv_no_orders_found)
                tv!!.visibility=View.VISIBLE

            }

        }
        private fun getmyorderslist(){
            FirestoreClass().getmyorderlist(this@OrdersFragment)
        }

        override fun onResume() {
            super.onResume()
            getmyorderslist()
        }
    }