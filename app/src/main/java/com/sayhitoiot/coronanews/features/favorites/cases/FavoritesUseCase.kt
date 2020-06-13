package com.sayhitoiot.coronanews.features.favorites.cases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity

class FavoritesUseCase {

    private val _favoritesFeed = MutableLiveData<MutableList<FeedEntity>>()
    private val _feed = MutableLiveData<FeedEntity>()
    private val _rates = MutableLiveData<MutableList<String>>()
    private val _statistics = MutableLiveData<MutableList<Float>>()

    val favoritesFeed: LiveData<MutableList<FeedEntity>?>? get() = _favoritesFeed
    val feed: LiveData<FeedEntity?>? get() = _feed

    val rates: LiveData<MutableList<String>?>? get() = _rates
    val statistics: LiveData<MutableList<Float>> get() = _statistics

    companion object {
        const val TAG = "getDataForFavorites"
    }

    fun getFavorites() {
        _favoritesFeed.postValue(FeedEntity.getAllFavorites())
    }

    fun getFeed(filter: String) {
        _feed.postValue(FeedEntity.findByFilter(filter))
        extractStatistics(filter)
    }

    private fun extractStatistics(filter: String) {
        val feed = FeedEntity.findByFilter(filter)
        val recoveries = feed?.recovereds?.toFloat() ?: 0f
        val deaths = feed?.deaths?.toFloat() ?: 0f
        val totalOfCases = feed?.cases?.toFloat() ?: 0f
        val rateRecoveries = ((recoveries / totalOfCases) * 100)
        val rateMortality = (deaths / totalOfCases) * 100

        val statistics: MutableList<Float> = mutableListOf()
        statistics.add(0, recoveries)
        statistics.add(1, deaths)
        statistics.add(2, totalOfCases)
        statistics.add(3, rateRecoveries)
        statistics.add(4, rateMortality)

        val rates: MutableList<String> = mutableListOf()
        rates.add("%.2f".format(rateRecoveries) + "%")
        rates.add("%.2f".format(rateMortality) + "%")


        _rates.postValue(rates)
        _statistics.postValue(statistics)
    }

}