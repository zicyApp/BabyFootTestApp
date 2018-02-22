package com.example.robertpolovitzer.babyfoot.adapters

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.bumptech.glide.Glide
import com.example.robertpolovitzer.babyfoot.R
import com.example.robertpolovitzer.babyfoot.activities.DetailActivity
import com.example.robertpolovitzer.babyfoot.activities.ListActivity
import com.example.robertpolovitzer.babyfoot.api.objects.MatchListObject
import com.example.robertpolovitzer.babyfoot.helpers.AppHelper

/**
 * Created by robertpolovitzer on 18-02-22.
 */
class ListViewAdapter(private val context: Context, private val matches: List<MatchListObject>) : RecyclerView.Adapter<ListViewAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return matches.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ListViewAdapter.CustomViewHolder, position: Int) {
        val item = matches[position]

        viewHolder.textDate.text = AppHelper().getTimeAgo(context, item.date)
        viewHolder.textScore.text = item.away_team!!.score.toString() + "-" + item.home_team!!.score.toString()
        viewHolder.textAwayTeam.text = item.away_team!!.name
        viewHolder.textAwayPlayers.text = item.away_team!!.players!!.first().firstName + ", " + item.away_team!!.players!!.last().firstName

        viewHolder.textHomeTeam.text = item.home_team!!.name
        viewHolder.textHomePlayers.text = item.home_team!!.players!!.first().firstName + ", " + item.home_team!!.players!!.last().firstName

        setImage(viewHolder.imageAwayFirst, item.away_team!!.players!!.first().imageUrl)
        setImage(viewHolder.imageAwaySecond, item.away_team!!.players!!.last().imageUrl)
        setImage(viewHolder.imageHomeFirst, item.home_team!!.players!!.first().imageUrl)
        setImage(viewHolder.imageHomeSecond, item.home_team!!.players!!.last().imageUrl)

        viewHolder.textAwayWinner.visibility = View.INVISIBLE
        viewHolder.textHomeWinner.visibility = View.INVISIBLE

        if(!item.status.equals("upcoming")) {
            if (item.away_team!!.score > item.home_team!!.score) {
                viewHolder.textAwayWinner.visibility = View.VISIBLE
            } else {
                viewHolder.textHomeWinner.visibility = View.VISIBLE
            }
        } else {
            viewHolder.textDate.text = AppHelper().getStatus(context, item.status)
        }

        viewHolder.textDuration.visibility = View.INVISIBLE

        viewHolder.baseView.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("matchId", item.id)
            context.startActivity(intent)
        }
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textDate: TextView
        var textAwayWinner: TextView
        var textHomeWinner: TextView
        var textDuration: TextView
        var textScore: TextView
        var textAwayTeam: TextView
        var textAwayPlayers: TextView
        var textHomeTeam: TextView
        var textHomePlayers: TextView
        var imageAwayFirst: ImageView
        var imageAwaySecond: ImageView
        var imageHomeFirst: ImageView
        var imageHomeSecond: ImageView
        var baseView: LinearLayout

        init {
            this.textDate = view.findViewById<TextView>(R.id.text_date) as TextView
            this.textAwayWinner = view.findViewById<TextView>(R.id.winner_away) as TextView
            this.textHomeWinner = view.findViewById<TextView>(R.id.winner_home) as TextView
            this.textDuration = view.findViewById<TextView>(R.id.text_duration) as TextView
            this.textScore = view.findViewById<TextView>(R.id.text_score) as TextView
            this.textAwayTeam = view.findViewById<TextView>(R.id.text_away_team) as TextView
            this.textAwayPlayers = view.findViewById<TextView>(R.id.text_away_players) as TextView
            this.textHomeTeam = view.findViewById<TextView>(R.id.text_home_team) as TextView
            this.textHomePlayers = view.findViewById<TextView>(R.id.text_home_players) as TextView
            this.imageAwayFirst = view.findViewById<ImageView>(R.id.image_away_first) as ImageView
            this.imageAwaySecond = view.findViewById<ImageView>(R.id.image_away_second) as ImageView
            this.imageHomeFirst = view.findViewById<ImageView>(R.id.image_home_first) as ImageView
            this.imageHomeSecond = view.findViewById<ImageView>(R.id.image_home_second) as ImageView
            this.baseView = view.findViewById<LinearLayout>(R.id.base_view) as LinearLayout
        }
    }

    fun setImage(image: ImageView, url: String) {
        Glide
                .with(context)
                .load(url)
                .centerCrop()
                .into(image)
    }
}