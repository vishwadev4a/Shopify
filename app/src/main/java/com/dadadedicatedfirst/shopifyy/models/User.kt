package com.dadadedicatedfirst.shopifyy.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    val uid:String="",
    val firstname:String="",
    val lastname:String="",
    val email:String="",
    val image:String="",
    val mobile:Long=0,
    val gender:String="",
    val profilecompleted:Int=0,
):Parcelable
