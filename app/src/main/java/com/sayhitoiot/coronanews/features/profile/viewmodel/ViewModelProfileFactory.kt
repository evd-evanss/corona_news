package com.sayhitoiot.coronanews.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.profile.repository.RepositoryProfile


class ViewModelProfileFactory(private val repositoryProfile: RepositoryProfile) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelProfile(repositoryProfile) as T
  }

}