package com.sayhitoiot.coronanews.features.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.cases.FeedsUseCase
import kotlinx.coroutines.launch

class FeedViewModel(private val feedsUseCase: FeedsUseCase) : ViewModel() {

    val feed: LiveData<MutableList<FeedEntity>?>? = feedsUseCase.dataFeed

    val filter: LiveData<Int> = feedsUseCase.dataFilter

    val messages: LiveData<String> = feedsUseCase.dataMessages

    val animation: LiveData<Boolean> = feedsUseCase.animation

  init {
      fetchDataForFeed()
  }

    fun fetchDataForFeed() {
        viewModelScope.launch {
            feedsUseCase.getFeed()
        }
    }

    fun filterData(text: String) {
        feedsUseCase.getFeedBySearch(text)
    }

    fun filterData(filter: Int) {
        feedsUseCase.getFeedByFilter(filter)
    }

    fun favoriteFeed(country: String, favorite: Boolean) {
        feedsUseCase.favoriteFeedByCountry(country, favorite)
    }


}