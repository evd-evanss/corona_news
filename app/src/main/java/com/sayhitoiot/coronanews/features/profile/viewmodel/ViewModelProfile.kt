package com.sayhitoiot.coronanews.features.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.profile.repository.RepositoryProfile
import kotlinx.coroutines.launch

class ViewModelProfile(private val repositoryProfile: RepositoryProfile) : ViewModel() {

    val user: LiveData<UserEntity?>? = repositoryProfile.user
    val logout: LiveData<Boolean> = repositoryProfile.logout

    private fun fetchDataForUser() {
        viewModelScope.launch {
            repositoryProfile.getUser()
        }
    }

    fun buttonLogoutTapped() {
        repositoryProfile.logout()
    }

    fun onResume() {
        fetchDataForUser()
    }

}