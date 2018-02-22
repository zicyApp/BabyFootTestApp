package com.example.robertpolovitzer.babyfoot.custom

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper

/**
 * Created by robertpolovitzer on 18-02-21.
 */
class RobotoBoldTextView (context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {
    init {
        val typeface = AppHelper().getFont(getContext(), "Roboto-Black.ttf")
        setTypeface(typeface)
    }
}