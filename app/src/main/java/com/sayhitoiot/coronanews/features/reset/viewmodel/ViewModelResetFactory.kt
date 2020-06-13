package com.sayhitoiot.coronanews.features.reset.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sayhitoiot.coronanews.features.reset.cases.ResetUseCase

class ViewModelResetFactory(private val delegate: DelegateToResetActivity, private val resetUseCase: ResetUseCase) : ViewModelProvider.Factory {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ViewModelReset(delegate, resetUseCase) as T
  }

}