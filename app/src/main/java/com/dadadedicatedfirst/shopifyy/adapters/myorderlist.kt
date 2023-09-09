package com.dadadedicatedfirst.shopifyy.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.MyListOrder
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import com.myshoppal.models.Order

class myorderlist(private val context: Context, var list:ArrayList<Order>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return myviewholder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is myviewholder){
            GlideLoader(context).loadproductpicture(model.image,holder.itemView.findViewById(R.id.iv_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_item_name).text=model.title
            holder.itemView.findViewById<TextView>(R.id.tv_item_price).text="₹${model.total_amount}"
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).visibility=View.GONE
        }
        holder.itemView.setOnClickListener{
            val intent= Intent(context,MyListOrder::class.java)
            intent.putExtra(Constants.EXTRA_MY_ODER_DETAILS,model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class myviewholder(view: View):RecyclerView.ViewHolder(view)

}