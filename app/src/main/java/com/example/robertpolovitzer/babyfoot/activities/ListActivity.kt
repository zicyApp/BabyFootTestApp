package com.example.robertpolovitzer.babyfoot.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import com.example.robertpolovitzer.babyfoot.R

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

    }
}