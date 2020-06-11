package com.sayhitoiot.coronanews.features.splash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.splash.repository.RepositorySplash


class ViewModelSplashFactory(private val repositorySplash: RepositorySplash) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelSplash(repositorySplash) as T
  }

}