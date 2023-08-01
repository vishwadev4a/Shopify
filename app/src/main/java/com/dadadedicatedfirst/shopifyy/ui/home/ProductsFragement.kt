package com.dadadedicatedfirst.shopifyy.ui.home

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.view.View.GONE
import android.view.View.inflate
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.*
import com.dadadedicatedfirst.shopifyy.adapters.myproductlistadapter
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Product
import com.myshoppal.ui.fragments.BaseFragment
import org.w3c.dom.Text

class ProductsFragment : BaseFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successproductlistfromfirestore(productlist:ArrayList<Product>){
        val rv= activity?.findViewById<RecyclerView>(R.id.rv_my_product_items)
        val tv=activity?.findViewById<TextView>(R.id.tv_no_products_found)

        if(productlist.size>0){
            rv!!.visibility=View.VISIBLE
            tv!!.visibility=View.GONE
            rv.layoutManager=LinearLayoutManager(activity)
            rv.setHasFixedSize(true)
            val adapterproducts=myproductlistadapter(requireActivity(),productlist,this@ProductsFragment)
            rv.adapter=adapterproducts
        }
        else{
            rv!!.visibility= View.GONE
            tv!!.visibility=View.VISIBLE
        }

    }
    fun getproductlistfromfiresore(){
        FirestoreClass().getproductlist(this@ProductsFragment)
    }

    override fun onResume() {
        super.onResume()
        getproductlistfromfiresore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_products, container, false)
        return root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    fun deleteproduct(productid:String){
        showalertdialogtodeleteproduct(productid)
    }
    private fun showalertdialogtodeleteproduct(productid:String){
        val builder=AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.Yes)){
            dialoginterface,_->
            FirestoreClass().deleteproduct(this@ProductsFragment,productid)
                dialoginterface.dismiss()
        }
            builder.setNegativeButton(resources.getString(R.string.No)){dialoginterface,_->
                dialoginterface.dismiss()
        }
        val alertDialog:AlertDialog=builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    fun successdeletemethod(){

        getproductlistfromfiresore()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.add_product->{
                startActivity(Intent(activity, AddProductActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}