package com.sayhitoiot.coronanews.features.feed.feed.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.R
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsByStatesCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.Response
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.model_states.ResultOfStates
import com.sayhitoiot.coronanews.commom.apicovid.repository.ApiDataManager
import com.sayhitoiot.coronanews.commom.apicovid.repository.InteractToApi
import com.sayhitoiot.coronanews.commom.extensions.toLocale
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.FilterEntity
import com.sayhitoiot.coronanews.features.feed.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.feed.feed.interact.contract.FeedPresenterToInteract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Job
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
        val feedList = fetchDataWithFilter(FilterEntity.getFilter()[0].filter)
        when {
            feedList.isEmpty() -> syncApiFirebase()
            feedList.isNotEmpty() -> presenter.didFetchDataForFeed(
                feedList,
                FilterEntity.getFilter()[0].filter
            )
        }
    }

    private fun fetchDataWithFilter(filter: Int) : MutableList<FeedEntity> {
        return when(filter) {
            0 -> fetchDataWithFilterMoreCases()
            1 -> fetchDataWithFilterFewerCases()
            2 -> fetchDataWithFilterContinentsCases()
            3 -> fetchDataWithFilterAllCases()
            else -> fetchDataWithFilterMoreCases()
        }
    }

    private fun syncApiFirebase() {
        val uid = mAuth.uid!!
        firebaseDatabase.reference.child(uid).child(FEEDS)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {

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
                presenter.didFetchDataForFeed(
                    FeedEntity.getAll(),
                    FilterEntity.getFilter()[0].filter
                )
            }
        })

        repository.getStatisticsByStates(object : OnGetStatisticsByStatesCoronaCallback{
            override fun onSuccess(coronaResult: ResultOfStates) {

            }

            override fun onError() {

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
            Log.d(TAG, feed.country)
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

        val feed = fetchDataWithFilter(FilterEntity.getFilter()[0].filter)
        presenter.didFetchDataForFeed(feed, FilterEntity.getFilter()[0].filter)

    }

    override fun fetDataByFilter(text: String) {
        val feedFilter = FeedEntity.findByFilter(text)
        val filter = FilterEntity.getFilter()[0].filter
        presenter.didFetchDataByFilter(feedFilter, filter)
    }

    override fun setFilter(filter: Int) {
        FilterEntity.update(filter)
        this.fetchDataForFeed()
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
        return casesByContinents
    }

    private fun fetchDataWithFilterAllCases() : MutableList<FeedEntity>{
        return FeedEntity.findByFilter("All")
    }
}