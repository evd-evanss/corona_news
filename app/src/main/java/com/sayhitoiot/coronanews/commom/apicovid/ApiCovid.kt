package com.sayhitoiot.coronanews.commom.apicovid

import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiCovid{

    @GET("statistics")
    fun getStatistics(
        @Header("x-rapidapi-host") host: String,
        @Header("x-rapidapi-key") key: String
    ): Call<ResultData>

}

interface OnGetStatisticsCoronaCallback{
    fun onSuccess(coronaResult: ResultData)
    fun onError()
}
