package com.dadadedicatedfirst.shopifyy.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.ProductDetailsActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import com.dadadedicatedfirst.shopifyy.utils.customtextview

class mydashboardlistadapter(
    private val context: Context,
    private val list:ArrayList<Product>
):RecyclerView.Adapter<RecyclerView.ViewHolder> (){
    private var onClickListener: View.OnClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return myproductlistadapter.MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.itemdashboardlayout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is myproductlistadapter.MyViewHolder){
            GlideLoader(context).loadproductpicture(model.image,holder.itemView.findViewById(R.id.iv_dashboard_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_dashboard_item_title).text=model.title
            holder.itemView.findViewById<customtextview>(R.id.tv_dashboard_item_price).text="₹${model.price}"
            holder.itemView.setOnClickListener{
                val intent= Intent(context,ProductDetailsActivity::class.java)
                intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
                intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
                context.startActivity(intent)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}