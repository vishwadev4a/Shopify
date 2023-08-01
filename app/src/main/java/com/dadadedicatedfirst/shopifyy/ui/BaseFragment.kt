package com.myshoppal.ui.fragments

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.dadadedicatedfirst.shopifyy.R

/**
 * A base fragment class is used to define the functions and members which we will use in all the fragments.
 * It inherits the Fragment class so in other fragment class we will replace the Fragment with BaseFragment.
 */
open class BaseFragment : Fragment() {
    private lateinit var mprogressdialog:Dialog
    /**
     * This is a progress dialog instance which we will initialize later on.
     */
    // END

    /**
     * This function is used to show the progress dialog with the title and message to user.
     */
    fun showprogressdialog(){
       val mprogressdialog=Dialog(requireActivity())
        mprogressdialog.setContentView(R.layout.dialog_progress)
        mprogressdialog.setCancelable(false)
        mprogressdialog.setCanceledOnTouchOutside(false)
        mprogressdialog.show()
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        mprogressdialog.dismiss()
    }
}