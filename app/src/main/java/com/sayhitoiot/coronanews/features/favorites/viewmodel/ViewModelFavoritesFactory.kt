package com.sayhitoiot.coronanews.features.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.favorites.cases.FavoritesUseCase


class ViewModelFavoritesFactory(private val favoritesUseCase: FavoritesUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelFavorites(favoritesUseCase) as T
  }

}