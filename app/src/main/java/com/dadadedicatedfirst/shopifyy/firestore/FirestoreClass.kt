package com.dadadedicatedfirst.shopifyy.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dadadedicatedfirst.shopifyy.*
import com.dadadedicatedfirst.shopifyy.models.*
import com.dadadedicatedfirst.shopifyy.ui.dashboard.DashboardFragment
import com.dadadedicatedfirst.shopifyy.ui.home.ProductsFragment
import com.dadadedicatedfirst.shopifyy.ui.notifications.OrdersFragment
import com.dadadedicatedfirst.shopifyy.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.myshoppal.models.Cart
import com.myshoppal.models.Order

class FirestoreClass:LoginActivity(){
    val mfirestore = FirebaseFirestore.getInstance()
    fun registeruseractivity(activity: RegisterActivty, userInfo: User) {
        mfirestore.collection(Constants.USERS).document(userInfo.uid)
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
            activity.userregisteredsuccessful()

        }
            .addOnFailureListener {
                activity.hideprogressdialog()
            }
    }

    fun getuserid(): String {
        val currentuser = FirebaseAuth.getInstance().currentUser
        var currentuserid: String = ""
        if (currentuser != null) {
            currentuserid = currentuser.uid
        }
        return currentuserid
    }
    fun getallproductslist(activity: Activity){
        mfirestore.collection(Constants.PRODUCTS).get().addOnSuccessListener {
            document->
            val productlist:ArrayList<Product> = ArrayList()
            for(i in document.documents) {
                val product=i.toObject(Product::class.java)!!
                product.product_id=i.id
                productlist.add(product)

            }

            when(activity) {
                is CartListActivity-> {
                    activity.successproductslistfromfirestore(productlist)
                }
                is CheckoutActivity->{
                    activity.successproductlistfromfirestore(productlist)
                }
            }

        }.addOnFailureListener{
            when(activity) {
                is CartListActivity-> {
                    activity.hideprogressdialog()
                }
                is CheckoutActivity->{
                    activity.hideprogressdialog()
                }
            }


        }
        }
    fun getcurrentuser(activity: Activity) {

        mfirestore.collection(Constants.USERS).document(getuserid()).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)!!
                val sharedpreferences = activity.getSharedPreferences(
                    Constants.SHOPIFY_PREFERENCES,
                    Context.MODE_PRIVATE
                )


                val editor: SharedPreferences.Editor? = sharedpreferences.edit()
                editor?.putString(
                    Constants.LOGGED_IN_USERNAME, "${user.firstname} ${user.lastname}"
                )
                editor?.apply()
                when (activity) {
                    is LoginActivity -> {
                        activity.userloggedinsuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userdetailssuccess(user)
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {

                    is LoginActivity -> {
                        activity.hideprogressdialog()
                    }
                    is SettingsActivity -> {
                        activity.hideprogressdialog()
                    }

                }

            }

    }

    fun updateuserprofiledata(activity: Activity, userhashmap: HashMap<String, Any>) {
        mfirestore.collection(Constants.USERS).document(getuserid()).update(userhashmap)
            .addOnSuccessListener {

                when (activity) {
                    is UserProfile -> {
                        activity.userprofileupdatesuccess()
                    }
                }
            }.addOnFailureListener { e ->
            when (activity) {
                is UserProfile -> {
                    activity.hideprogressdialog()
                }
            }
        }
    }

    fun uploadproductdetails(activity: AddProductActivity, product: Product) {
        mfirestore.collection(Constants.PRODUCTS).document().set(product, SetOptions.merge())
            .addOnSuccessListener {
                activity.productuploadsuccess()
            }.addOnFailureListener {
            activity.hideprogressdialog()
        }

    }
