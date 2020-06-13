package com.sayhitoiot.coronanews.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.Response
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.repository.Repository
import com.sayhitoiot.coronanews.commom.apicovid.repository.InteractToApi
import com.sayhitoiot.coronanews.commom.extensions.toLocale
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.services.SyncUser
import java.lang.Exception
import java.util.*

class SyncApiCovidWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private val repository: InteractToApi = Repository()
    private val response: MutableList<Response> = mutableListOf()

    override fun doWork(): Result {
        return try {
            syncApiCovid()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun syncApiCovid() {

        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(SyncUser.TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnFirebase(response)
                Log.d(TAG, "Success on fetch data: ${Date()}")
            }

            override fun onError() {
                Log.d(TAG, "Error on fetch data: ${Date()}")
            }
        })
    }

    private fun saveFeedOnFirebase(response: MutableList<Response>) {
        val uid = mAuth.uid

        val feedFirebaseModel: MutableList<Feed> = mutableListOf()

        response.forEach {
            feedFirebaseModel.add(
                Feed (
                    cases = it.cases.total,
                    country = it.country,
                    day = it.day.toLocale(),
                    deaths = it.deaths.total,
                    newCases = it.cases.new,
                    recoveries = it.cases.recovered,
                    favorite = false
                )
            )
        }

        feedFirebaseModel.forEach {
            uid?.let { it1 ->
                firebaseDatabase.reference.child(it1).child(SyncUser.FEEDS).child(it.country)
                    .setValue(it)
            }
        }

        saveFeedOnDB(feedFirebaseModel)

    }

    private fun saveFeedOnDB(results: MutableList<Feed>) {

        if(FeedEntity.getAll().isEmpty()) {
            for(feed in results) {
                Log.d(SyncUser.TAG, feed.country)
                FeedEntity.create (
                    country = feed.country,
                    day = feed.day ?: "",
                    cases = feed.cases,
                    recoveries = feed.recoveries,
                    deaths = feed.deaths,
                    newCases = feed.newCases,
                    favorite = feed.favorite
                )
            }
        } else {
            for(feed in results) {
                FeedEntity.update(
                    country = feed.country,
                    day = feed.day
                )
            }
        }
    }

    companion object {
        const val TAG = "sync-service"
    }

}
