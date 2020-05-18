package com.sayhitoiot.coronanews.commom.firebase.model

data class Feed(
    val cases: Int = 0,
    val country: String = "",
    val day: String = "",
    val deaths: Int = 0,
    val favorite: Boolean = false,
    val newCases: String = "",
    val recovereds: Int = 0
)