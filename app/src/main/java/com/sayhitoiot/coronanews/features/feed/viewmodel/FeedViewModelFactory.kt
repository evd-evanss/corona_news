package com.sayhitoiot.coronanews.features.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.feed.cases.FeedsUseCase


class FeedViewModelFactory(private val feedsUseCase: FeedsUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return FeedViewModel(feedsUseCase) as T
  }

}