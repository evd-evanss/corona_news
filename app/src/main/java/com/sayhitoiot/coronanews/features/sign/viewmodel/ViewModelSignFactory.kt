package com.sayhitoiot.coronanews.features.sign.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.sign.cases.SignUseCase

class ViewModelSignFactory(private val delegate: DelegateToSignActivity, private val signUseCase: SignUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelSign(delegate, signUseCase) as T
  }

}