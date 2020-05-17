package com.sayhitoiot.coronanews.features.home.feed.interact.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sayhitoiot.coronanews.api.ApiCovid
import com.sayhitoiot.coronanews.api.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.api.model.ResultData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiDataManager : InteractToApi {
    private var service: ApiCovid
    private var statistic: MutableLiveData<ResultData> = MutableLiveData()

    companion object {
        const val BASE_URL = "https://covid-193.p.rapidapi.com/"
        const val host = "covid-193.p.rapidapi.com"
        const val key = "544555cc06msh818811d07ad8f96p1195d4jsn6cdd83152382"
    }

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiCovid::class.java)

    }

    override fun getStatistics(callbackStatistics: OnGetStatisticsCoronaCallback) {

        service.getStatistics(host, key)
            .enqueue(object : Callback<ResultData> {
                override fun onResponse(call: Call<ResultData>, response: Response<ResultData>) {
                    if (response.isSuccessful) {
                        statistic.postValue(response.body())
                        response.body()?.let { callbackStatistics.onSuccess(it) }
                        Log.d("Success_Request", "result = ${response.body()!!.response.size}")
                    } else {
                        callbackStatistics.onError()
                        Log.d("ResponseNetwork", response.raw().networkResponse.toString())
                    }
                }

                override fun onFailure(call: Call<ResultData>, t: Throwable) {
                    callbackStatistics.onError()
                    t.printStackTrace()
                    Log.d("Failure", t.toString())
                }
            })

    }

}