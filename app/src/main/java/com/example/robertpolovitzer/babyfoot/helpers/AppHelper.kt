package com.example.robertpolovitzer.babyfoot.helpers

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import java.util.*
import android.text.TextUtils



/**
 * Created by robertpolovitzer on 18-02-21.
 */
class AppHelper {

    val SessionAccessToken: String = "SessionAccessToken"
    val SessionRefreshToken: String = "SessionRefreshToken"
    val SessionExpiresIn: String = "SessionExpiresIn"
    val SessionIssuedAt: String = "SessionIssuedAd"

    fun setPref(c: Context, pref: String, v: String) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putString(pref, v)
        e.commit()
    }

    fun getPref(c: Context, pref: String, v: String): String? {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                v)
    }

    fun setPref(c: Context, pref: String, v: Long) {
        val e = PreferenceManager.getDefaultSharedPreferences(c).edit()
        e.putLong(pref, v)
        e.commit()
    }

    fun getPref(c: Context, pref: String, v: Long): Long {
        try {
            return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref, v)
        } catch (e: Exception) {
            return v
        }

    }

    fun getBearer(c: Context): String {
        if (getPref(c, AppHelper().SessionAccessToken, "") != null) {
            return getPref(c, AppHelper().SessionAccessToken, "").toString()
        }
        return ""
    }

    fun getFont(c: Context, path: String): Typeface {
        return Typeface.createFromAsset(c.assets, path)
    }

    fun getColor(context: Context, dp: Int): Int {
        val version = Build.VERSION.SDK_INT
        return if (version >= 23) {
            ContextCompat.getColor(context, dp.toInt())
        } else {
            context.resources.getColor(dp.toInt())
        }
    }

    fun getUniquePhoneIdentity(): String {
        val m_szDevIDShort = "35" + Build.BOARD.length % 10 + Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
        var serial: String? = null
        try {
            serial = Build::class.java.getField("SERIAL").get(null).toString()
            // Go ahead and return the serial for api => 9
            return "android-" + UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString()
        } catch (exception: Exception) {
            // String needs to be initialized
            serial = "serial" // some value
        }
        return "android-" + UUID(m_szDevIDShort.hashCode().toLong(), serial!!.hashCode().toLong()).toString()
    }

    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }

    private fun capitalize(str: String): String? {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true

        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }

        return phrase.toString()
    }

}