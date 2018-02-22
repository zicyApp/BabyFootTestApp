package com.example.robertpolovitzer.babyfoot.custom

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper

/**
 * Created by robertpolovitzer on 18-02-21.
 */
class RobotoMediumEditText(context: Context?, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {
    init {
        val typeface = AppHelper().getFont(getContext(), "Roboto-Medium.ttf")
        setTypeface(typeface)
    }
}