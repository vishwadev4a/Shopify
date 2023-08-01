package com.dadadedicatedfirst.shopifyy.utils

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class customedittext(context: Context, attrs: AttributeSet):AppCompatEditText(context,attrs) {

    init {
        applyfont()
    }
    private fun applyfont(){
        val typeface: Typeface = Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        setTypeface(typeface)
    }



}