package com.sayhitoiot.coronanews.commom.apicovid.model

data class Cases(
    val active: Int,
    val critical: Int,
    val new: String,
    val recovered: Int,
    val total: Int
)