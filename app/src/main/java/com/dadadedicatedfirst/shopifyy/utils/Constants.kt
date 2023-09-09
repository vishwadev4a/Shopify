package com.dadadedicatedfirst.shopifyy.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.dadadedicatedfirst.shopifyy.MainActivity
import com.dadadedicatedfirst.shopifyy.UserProfile

object Constants {
    val SOLD_PRODUCT:String="sold_product"
    const val ORDERS:String="orders"
    val CART_ITEMS: String="cartitems"
    const val USERS:String="users"
    const val SHOPIFY_PREFERENCES:String="shopifyprefs"
    const val LOGGED_IN_USERNAME:String="loggedinusername"
    const val EXTRA_USER_DETAILS:String="extra_user_details"
    const val STOCK_QUANTITY:String="stock_quantity"
    const val READ_STORAGE_CODE=2
    const val EXTRA_MY_ODER_DETAILS:String="extramyorderdetails"
    const val PRODUCT_ID:String="product_id"
    const val PICK_IMAGE_REQUEST_CODE=2
    const val MALE:String="Male"
    const val PRODUCT_IMAGE="Product_Image"
    const val FEMALE:String="Female"
    const val FIRSTNAME:String="firstname"
    const val DEFAULT_CART_QUANTITY:String="1"
    const val LASTINAME:String="lastname"
    const val EXTRA_SELECTED_ADDRESS: String = "extra_selected_address"
    const val PRODUCTS:String="Products"
    const val CART_QUANTITY: String = "cart_quantity"
    const val MOBILE:String="mobile"
    const val GENDER:String="gender"
    const val COMPLELTE_PROFILE="profilecompleted"
    const val IMAGE:String="image"
    const val USER_PROFILE_IMAGE:String="User_Profile_Image"
    const val USER_ID: String = "user_id"
    const val EXTRA_PRODUCT_OWNER_ID:String="extra_product_owner_id"
    const val EXTRA_PRODUCT_ID:String="extra_product_i"
    const val HOME: String = "Home"
    const val OFFICE: String = "Office"
    const val OTHER: String = "Other"
    const val EXTRA_SELECT_ADDRESS:String="extra_select_address"
    const val EXTRA_ADDRESS_DETAILS:String="AddressDetails"
    const val ADD_ADDRESS_REQUEST_CODE:Int=121
    const val ADDRESSES:String="addresses"

    fun showImageChooser(activity: Activity){

        val galleryintent=Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryintent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getfileextension(activity: Activity,uri: Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}
