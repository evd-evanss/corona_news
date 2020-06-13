package com.sayhitoiot.coronanews.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.profile.cases.ProfileUseCase


class ViewModelProfileFactory(private val profileUseCase: ProfileUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelProfile(profileUseCase) as T
  }

}