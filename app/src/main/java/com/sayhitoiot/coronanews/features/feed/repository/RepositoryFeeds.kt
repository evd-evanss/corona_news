package com.sayhitoiot.coronanews.features.feed.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
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
import com.sayhitoiot.coronanews.commom.extensions.toLocale
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.FilterEntity
import com.sayhitoiot.coronanews.features.feed.view.adapter.interact.FeedAdapterInteract
import com.sayhitoiot.coronanews.features.feed.view.adapter.interact.FeedAdapterInteract.Companion.FEEDS
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepositoryFeeds(private val ioDispatcher: CoroutineDispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = ioDispatcher + Job()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private val repository: InteractToApi = ApiDataManager()
    private val response: MutableList<Response> = mutableListOf()

    private val _dataFeed = MutableLiveData<MutableList<FeedEntity>>()
    private val _dataFilter = MutableLiveData<Int>()
    private val _dataMessages = MutableLiveData<String>()
    private val _animation = MutableLiveData<Boolean>()

    val dataFeed : LiveData<MutableList<FeedEntity>?> get() = _dataFeed
    val dataFilter : LiveData<Int> get() = _dataFilter
    val dataMessages: LiveData<String> get() = _dataMessages
    val animation: LiveData<Boolean> get() = _animation

    companion object {
        const val TAG = "getDataForFeed"
    }

    init {
        _dataFilter.postValue(FilterEntity.getFilter()?.filter)
    }

    fun getFeed() {
        _animation.postValue(true)
        val feedList = fetchDataWithFilter(FilterEntity.getFilter()?.filter)
        val filter = FilterEntity.getFilter()?.filter
        if(filter != null) {
            when {
                feedList.isEmpty() -> {
                    launch {
                        syncApiFirebase()
                    }
                }
                feedList.isNotEmpty() -> _dataFeed.postValue(FeedEntity.getAll())
            }
        }

    }

    private fun fetchDataWithFilter(filter: Int?) : MutableList<FeedEntity> {
        return when(filter) {
            0 -> fetchDataWithFilterMoreCases()
            1 -> fetchDataWithFilterFewerCases()
            2 -> fetchDataWithFilterContinentsCases()
            3 -> fetchDataWithFilterAllCases()
            else -> fetchDataWithFilterMoreCases()
        }
    }

    private fun fetchDataWithFilterMoreCases() : MutableList<FeedEntity> {
        val moreCases = FeedEntity.getAll()
        moreCases.sortByDescending { it.cases }
        return moreCases
    }

    private fun fetchDataWithFilterFewerCases() : MutableList<FeedEntity> {
        val fewerCases = FeedEntity.getAll()
        fewerCases.sortBy { it.cases }
        return fewerCases
    }

    private fun fetchDataWithFilterContinentsCases() : MutableList<FeedEntity>{
        val casesByContinents = mutableListOf<FeedEntity>()
        casesByContinents.addAll(FeedEntity.getAsiaFeed())
        casesByContinents.addAll(FeedEntity.getAfricaFeed())
        casesByContinents.addAll(FeedEntity.getEuropeFeed())
        casesByContinents.addAll(FeedEntity.getNorthAmericaFeed())
        casesByContinents.addAll(FeedEntity.getOceaniaFeed())
        casesByContinents.addAll(FeedEntity.getSouthAmericaFeed())
        casesByContinents.sortByDescending { it.cases }
        return casesByContinents
    }

    private fun fetchDataWithFilterAllCases() : MutableList<FeedEntity> {
        return FeedEntity.findByFilter("All")
    }

    private fun get(): LiveData<MutableList<FeedEntity>> = liveData {
        emit(FeedEntity.getAll())
    }

    private fun syncApiFirebase() {
        val uid = mAuth.uid!!
        firebaseDatabase.reference.child(uid).child(FEEDS)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                    _dataMessages.postValue("Falha ao buscar Feed, verifique sua internet e tente novamente.")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val feedFirebaseModel: MutableList<Feed> = mutableListOf()
                    snapshot.children.forEach {
                        val feed = it.getValue(Feed::class.java)
                        feed?.let { it1 ->
                            feedFirebaseModel.add(it1)
                        }
                    }
                    if(feedFirebaseModel.isNotEmpty()) {
                        saveFeedOnDB(feedFirebaseModel)
                    } else {
                        syncApiCovid()
                    }
                }

            })
    }

    private fun syncApiCovid() {

        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnFirebase(response)
            }

            override fun onError() {
                Log.e(TAG, "Error on fetch data")
                _dataMessages.postValue("Falha ao buscar Feed, verifique sua internet e tente novamente.")
            }
        })

    }

    private fun saveFeedOnFirebase(response: MutableList<Response>) {
        val uid = mAuth.uid!!

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
            firebaseDatabase.reference.child(uid).child(FEEDS).child(it.country)
                .setValue(it)
        }

        saveFeedOnDB(feedFirebaseModel)

    }

    private fun saveFeedOnDB(results: MutableList<Feed>) {

        if(FeedEntity.getAll().isEmpty()) {
            for(feed in results) {
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
        _dataFeed.postValue(FeedEntity.getAll())
    }

    fun getFeedBySearch(text: String) {
        _animation.postValue(true)
        val feedByFilter = FeedEntity.findByFilter(text)
        _dataFilter.postValue(FilterEntity.getFilter()?.filter)
        _dataFeed.postValue(feedByFilter)
    }

    fun getFeedByFilter(filter: Int) {
        _animation.postValue(true)
        FilterEntity.update(filter)
        val feedList = fetchDataWithFilter(FilterEntity.getFilter()?.filter)
        _dataFeed.postValue(feedList)
        _dataFilter.postValue(filter)
    }

    fun favoriteFeedByCountry(country: String, favorite: Boolean) {
        _animation.postValue(false)
        FeedEntity.update(country, !favorite)
        favoriteFeedInFirebase(country, !favorite)
        fetchFeedUpdated()
    }

    private fun favoriteFeedInFirebase(country: String, favorite: Boolean) {
        val uid = mAuth.uid!!
        firebaseDatabase.reference
            .child(uid)
            .child(FEEDS)
            .child(country)
            .child(FeedAdapterInteract.FAVORITE)
            .setValue(favorite)
    }

    private fun fetchFeedUpdated() {
        val filter = FilterEntity.getFilter()
        if(filter != null) {
            val feedUpdated = fetchDataWithFilter(filter.filter)
            _dataFeed.postValue(feedUpdated)
        }
    }

}