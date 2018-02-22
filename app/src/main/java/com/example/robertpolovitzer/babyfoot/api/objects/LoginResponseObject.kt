package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-21.
 */
data class LoginResponseObject (
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Long = 0,
    val issuedAt: Long = 0
)