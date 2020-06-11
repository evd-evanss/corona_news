package com.sayhitoiot.coronanews.features.feed.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.feed.repository.RepositoryFeeds


class FeedViewModelFactory(private val repositoryFeeds: RepositoryFeeds) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return FeedViewModel(repositoryFeeds) as T
  }

}