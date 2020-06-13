package com.sayhitoiot.coronanews.commom.apicovid.model_states

data class State(
    val cases: Int,
    val datetime: String,
    val deaths: Int,
    val refuses: Int,
    val state: String,
    val suspects: Int,
    val uf: String,
    val uid: Int
)