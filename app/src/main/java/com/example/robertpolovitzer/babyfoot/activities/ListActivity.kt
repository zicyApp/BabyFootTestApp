package com.example.robertpolovitzer.babyfoot.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife
import com.example.robertpolovitzer.babyfoot.R
import com.example.robertpolovitzer.babyfoot.api.ApiHandler
import com.example.robertpolovitzer.babyfoot.api.objects.LoginResponseObject
import com.example.robertpolovitzer.babyfoot.api.objects.MatchListObject
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.ArrayList
import javax.security.auth.callback.Callback

/**
 * Created by robertpolovitzer on 18-02-22.
 */
class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        ButterKnife.bind(this)
        initView()
    }

    fun initView() {
        ApiHandler().getService(this, true)?.getAllMatches(object: retrofit.Callback<ArrayList<MatchListObject>> {
            override fun success(t: ArrayList<MatchListObject>?, response: Response?) {
                Log.e("Response", "" + t.toString())
            }

            override fun failure(error: RetrofitError?) {

            }
        })
    }
}