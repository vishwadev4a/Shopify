package com.dadadedicatedfirst.shopifyy.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dadadedicatedfirst.shopifyy.CartListActivity
import com.dadadedicatedfirst.shopifyy.R
import com.dadadedicatedfirst.shopifyy.SettingsActivity
import com.dadadedicatedfirst.shopifyy.adapters.mydashboardlistadapter
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Product

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    fun successdashboardlistfromfirestore(dashboardlist:ArrayList<Product>){

        val rv=activity?.findViewById<RecyclerView>(R.id.rv_dashboard_items)
        val tv=activity?.findViewById<TextView>(R.id.text_dashboard)
        if(dashboardlist.size>0){
            tv!!.visibility=View.GONE
            rv!!.visibility=View.VISIBLE
            rv.layoutManager=GridLayoutManager(activity,2)
            rv.setHasFixedSize(true)
            val dashboardadapter=mydashboardlistadapter(requireActivity(),dashboardlist)
            rv.adapter=dashboardadapter
        }
        else{
            rv!!.visibility=View.GONE
            tv!!.visibility=View.VISIBLE
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item.itemId
        when(id){
            R.id.dashboard_setting->{
                startActivity(Intent(activity, SettingsActivity::class.java))
            }
            R.id.action_cart->{
                startActivity(Intent(activity,CartListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun getdashboardlistfromfirestore(){
        FirestoreClass().getdashboarditemslist(this@DashboardFragment)
    }
    override fun onResume() {
        super.onResume()
        getdashboardlistfromfirestore()

    }
}