package com.dadadedicatedfirst.shopifyy.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.BaseActivity
import com.dadadedicatedfirst.shopifyy.LoginActivity
import com.dadadedicatedfirst.shopifyy.ProductDetailsActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.databinding.ActivityBaseBinding.inflate
import com.dadadedicatedfirst.shopifyy.models.Product
import com.dadadedicatedfirst.shopifyy.ui.home.ProductsFragment
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import com.dadadedicatedfirst.shopifyy.utils.MSPTextViewBold
import com.dadadedicatedfirst.shopifyy.utils.customtextview
import kotlinx.coroutines.NonDisposableHandle.parent

open class myproductlistadapter(private val context: Context,private var list:ArrayList<Product>,private val fragment: ProductsFragment) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return MyViewHolder(
           LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false)
       )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]
        if(holder is MyViewHolder){
            val view:View=LayoutInflater.from(context).inflate(R.layout.item_list_layout,null)
            val itemimage=view.findViewById<ImageView>(R.id.iv_item_image)
            GlideLoader(context).loadproductpicture(model.image,holder.itemView.findViewById(R.id.iv_item_image))
            holder.itemView.findViewById<MSPTextViewBold>(R.id.tv_item_name).text=model.title
            holder.itemView.findViewById<customtextview>(R.id.tv_item_price).text="₹${model.price}"
            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_product).setOnClickListener{
                fragment.deleteproduct(model.product_id)
            }
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

    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)

}

