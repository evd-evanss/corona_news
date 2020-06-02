package com.sayhitoiot.coronanews.commom.apicovid.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.sayhitoiot.coronanews.commom.apicovid.ApiCovid
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsByStatesCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.model_states.ResultOfStates
import com.sayhitoiot.coronanews.commom.apicovid.model_states.State
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.HOST
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.KEY
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.URL_BASE
import com.sayhitoiot.coronanews.commom.util.Constants.Companion.URL_STATES
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiDataManager : InteractToApi {

    private var service: ApiCovid
    //private var service2: ApiCovid
    private var statistic: MutableLiveData<ResultData> = MutableLiveData()
    private var statisticsByStates: MutableLiveData<State> = MutableLiveData()

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val retrofit2 = Retrofit.Builder()
            .baseUrl(URL_STATES)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiCovid::class.java)
        //service2 = retrofit2.create(ApiCovid::class.java)

    }

    override fun getStatistics(callbackStatistics: OnGetStatisticsCoronaCallback) {

        service.getStatistics(HOST, KEY)
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

    override fun getStatisticsByStates(callbackStatisticsByStatesCoronaCallback: OnGetStatisticsByStatesCoronaCallback) {
//        service2.getStatesOfBrazil()
//            .enqueue(object : Callback<ResultOfStates> {
//                override fun onFailure(call: Call<ResultOfStates>, t: Throwable) {
//                    callbackStatisticsByStatesCoronaCallback.onError()
//                    t.printStackTrace()
//                    Log.d("Failure", t.toString())
//                }
//
//                override fun onResponse(
//                    call: Call<ResultOfStates>,
//                    response: Response<ResultOfStates>
//                ) {
//                    if(response.body() != null) {
//
//                        response.body()?.let {
//                            callbackStatisticsByStatesCoronaCallback.onSuccess(it)
//                        }
//
//                    }
//
//                }
//
//            })
    }
}