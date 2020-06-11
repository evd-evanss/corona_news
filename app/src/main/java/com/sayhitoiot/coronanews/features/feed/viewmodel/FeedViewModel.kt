package com.sayhitoiot.coronanews.features.feed.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.feed.repository.RepositoryFeeds
import kotlinx.coroutines.launch

class FeedViewModel(private val repositoryFeeds: RepositoryFeeds) : ViewModel() {

    val feed: LiveData<MutableList<FeedEntity>?>? = repositoryFeeds.dataFeed

    val filter: LiveData<Int> = repositoryFeeds.dataFilter

    val messages: LiveData<String> = repositoryFeeds.dataMessages

    val animation: LiveData<Boolean> = repositoryFeeds.animation

  init {
      fetchDataForFeed()
  }

    fun fetchDataForFeed() {
        viewModelScope.launch {
            repositoryFeeds.getFeed()
        }
    }

    fun filterData(text: String) {
        repositoryFeeds.getFeedBySearch(text)
    }

    fun filterData(filter: Int) {
        repositoryFeeds.getFeedByFilter(filter)
    }

    fun favoriteFeed(country: String, favorite: Boolean) {
        repositoryFeeds.favoriteFeedByCountry(country, favorite)
    }


}