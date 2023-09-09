package com.dadadedicatedfirst.shopifyy.adapters

import android.content.Context
import android.media.Image
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.CartListActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Cartitem
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.dadadedicatedfirst.shopifyy.utils.GlideLoader
import com.dadadedicatedfirst.shopifyy.utils.customtextview
import com.myshoppal.models.Cart
import kotlinx.coroutines.NonDisposableHandle.parent
import org.w3c.dom.Text

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Cart>,
    private val updatecartitems:Boolean
    ):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return myviewholder(
            LayoutInflater.from(context).inflate(R.layout.item_cart_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model=list[position]
        if(holder is myviewholder){
            GlideLoader(context).loadproductpicture(model.image,holder.itemView.findViewById(R.id.iv_cart_item_image))
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_title).text=model.title
            holder.itemView.findViewById<TextView>(R.id.tv_cart_item_price).text="₹${model.price}"
            holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text=model.cart_quantity

            if(model.cart_quantity=="0"){
                holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility=View.GONE
                holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility=View.GONE


                if(updatecartitems){
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility=View.VISIBLE
                }
                else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility=View.GONE

                }




                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).text=context.resources.getString(R.string.lbl_out_of_stock)
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(context,R.color.colorsnackbarerror)
                )
            }
            else{
                if(updatecartitems) {
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                        View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                        View.VISIBLE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility=View.VISIBLE
                }
                else{
                    holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).visibility =
                        View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).visibility =
                        View.GONE
                    holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).visibility=View.GONE

                }
                holder.itemView.findViewById<TextView>(R.id.tv_cart_quantity).setTextColor(
                    ContextCompat.getColor(context,R.color.colorSecondaryText)
                )
            }

            holder.itemView.findViewById<ImageButton>(R.id.ib_remove_cart_item).setOnClickListener{

                if(model.cart_quantity=="1"){
                    FirestoreClass().removeitemfromcart(context,model.id)
                }
                else{

                    val cartquantity:Int=model.cart_quantity.toInt()
                    val itemhashmap=HashMap<String,Any>()
                    itemhashmap[Constants.CART_QUANTITY]=(cartquantity-1).toString()
                    if(context is CartListActivity){
                        context.showprogressdialog()
                    }
                    FirestoreClass().updatemycart(context,model.id,itemhashmap)
                }
            }
            
            holder.itemView.findViewById<ImageButton>(R.id.ib_add_cart_item).setOnClickListener{
                val cartquantity:Int=model.cart_quantity.toInt()
                if(cartquantity<model.stock_quantity.toInt()){
                    val itemhashmap=HashMap<String,Any>()
                    itemhashmap[Constants.CART_QUANTITY]=(cartquantity+1).toString()
                    if(context is CartListActivity){
                        context.showprogressdialog()
                    }
                    FirestoreClass().updatemycart(context,model.id,itemhashmap)
                }
                else{
                    if(context is CartListActivity){
                        context.showerrorsnackbar(context.getString(R.string.cannotaddmore),true)
                    }
                }
                
            }













            holder.itemView.findViewById<ImageButton>(R.id.ib_delete_cart_item).setOnClickListener{
                when(context){
                    is CartListActivity->{
                        context.showprogressdialog()
                    }
                }
                FirestoreClass().removeitemfromcart(context,model.id)
            }

        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
    private class myviewholder(view: View):RecyclerView.ViewHolder(view)
}