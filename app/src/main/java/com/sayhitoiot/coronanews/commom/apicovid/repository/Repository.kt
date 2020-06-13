package com.sayhitoiot.coronanews.commom.apicovid.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sayhitoiot.coronanews.BuildConfig
import com.sayhitoiot.coronanews.commom.apicovid.ApiCovid
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsByStatesCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.model_states.ResultOfStates
import com.sayhitoiot.coronanews.commom.apicovid.model_states.State
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.URL_STATES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository : InteractToApi {

    companion object {
        const val HOST = BuildConfig.HOST
        const val KEY = BuildConfig.API_KEY
        const val URL_BASE = BuildConfig.URL_BASE
    }

    private var serviceCountry: ApiCovid
    private var serviceStates: ApiCovid
    private var statistic: MutableLiveData<ResultData> = MutableLiveData()
    private var statisticsByStates: MutableLiveData<State> = MutableLiveData()

    init {

        val clientOne = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val clientTwo = Retrofit.Builder()
            .baseUrl(URL_STATES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        serviceCountry = clientOne.create(ApiCovid::class.java)
        serviceStates = clientTwo.create(ApiCovid::class.java)

    }

    override fun getStatistics(callbackStatistics: OnGetStatisticsCoronaCallback) {

        serviceCountry.getStatistics(HOST, KEY)
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

    override fun getStatisticsByStates(
        callbackStatisticsByStatesCoronaCallback: OnGetStatisticsByStatesCoronaCallback) {
        serviceStates.getStatesOfBrazil()
            .enqueue(object : Callback<ResultOfStates> {
                override fun onFailure(call: Call<ResultOfStates>, t: Throwable) {
                    callbackStatisticsByStatesCoronaCallback.onError()
                    t.printStackTrace()
                    Log.d("Failure", t.toString())
                }

                override fun onResponse(
                    call: Call<ResultOfStates>,
                    response: Response<ResultOfStates>
                ) {
                    if(response.body() != null) {

                        response.body()?.let {
                            callbackStatisticsByStatesCoronaCallback.onSuccess(it)
                        }

                    }

                }

            })
    }
}