fun updatemycart(context: Context,cartid: String,itemhashmap:HashMap<String,Any>){
    mfirestore.collection(Constants.CART_ITEMS).document(cartid).update(itemhashmap).addOnSuccessListener {
        when(context){
            is CartListActivity->{
                context.itemupdatesuccess()
            }
        }

    }.addOnFailureListener{
        when(context){
            is CartListActivity->{context.hideprogressdialog()}

        }
    }
}
    fun removeitemfromcart(context:Context,cartid:String){
        mfirestore.collection(Constants.CART_ITEMS).document(cartid).delete().addOnSuccessListener {
            when(context){
                is CartListActivity->{

                    context.itemremovedsuccess()

                }
            }



        }.addOnFailureListener{
            when(context){
                is CartListActivity->{
                    context.hideprogressdialog()
                }
            }

        }
    }


     fun cartlistget(activity:Activity) {
        mfirestore.collection(Constants.CART_ITEMS).whereEqualTo(Constants.USER_ID, getuserid())
            .get().addOnSuccessListener { document ->
                val list: ArrayList<Cart> = ArrayList()
                for (i in document.documents) {
                    val cartitem = i.toObject(Cart::class.java)!!
                    cartitem.id = i.id
                    list.add(cartitem)


                }
                when (activity) {
                    is CartListActivity -> {
                        activity.successcartitemslist(list)
                    }
                    is CheckoutActivity->{
                        activity.successcartitemslist(list)
                    }
                }
            }
            .addOnFailureListener {
                when (activity) {
                    is CartListActivity -> {
                        activity.hideprogressdialog()
                    }

                }
            }
    }

    fun uploadimagetocloudstorage(activity: Activity, imageuri: Uri?, imageType: String) {

        val sref: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "." + Constants.getfileextension(
                activity,
                imageuri
            )
        )
        sref.putFile(imageuri!!).addOnSuccessListener {
            sref.downloadUrl.addOnSuccessListener { url ->
                when (activity) {
                    is UserProfile -> {
                        activity.imageuploadsuccess(url.toString())
                    }
                    is AddProductActivity -> {
                        activity.imageuploadsuccess(url.toString())
                    }

                }
            }


        }.addOnFailureListener {

                exception ->

            when (activity) {
                is UserProfile -> {
                    activity.hideprogressdialog()
                }
                is AddProductActivity -> {
                    activity.hideprogressdialog()
                }

            }

        }
    }
    fun deleteproduct(fragment:ProductsFragment,productid:String){
        mfirestore.collection(Constants.PRODUCTS)
            .document(productid)
            .delete()
            .addOnSuccessListener {
                fragment.successdeletemethod()

            }.addOnFailureListener{
                showerrorsnackbar(getString(R.string.productcouldnotbedeleted),true)
            }
    }
    fun addcartitems(activity:ProductDetailsActivity, addtocart: Cart){

        mfirestore.collection(Constants.CART_ITEMS).document().set(addtocart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addtocartsuccess()
            }
            .addOnFailureListener{
                activity.hideprogressdialog()
            }

    }
    fun getproductdetails(activity: ProductDetailsActivity,productid:String){
        mfirestore.collection(Constants.PRODUCTS).document(productid).get().addOnSuccessListener {
            document->
            val product = document.toObject(Product::class.java)
            if (product != null) {
                activity.productdetailsuccess(product)
            }

        }.addOnFailureListener{
            hideprogressdialog()
        }

    }
    fun getdashboarditemslist(fragment: DashboardFragment){
        val productlist:ArrayList<Product> = ArrayList()
        mfirestore.collection(Constants.PRODUCTS).get().addOnSuccessListener { document->
            for(i in document.documents){
                val product=i.toObject(Product::class.java)!!
                Log.e("Bsdk",i.id)
                product.product_id=i.id
                productlist.add(product)
            }
            fragment.successdashboardlistfromfirestore(productlist)
        }
    }



    fun checkifitemexistincart(activity:ProductDetailsActivity,productid:String){
         mfirestore.collection(Constants.CART_ITEMS)
             .whereEqualTo(Constants.USER_ID,getuserid())
             .whereEqualTo(Constants.PRODUCT_ID,productid)
             .get()
             .addOnSuccessListener {document->
                 if(document.documents.size>0){
                     activity.productexistsincart()
                 }
                 else{
                     activity.hideprogressdialog()
                 }
             }
             .addOnFailureListener{
                 activity.hideprogressdialog()
             }
    }


    fun getproductlist(fragment: Fragment) {

        mfirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getuserid())
            .get().addOnSuccessListener { document ->
                val productlist: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productlist.add(product)
                }
                when (fragment) {
                    is ProductsFragment -> {
                        Log.e("Checkbro","Called")
                        fragment.successproductlistfromfirestore(productlist)
                    }
                }
            }.addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }

            }
    }

    fun addaddress(activity:AddEditAddressActivity,address: Address){

        mfirestore.collection(Constants.ADDRESSES).document().set(address, SetOptions.merge()).addOnSuccessListener {

            activity.addupdateaddresssuccess()
        }
            .addOnFailureListener{
                activity.hideprogressdialog()

            }
    }
