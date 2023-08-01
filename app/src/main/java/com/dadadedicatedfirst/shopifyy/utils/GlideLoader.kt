package com.dadadedicatedfirst.shopifyy.utils

import android.content.Context
import android.media.Image
import android.net.Uri
import android.util.AttributeSet
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dadadedicatedfirst.shopifyy.R

class GlideLoader(val context:Context) {
    fun loaduserimage(image: Any, imageview: ImageView){
        try{
            Glide.with(context).load(image).centerCrop().placeholder(R.drawable.ic_user_placeholder)
                .into(imageview)
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
    fun loadproductpicture(image: Any, imageview: ImageView){
        try{
            Glide.with(context).load(image).centerCrop()
                .into(imageview)
        }
        catch(e:Exception){
            e.printStackTrace()
        }
    }
}