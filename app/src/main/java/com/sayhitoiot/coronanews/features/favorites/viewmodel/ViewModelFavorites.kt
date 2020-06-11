package com.sayhitoiot.coronanews.features.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.favorites.repository.RepositoryFavorites
import kotlinx.coroutines.launch

class ViewModelFavorites(private val repositoryFavorites: RepositoryFavorites) : ViewModel() {

    val favoritesFeed: LiveData<MutableList<FeedEntity>?>? = repositoryFavorites.favoritesFeed
    val rates: LiveData<MutableList<String>?>? = repositoryFavorites.rates
    val statistics: LiveData<MutableList<Float>> = repositoryFavorites.statistics
    val feed: LiveData<FeedEntity?>? = repositoryFavorites.feed

  init {
      fetchDataForFavorites()
  }

    private fun fetchDataForFavorites() {
        viewModelScope.launch {
            repositoryFavorites.getFavorites()
        }
    }

    fun onResumeStatistics(country: String?) {
        viewModelScope.launch {
            country?.let { repositoryFavorites.getFeed(it) }
        }
    }


}