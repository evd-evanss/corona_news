package com.sayhitoiot.coronanews.features.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.favorites.repository.RepositoryFavorites


class ViewModelFavoritesFactory(private val repositoryFavorites: RepositoryFavorites) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelFavorites(repositoryFavorites) as T
  }

}