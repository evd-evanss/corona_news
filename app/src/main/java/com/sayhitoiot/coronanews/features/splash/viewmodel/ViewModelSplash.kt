package com.sayhitoiot.coronanews.features.splash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.features.splash.repository.RepositorySplash
import kotlinx.coroutines.launch

class ViewModelSplash(private val repositorySplash: RepositorySplash) : ViewModel() {

    val login: LiveData<Boolean?> = repositorySplash.login

    init {
        fetchDataForUser()
    }

    private fun fetchDataForUser() {
        viewModelScope.launch {
            repositorySplash.fetchUser()
        }
    }

}