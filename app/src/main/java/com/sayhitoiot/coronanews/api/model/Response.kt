package com.sayhitoiot.coronanews.api.model

data class Response(
    val cases: Cases,
    val country: String,
    val day: String,
    val deaths: Deaths,
    val time: String
)