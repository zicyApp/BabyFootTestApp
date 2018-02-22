package com.example.robertpolovitzer.babyfoot.helpers

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import java.util.*
import android.text.TextUtils
import android.util.Log
import com.afollestad.materialdialogs.MaterialDialog
import com.example.robertpolovitzer.babyfoot.R
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


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

    fun checkIfLogged(c: Context): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        val lastLogged = getPref(c, AppHelper().SessionIssuedAt, 0)
        val limit = getPref(c, AppHelper().SessionExpiresIn, 0)
        if (currentTime < (lastLogged + limit)) {
            return true
        }
        return false
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

    fun getStatus(c: Context, status: String): String {
        if(status.equals("inProgress")) {
            return c.resources.getString(R.string.live)
        } else if (status.equals("final")) {
            return c.resources.getString(R.string.event_final)
        } else {
            return c.resources.getString(R.string.upcoming)
        }
    }

    fun getTime(timestamp: Long): String {
        val s = SimpleDateFormat("HH:mm")
        return s.format(Date(timestamp))
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun getTimeAgo(c: Context, timestamp: Long): String {
        try {
            Log.e("timestamp", "" + timestamp)
            val format = SimpleDateFormat("HH:mm", Locale.CANADA_FRENCH)
            var dateFormat = SimpleDateFormat("YYYY, MMM dd", Locale.CANADA_FRENCH)
            val past = Date(timestamp)
            val now = Date()
            val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - past.time)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
            val hours = TimeUnit.MILLISECONDS.toHours(now.time - past.time)

            if (seconds < 60) {
                return (c.getString(R.string.today) + ", " + format.format(past))
            } else if (minutes < 60) {
                return (c.getString(R.string.today) + ", " + format.format(past))
            } else if (hours < 24) {
                return (c.getString(R.string.yesterday) + ", " + format.format(past))
            } else if (hours > 48) {
                return (dateFormat.format(past))
            }
        } catch (j: Exception) {
            j.printStackTrace()
        }
        return ""
    }

    fun showAlert(c: Context, title: String, msg: String) {
        MaterialDialog.Builder(c)
                .title(title)
                .content(msg)
                .positiveText("Ok")
                .positiveColor(AppHelper().getColor(c, R.color.colorTextGreen))
                .onAny { _, _ ->

                }
                .show()
    }

}