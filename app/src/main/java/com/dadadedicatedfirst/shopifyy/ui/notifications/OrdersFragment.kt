package com.dadadedicatedfirst.shopifyy.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dadadedicatedfirst.shopifyy.R

class OrdersFragment : Fragment() {
    class OrdersFragment : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val root = inflater.inflate(R.layout.fragment_orders, container, false)
            val textView: TextView = root.findViewById(R.id.text_notifications)
            textView.text = "This is Orders Fragment"
            return root
        }
    }
}