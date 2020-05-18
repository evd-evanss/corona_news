package com.sayhitoiot.coronanews.commom.apicovid.model

data class ResultData(
    val errors: List<Any>,
    val `get`: String,
    val parameters: List<Any>,
    val response: List<Response>,
    val results: Int
)