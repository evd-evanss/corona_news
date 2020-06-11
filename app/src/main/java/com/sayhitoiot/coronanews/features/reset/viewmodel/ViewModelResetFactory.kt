package com.sayhitoiot.coronanews.features.reset.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.reset.repository.RepositoryReset

class ViewModelResetFactory(private val delegate: DelegateToResetActivity, private val repositoryReset: RepositoryReset) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelReset(delegate, repositoryReset) as T
  }

}