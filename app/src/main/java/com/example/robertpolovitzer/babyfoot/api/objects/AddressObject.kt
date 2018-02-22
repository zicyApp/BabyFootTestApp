package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-22.
 */
data class AddressObject (
    var street: String =  "",
    var city: String = "",
    var state: String = "",
    var country: String = "",
    var zipcode: String = ""
)