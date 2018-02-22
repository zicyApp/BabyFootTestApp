package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-22.
 */
data class TeamObject (
    var name: String = "",
    var score: Int = 0,
    var players: Array<PlayerObject>?
)