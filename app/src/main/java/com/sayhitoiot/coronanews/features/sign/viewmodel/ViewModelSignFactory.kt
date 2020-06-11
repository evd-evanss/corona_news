package com.sayhitoiot.coronanews.features.sign.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.sign.repository.RepositorySign

class ViewModelSignFactory(private val delegate: DelegateToSignActivity, private val repositorySign: RepositorySign) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelSign(delegate, repositorySign) as T
  }

}