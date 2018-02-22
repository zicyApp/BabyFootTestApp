package com.example.robertpolovitzer.babyfoot.api.objects

/**
 * Created by robertpolovitzer on 18-02-22.
 */
data class PlaceObject (
    var name: String = "",
    var address: AddressObject? = null,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)