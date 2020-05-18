package com.sayhitoiot.coronanews.features.home.feed.interact

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class FeedInteract(private val presenter: FeedPresenterToInteract) : FeedInteractToPresenter,
    CoroutineScope  {

    override val coroutineContext: CoroutineContext
        get() = Default + Job()

    private val repository: InteractToApi = ApiDataManager()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
    var livedata: MutableLiveData<Feed> = MutableLiveData()

    companion object{
        const val TAG = "feed-interact"
        const val FEEDS = "Feeds"
    }

    private val response: MutableList<Response> = mutableListOf()

    override fun fetchDataForFeed() {
        val feedList = FeedEntity.getAll()
        if(feedList.isEmpty()){
            fetchFeedOnApiCovid()
        } else {
            presenter.didFetchDataForFeed(feedList)
        }
    }

    private fun fetchFeedOnApiCovid() {
        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnDB(response)
                Log.d(TAG, "${response.size}")
            }

            override fun onError() {
                Log.e(TAG, "Error on fetch data")
                updateDayOnFeedFirebase()
            }
        })

    }

    private fun updateDayOnFeedFirebase() {
        val feedList = FeedEntity.getAll()
        repeat(feedList.size) {
            firebaseDatabase
                .reference
                .child(mAuth.uid!!)
                .child("Feeds")
                .child("${it})")
                .child("day")
                .setValue(feedList[it].day)
        }
        //fetchFeedOnFirebase()
    }

    private fun saveFeedOnDB(results: MutableList<Response>) {

        for(feed in results) {
            FeedEntity.update(
                country = feed.country,
                day = feed.day,
                cases = feed.cases.total,
                recoveries = feed.cases.recovered,
                deaths = feed.deaths.total,
                newCases = feed.cases.new
            )
        }

        updateDayOnFeedFirebase()

    }

    private fun fetchFeedOnFirebase() {
        val uid = mAuth.uid ?: return
        val userReference = firebaseDatabase.getReference(uid).child(FEEDS)

        userReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val response =
                    dataSnapshot.children
                response.mapNotNull {
                    userReference.removeEventListener(this)

                    val feed : Feed? = it.getValue(Feed::class.java)
                    livedata.postValue(feed)
                    if(feed != null) {
                        FeedEntity.favoriteFeed(
                            feed.country,
                            feed.favorite
                        )
                    }

                    val feedList = FeedEntity.getAll()
                    presenter.didFetchDataForFeed(feedList)
                    Log.d(TAG, "Sync $feed success")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to sync user.", error.toException())
            }
        })
    }


}