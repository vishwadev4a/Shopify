package com.dadadedicatedfirst.shopifyy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.dadadedicatedfirst.shopifyy.databinding.ActivityAddEditAddressBinding
import com.dadadedicatedfirst.shopifyy.databinding.ActivityAddressListBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.Address
import com.dadadedicatedfirst.shopifyy.utils.Constants

class AddEditAddressActivity : BaseActivity() {
    private var maddressdetails:Address?=null
    private lateinit var binding: ActivityAddEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            maddressdetails=intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)

        }
        if(maddressdetails!=null){
        if(maddressdetails!!.id.isNotEmpty()) {
            binding.tvTitle.text = getString(R.string.editaddress)
            binding.btnSubmitAddress.text = getString(R.string.updateaddress)
            binding.etFullName.setText(maddressdetails?.name)
            binding.etPhoneNumber.setText(maddressdetails?.mobileNumber)
            binding.etAddress.setText(maddressdetails?.address)
            binding.etZipCode.setText(maddressdetails?.zipCode)
            binding.etAdditionalNote.setText(maddressdetails?.additionalNote)
            when (maddressdetails?.type) {
                Constants.HOME -> {
                    binding.rbHome.isChecked = true
                }
                Constants.OFFICE -> {
                    binding.rbOffice.isChecked = true
                }
                else -> {
                    binding.rbOther.isChecked = true
                    binding.tilOtherDetails.visibility = View.VISIBLE
                    binding.etOtherDetails.setText(maddressdetails?.otherDetails)
                }
            }
        }


        }
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN
        binding.btnSubmitAddress.setOnClickListener{
            saveaddresstofirestore()
        }

        binding.rgType.setOnCheckedChangeListener{_,checkid->
            if(checkid==R.id.rb_other){
                binding.tilOtherDetails.visibility=View.VISIBLE
            }
            else {
                binding.tilOtherDetails.visibility = View.INVISIBLE
            }
        }


    }
    private fun validatedata():Boolean{
        return when {
            TextUtils.isEmpty(binding.etFullName.text.toString().trim{it<=' '})->{
                showerrorsnackbar("Please Enter Full Name",true)
                false
            }
            TextUtils.isEmpty(binding.etPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showerrorsnackbar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etAddress.text.toString().trim { it <= ' ' }) -> {
                showerrorsnackbar(
                    getString(R.string.emptyaddress),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etZipCode.text.toString().trim { it <= ' ' }) -> {
                showerrorsnackbar(
                    getString(R.string.emptyzipcode),
                    true
                )
                false
            }
            binding.rbOther.isChecked && TextUtils.isEmpty(binding.etZipCode.text.toString().trim { it <= ' ' }) -> {
                showerrorsnackbar(resources.getString(R.string.err_msg_please_enter_zip_code), true)
                false
            }
            else->{
                true
            }

        }
    }

    private fun saveaddresstofirestore(){

        val fullname:String=binding.etFullName.text.toString().trim{it<= ' '}
        val phonenumber:String=binding.etPhoneNumber.text.toString().trim{it<= ' '}
        val address:String=binding.etAddress.text.toString().trim{it<= ' '}
        val zipcode:String=binding.etZipCode.text.toString().trim{it<= ' '}
        val additionalnote:String=binding.etAdditionalNote.text.toString().trim{it<= ' '}
        val otherdetails: String = binding.etOtherDetails.text.toString().trim { it <= ' ' }
        if(validatedata()){
            showprogressdialog()
            val addresstype: String = when {
                binding.rbHome.isChecked -> {
                    Constants.HOME
                }
                binding.rbOffice.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressmodel= Address(
                FirestoreClass().getuserid(),
                fullname,
                phonenumber,
                address,
                zipcode,
                additionalnote,
                addresstype,
                otherdetails
            )

            if(maddressdetails!=null && maddressdetails!!.id.isNotEmpty()){
                FirestoreClass().updateaddress(this,addressmodel,maddressdetails!!.id)
            }
            else {
                FirestoreClass().addaddress(this@AddEditAddressActivity, addressmodel)
            }
        }
    }
    fun addupdateaddresssuccess(){
        hideprogressdialog()
        val notifysuccessmessage: String =if (maddressdetails!=null && maddressdetails!!.id.isNotEmpty()){
            getString(R.string.addressupdated)

        }
        else{
            getString(R.string.addressaddedsuccess)

        }
        Toast.makeText(this@AddEditAddressActivity,notifysuccessmessage,Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK )
        finish()
    }

}