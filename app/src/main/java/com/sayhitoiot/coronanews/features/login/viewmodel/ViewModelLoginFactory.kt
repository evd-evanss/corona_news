package com.sayhitoiot.coronanews.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.login.repository.RepositoryLogin

class ViewModelLoginFactory(private val delegate: DelegateToLoginActivity, private val repositoryLogin: RepositoryLogin) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelLogin(delegate, repositoryLogin) as T
  }

}