package com.sayhitoiot.coronanews.features.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.login.cases.LoginUseCase

class ViewModelLoginFactory(private val delegate: DelegateToLoginActivity, private val loginUseCase: LoginUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelLogin(delegate, loginUseCase) as T
  }

}