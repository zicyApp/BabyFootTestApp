package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-21.
 */
data class LoginObject (
    var username: String = "",
    var password: String = "",
    var device: DeviceObject? = null
)