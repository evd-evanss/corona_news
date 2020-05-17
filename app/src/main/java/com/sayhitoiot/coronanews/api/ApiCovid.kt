package com.sayhitoiot.coronanews.api

import com.sayhitoiot.coronanews.api.model.ResultData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

/*
   https://rapidapi.com/api-sports/api/covid-193
*/

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
