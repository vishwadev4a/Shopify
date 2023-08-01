package com.dadadedicatedfirst.shopifyy
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar;
import com.dadadedicatedfirst.shopifyy.databinding.ActivityRegisterActivtyBinding
import com.dadadedicatedfirst.shopifyy.firestore.FirestoreClass
import com.dadadedicatedfirst.shopifyy.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivty : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding= ActivityRegisterActivtyBinding.inflate(layoutInflater)
        val view:View=binding.getRoot()
        setContentView(view)
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        else{
            // Hide the status bar.
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
// Remember that you should never show the action bar if the
// status bar is hidden, so hide that too if necessary.
            actionBar?.hide()
        }
        setupactionbar()
        val alreadylogin:TextView=findViewById(R.id.tv_login)
        alreadylogin.setOnClickListener {
            onBackPressed()
        }
        binding.btnRegister.setOnClickListener{
            registerUser(binding)
        }

    }
    private fun setupactionbar(){
        val toolbarbutton:Toolbar=findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbarbutton)
        val actionbar=supportActionBar
        if(actionbar!=null){
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.indicator_toolbar)
        }
        toolbarbutton.setNavigationOnClickListener{onBackPressed()}
    }
    private fun validateregisterdetails(binding: ActivityRegisterActivtyBinding):Boolean{

        return when {
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim{it <=' '})->{
                showerrorsnackbar(resources.getString(R.string.err_msg_enter_first_name),true)
                false
            }
            TextUtils.isEmpty(binding.etLastName.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.err_msg_enter_last_name),true)
                false
            }
            TextUtils.isEmpty(binding.etEmail.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim{it<=' '})->{
                showerrorsnackbar(resources.getString(R.string.err_msg_enter_confirm_password),true)
                false
            }
            binding.etPassword.text.toString().trim{it<=' '}!=binding.etConfirmPassword.text.toString().trim{it<=' '}->{
                showerrorsnackbar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),true)
                false
            }
            !binding.cbTermsAndCondition.isChecked->{
                showerrorsnackbar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else->{
                //showerrorsnackbar(getString(R.string.validdetails),false)
                true
            }
        }
    }

    private fun registerUser(binding: ActivityRegisterActivtyBinding){
        if(validateregisterdetails(binding)){
            showprogressdialog()
            val email:String=binding.etEmail.text.toString().trim{it<=' '}
            val password:String=binding.etPassword.text.toString().trim{it<=' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{
                    task ->
                    if(task.isSuccessful){
                        val firebaseUser: FirebaseUser =task.result!!.user!!
                        val user= User(
                            firebaseUser.uid,
                            binding.etFirstName.text.toString().trim{it<=' '},
                                    binding.etLastName.text.toString().trim{it<=' '},
                                    binding.etEmail.text.toString().trim{it<=' '}
                        )

                        FirestoreClass().registeruseractivity(this@RegisterActivty,user)
                    }
                    else{
                        hideprogressdialog()
                        showerrorsnackbar(task.exception!!.message.toString()+" Please Login",true)
                    }
                }
            )
            
        }
    }

fun userregisteredsuccessful(){
    hideprogressdialog()
    Toast.makeText(this@RegisterActivty,getString(R.string.successfulregistered),Toast.LENGTH_SHORT).show()
}

}