package com.sayhitoiot.coronanews.features.home.feed.interact

import android.util.Log
import com.sayhitoiot.coronanews.api.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.api.model.Response
import com.sayhitoiot.coronanews.api.model.ResultData
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedInteractToPresenter
import com.sayhitoiot.coronanews.features.home.feed.interact.contract.FeedPresenterToInteract
import com.sayhitoiot.coronanews.features.home.feed.interact.repository.ApiDataManager
import com.sayhitoiot.coronanews.features.home.feed.interact.repository.InteractToApi


class FeedInteract(private val presenter: FeedPresenterToInteract) : FeedInteractToPresenter  {

    private val repository: InteractToApi = ApiDataManager()

    companion object{
        const val TAG = "feed-interact"
    }

    private val response: MutableList<Response> = mutableListOf()

    fun getStatisticsList() = response

    private fun fetchFeedOnApi() {
        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnDB(response)
                Log.d(TAG, "${response.size}")
            }

            override fun onError() {
                Log.e(TAG, "Error on fetch data")
                fetchFeedOnDB()
            }
        })

    }

    private fun saveFeedOnDB(results: MutableList<Response>) {
        for(feed in results) {
            FeedEntity.create(
                country = feed.country,
                day = feed.day,
                cases = feed.cases.total,
                recoveries = feed.cases.recovered,
                deaths = feed.deaths.total,
                newCases = feed.cases.new,
                favorite = false
            )
        }

        fetchFeedOnDB()

    }

    private fun fetchFeedOnDB() {
        val feedList = FeedEntity.getAll()
        presenter.didFetchDataForFeed(feedList)
    }

    override fun fetchDataForFeed() {
        val feedList = FeedEntity.getAll()
        if(feedList.isEmpty()){
            fetchFeedOnApi()
        } else {
            presenter.didFetchDataForFeed(feedList)
        }
    }
}