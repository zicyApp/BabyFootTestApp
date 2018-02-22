package com.example.robertpolovitzer.babyfoot.activities

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.example.robertpolovitzer.babyfoot.R
import com.example.robertpolovitzer.babyfoot.api.ApiHandler
import com.example.robertpolovitzer.babyfoot.api.objects.DeviceObject
import com.example.robertpolovitzer.babyfoot.api.objects.LoginObject
import com.example.robertpolovitzer.babyfoot.api.objects.LoginResponseObject
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper
import retrofit.Callback
import retrofit.RetrofitError
import retrofit.client.Response

class LoginActivity : AppCompatActivity() {

    @BindView(R.id.form_firstname)
    lateinit var firstName: EditText

    @BindView(R.id.form_lastname)
    lateinit var lastName: EditText

    @BindView(R.id.submit_button)
    lateinit var submitButton: TextView

    @BindView(R.id.req_firstname)
    lateinit var requiredFirstname: TextView

    @BindView(R.id.req_lastname)
    lateinit var requiredLastname: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        initView()
    }

    private fun initView() {
        firstName.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                lastName.requestFocus()
                return@OnKeyListener true
            }
            false
        })
        firstName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(firstName.text.length != 0) {
                    requiredFirstname.visibility = View.GONE
                }else{
                    requiredFirstname.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        lastName.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if(lastName.text.length != 0) {
                    executeSubmit()
                }
                return@OnKeyListener true
            }
            false
        })
        lastName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(lastName.text.length != 0) {
                    requiredLastname.visibility = View.GONE
                }else{
                    requiredLastname.visibility = View.VISIBLE
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        submitButton.setOnClickListener(submitClick)
    }

    val submitClick = View.OnClickListener { view ->
        executeSubmit()
    }

    private fun executeSubmit() {
        if (firstName.text.length == 0) {
            showAlert(resources.getString(R.string.error), resources.getString(R.string.first_name_missing))
            return
        }

        if (lastName.text.length == 0) {
            showAlert(resources.getString(R.string.error), resources.getString(R.string.last_name_missing))
            return
        }

        val progressDialog = MaterialDialog.Builder(this)
                .content(resources.getString(R.string.loading_message))
                .cancelable(false)
                .progress(true, 0)
                .progressIndeterminateStyle(false)
                .show()

        var login = LoginObject()
        login.username = firstName.text.toString()
        login.password = lastName.text.toString()

        val device = DeviceObject()
        device.id = AppHelper().getUniquePhoneIdentity()
        device.model = AppHelper().getDeviceName().toString()
        device.type = "ANDROID"

        login.device = device

        ApiHandler().getService(this@LoginActivity, false)?.postLogin(login, object: Callback<LoginResponseObject> {
            override fun success(t: LoginResponseObject?, response: Response?) {
                if (t != null) {
                    ApiHandler().setApiToken(t.accessToken)
                    AppHelper().setPref(applicationContext, AppHelper().SessionAccessToken, t.accessToken)
                    AppHelper().setPref(applicationContext, AppHelper().SessionRefreshToken, t.refreshToken)
                    AppHelper().setPref(applicationContext, AppHelper().SessionIssuedAt, t.issuedAt)
                    AppHelper().setPref(applicationContext, AppHelper().SessionExpiresIn, t.expiresIn)

                    val intent = Intent(this@LoginActivity, ListActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun failure(error: RetrofitError?) {
                progressDialog.dismiss()
                showAlert(resources.getString(R.string.error), resources.getString(R.string.error_occurred))
            }
        })
    }

    fun showAlert(title: String, msg: String) {
        MaterialDialog.Builder(this@LoginActivity)
                .title(title)
                .content(msg)
                .positiveText("Ok")
                .positiveColor(AppHelper().getColor(this, R.color.colorTextGreen))
                .onAny { _, _ ->

                }
                .show()
    }

}
