package com.example.robertpolovitzer.babyfoot.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.example.robertpolovitzer.babyfoot.R
import com.example.robertpolovitzer.babyfoot.adapters.ListViewAdapter
import com.example.robertpolovitzer.babyfoot.api.ApiHandler
import com.example.robertpolovitzer.babyfoot.api.objects.LoginResponseObject
import com.example.robertpolovitzer.babyfoot.api.objects.MatchListObject
import com.example.robertpolovitzer.babyfoot.api.objects.RefreshObject
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper
import retrofit.RetrofitError
import retrofit.client.Response
import java.util.ArrayList

/**
 * Created by robertpolovitzer on 18-02-22.
 */
class DetailActivity : AppCompatActivity() {

    @BindView(R.id.text_islive)
    lateinit var textIsLive: TextView

    @BindView(R.id.text_time)
    lateinit var textTime: TextView

    @BindView(R.id.text_score)
    lateinit var textScore: TextView

    @BindView(R.id.text_away_team)
    lateinit var textAwayTeam: TextView

    @BindView(R.id.text_away_players)
    lateinit var textAwayPlayers: TextView

    @BindView(R.id.text_home_team)
    lateinit var textHomeTeam: TextView

    @BindView(R.id.text_home_players)
    lateinit var textHomePlayers: TextView

    @BindView(R.id.image_away_first)
    lateinit var imageAwayFirst: ImageView

    @BindView(R.id.image_away_second)
    lateinit var imageAwaySecond: ImageView

    @BindView(R.id.image_home_first)
    lateinit var imageHomeFirst: ImageView

    @BindView(R.id.image_home_second)
    lateinit var imageHomeSecond: ImageView

    @BindView(R.id.recycle_view)
    lateinit var recyclerView: RecyclerView

    private var match: MatchListObject? = null
    private var matchId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        ButterKnife.bind(this)
        var intent: Intent = getIntent()
        if(intent.hasExtra("matchId")) {
            matchId = intent.getIntExtra("matchId", 0)
        } else {
            finish()
        }
        initView()
    }

    fun initView() {
        ApiHandler().getService(this, 1)?.getMatch(matchId, object: retrofit.Callback<MatchListObject> {
            override fun success(t: MatchListObject?, response: Response?) {
                match = t
                setMainItem(match!!)
            }

            override fun failure(error: RetrofitError?) {
                //Check if logged in
                if(AppHelper().checkIfLogged(applicationContext)) {
                    AppHelper().showAlert(applicationContext, resources.getString(R.string.error), resources.getString(R.string.error_occurred))
                } else{
                    //If fail move back to the login screen
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            }
        })
    }

    fun setMainItem(item: MatchListObject) {
        //Set all item's content
        textIsLive.text = AppHelper().getStatus(applicationContext, item.status)
        textTime.text = AppHelper().getTime(item.date)
        textScore.text = item.away_team!!.score.toString() + "-" + item.home_team!!.score.toString()
        textAwayTeam.text = item.away_team!!.name
        textAwayPlayers.text = item.away_team!!.players!!.first().firstName + "," + item.away_team!!.players!!.last().firstName

        textHomeTeam.text = item.home_team!!.name
        textHomePlayers.text = item.home_team!!.players!!.first().firstName + "," + item.home_team!!.players!!.last().firstName

        setImage(imageAwayFirst, item.away_team!!.players!!.first().imageUrl)
        setImage(imageAwaySecond, item.away_team!!.players!!.last().imageUrl)
        setImage(imageHomeFirst, item.home_team!!.players!!.first().imageUrl)
        setImage(imageHomeSecond, item.home_team!!.players!!.last().imageUrl)
    }

    fun setImage(image: ImageView, url: String) {
        Glide
                .with(this)
                .load(url)
                .centerCrop()
                .into(image)
    }

    override fun onResume() {
        super.onResume()
        //Move back to login screen if session expired
        if(!AppHelper().checkIfLogged(applicationContext)) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            return
        }

        refreshToken()
    }

    fun refreshToken() {
        if(AppHelper().getPref(applicationContext, AppHelper().SessionRefreshToken, "") != null) {
            var refresh = RefreshObject()
            refresh.refreshToken = AppHelper().getPref(applicationContext, AppHelper().SessionRefreshToken, "")!!

            ApiHandler().getService(this, 2)?.postRefresh(refresh, object : retrofit.Callback<LoginResponseObject> {
                override fun success(t: LoginResponseObject?, response: Response?) {

                }

                override fun failure(error: RetrofitError?) {

                }
            })
        }
    }
}