fun updateaddress(activity: AddEditAddressActivity,address:Address,addressid:String){
    mfirestore.collection(Constants.ADDRESSES).document(addressid).set(address, SetOptions.merge()).addOnSuccessListener {

        activity.addupdateaddresssuccess()
    }
        .addOnFailureListener{
            activity.hideprogressdialog()
        }
}
    fun deleteaddress(activity: AddressListActivity,addressid:String){
        mfirestore.collection(Constants.ADDRESSES).document(addressid).delete().addOnSuccessListener {
            activity.deleteaddresssuccess()
        }.addOnFailureListener{
            activity.hideprogressdialog()
        }
    }
    fun getaddresslist(activity: AddressListActivity){
        mfirestore.collection(Constants.ADDRESSES).whereEqualTo(Constants.USER_ID,getuserid()).get().addOnSuccessListener { document->
            val addresslist:ArrayList<Address> = ArrayList()
            for(i in document.documents){
                val address=i.toObject(Address::class.java)
                address!!.id=i.id
                addresslist.add(address)
            }
            activity.successaddresslistfromfirestore(addresslist)

        }.addOnFailureListener{
            activity.hideprogressdialog()
        }
    }

    fun placeorder(activity: CheckoutActivity,order: Order){

        mfirestore.collection(Constants.ORDERS).document().set(order, SetOptions.merge()).addOnSuccessListener {
            activity.orderplacedsuccess()




        }
            .addOnFailureListener{
                activity.hideprogressdialog()



            }
    }

    fun updatealldetails(activity:CheckoutActivity,cartlist:ArrayList<Cart>,order: Order) {
        val writebatch = mfirestore.batch()

        for (cart in cartlist) {
            //producthashmap[Constants.STOCK_QUANTITY] =
                //(cart.stock_quantity.toInt() - cart.cart_quantity.toInt()).toString()
            val soldProduct=SoldProduct(
                FirestoreClass().getuserid(),
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.orderdatetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )
           val document=mfirestore.collection(Constants.SOLD_PRODUCT).document(cart.product_id)
            writebatch.set(document,soldProduct)
        }

        for (cartitem in cartlist) {
            val document=mfirestore.collection(Constants.CART_ITEMS).document(cartitem.id)
            val writeBatch=mfirestore.batch()
            writebatch.delete(document)
        }
        activity.alldetailsupdatedsuccessfully()
    }

    fun getmyorderlist(fragment: OrdersFragment){
        mfirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID,getuserid())
            .get()
            .addOnSuccessListener {document->
                val list:ArrayList<Order> = ArrayList()
                for(i in document.documents){
                    val orderitem=i.toObject(Order::class.java)!!
                    orderitem.id=i.id
                    list.add(orderitem)
                }
                fragment.populateorderslistinui(list)
            }.addOnFailureListener{

            }

    }





}