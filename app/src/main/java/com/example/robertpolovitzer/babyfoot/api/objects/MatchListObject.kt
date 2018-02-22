package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-22.
 */
data class MatchListObject (
    var id: Int = 0,
    var away_team: TeamObject? = null,
    var home_team: TeamObject? = null,
    var status: String = "",
    var place: PlaceObject? = null,
    var date: Long = 0
)