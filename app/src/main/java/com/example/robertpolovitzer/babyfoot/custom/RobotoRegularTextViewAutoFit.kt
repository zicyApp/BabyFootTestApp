package com.example.robertpolovitzer.babyfoot.custom

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper
import me.grantland.widget.AutofitTextView

/**
 * Created by robertpolovitzer on 18-02-21.
 */
class RobotoRegularTextViewAutoFit(context: Context?, attrs: AttributeSet?) : AutofitTextView(context, attrs) {
    init {
        val typeface = AppHelper().getFont(getContext(), "Roboto-Regular.ttf")
        setTypeface(typeface)
    }
}