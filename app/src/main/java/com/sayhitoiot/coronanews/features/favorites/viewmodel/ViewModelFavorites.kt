package com.sayhitoiot.coronanews.features.favorites.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.features.favorites.cases.FavoritesUseCase
import kotlinx.coroutines.launch

class ViewModelFavorites(private val favoritesUseCase: FavoritesUseCase) : ViewModel() {

    val favoritesFeed: LiveData<MutableList<FeedEntity>?>? = favoritesUseCase.favoritesFeed
    val rates: LiveData<MutableList<String>?>? = favoritesUseCase.rates
    val statistics: LiveData<MutableList<Float>> = favoritesUseCase.statistics
    val feed: LiveData<FeedEntity?>? = favoritesUseCase.feed

  init {
      fetchDataForFavorites()
  }

    private fun fetchDataForFavorites() {
        viewModelScope.launch {
            favoritesUseCase.getFavorites()
        }
    }

    fun onResumeStatistics(country: String?) {
        viewModelScope.launch {
            fetchDataForFavorites()
            country?.let { favoritesUseCase.getFeed(it) }
        }
    }


}