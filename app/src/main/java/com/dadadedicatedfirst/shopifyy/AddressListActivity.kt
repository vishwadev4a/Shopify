package com.dadadedicatedfirst.shopifyy

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.adapters.AddressListAdapter
import com.dadadedicatedfirst.shopifyy.adapters.myproductlistadapter
import com.dadadedicatedfirst.shopifyy.databinding.ActivityAddEditAddressBinding
import com.dadadedicatedfirst.shopifyy.databinding.ActivityAddressListBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Address
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.myshoppal.utils.SwipeToDeleteCallback
import com.myshoppal.utils.SwipeToEditCallback

class AddressListActivity : BaseActivity() {
    private lateinit var binding:ActivityAddressListBinding
    private var mselectaddress:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddressListBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(binding.root)




        val tvaddaddress:TextView=findViewById<TextView?>(R.id.tv_add_address)
        tvaddaddress.setOnClickListener{
            val intent=Intent(this@AddressListActivity,AddEditAddressActivity::class.java)
            startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        }
        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mselectaddress=intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
        }
        if(mselectaddress){
             binding.tvTitle.text=resources.getString(R.string.title_select_address)
        }
    }

    fun getaddresslist(){
        showprogressdialog()
        FirestoreClass().getaddresslist(this@AddressListActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            if(requestCode==Activity.RESULT_OK) {
                getaddresslist()
            }
        }
    }
    fun successaddresslistfromfirestore(addresslist:ArrayList<Address>){
        hideprogressdialog()
        if(addresslist.size>0){
            binding.rvAddressList.visibility=View.VISIBLE
            binding.tvNoAddressFound.visibility=View.GONE
            binding.rvAddressList.layoutManager=LinearLayoutManager(this)
            binding.rvAddressList.setHasFixedSize(true)
            val addressadapter= AddressListAdapter(this,addresslist,mselectaddress)
            binding.rvAddressList.adapter=addressadapter
            if(!mselectaddress){
                val editswipehandler=object:SwipeToEditCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter=binding.rvAddressList.adapter as AddressListAdapter
                        adapter.notifyedititem(
                            this@AddressListActivity, viewHolder.adapterPosition
                        )
                    }

                }
                val edititemtouchhelper=ItemTouchHelper(editswipehandler)
                edititemtouchhelper.attachToRecyclerView(binding.rvAddressList)
                val deleteswipehandler=object: SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        showprogressdialog()
                        FirestoreClass().deleteaddress(this@AddressListActivity, addresslist[viewHolder.adapterPosition].id)
                    }

                }
                val deleteitemtouchhelper=ItemTouchHelper(deleteswipehandler)
                deleteitemtouchhelper.attachToRecyclerView(binding.rvAddressList)

            }

        }
        else{
            binding.rvAddressList.visibility=View.GONE
            binding.tvNoAddressFound.visibility=View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getaddresslist()
    }
    fun deleteaddresssuccess(){
        hideprogressdialog()
        Toast.makeText(this,R.string.addressdeleted,Toast.LENGTH_SHORT).show()
        getaddresslist()
    }
}