package com.sayhitoiot.coronanews.features.home.feed.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.Response
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.repository.ApiDataManager
import com.sayhitoiot.coronanews.commom.apicovid.repository.InteractToApi
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedPresenterToInteract
import com.sayhitoiot.coronanews.services.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class FeedInteract(private val presenter: FeedPresenterToInteract) : FeedInteractToPresenter,
    CoroutineScope  {

    override val coroutineContext: CoroutineContext
        get() = Default + Job()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private val repository: InteractToApi = ApiDataManager()
    private val response: MutableList<Response> = mutableListOf()

    companion object{
        const val TAG = "feed-interact"
        const val FEEDS = "Feeds"
        const val FAIL = "Tente novamente mais tarde"
    }

    override fun fetchDataForFeed() {
        val feedList = FeedEntity.getAll()

        when {
            feedList.isEmpty() -> syncApiCovid()
            feedList.isNotEmpty() -> presenter.didFetchDataForFeed(feedList)
        }

    }

    private fun syncApiCovid() {

        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnDB(response)
                saveFeedOnFirebase(response)
            }

            override fun onError() {
                Log.e(TAG, "Error on fetch data")
                presenter.didFetchDataForFeed(FeedEntity.getAll())
            }
        })

    }

    private fun saveFeedOnFirebase(response: MutableList<Response>) {
        val uid = mAuth.uid!!

        response.forEach {
            firebaseDatabase.reference.child(uid).child(FEEDS).child(it.country)
                .setValue(it)
        }


    }

    private fun saveFeedOnDB(results: MutableList<Response>) {

        if(FeedEntity.getAll().isEmpty()) {
            for(feed in results) {

                FeedEntity.create (
                    country = feed.country,
                    day = feed.day,
                    cases = feed.cases.total,
                    recoveries = feed.cases.recovered,
                    deaths = feed.deaths.total,
                    newCases = feed.cases.new,
                    favorite = false
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

        presenter.didFetchDataForFeed(FeedEntity.getAll())

    }

    override fun fetDataByFilter(text: String) {
        val feedFilter = FeedEntity.findByFilter(text)
        presenter.didFetchDataByFilter(feedFilter)
    }

}