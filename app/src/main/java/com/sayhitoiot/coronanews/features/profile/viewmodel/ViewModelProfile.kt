package com.sayhitoiot.coronanews.features.profile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.profile.cases.ProfileUseCase
import kotlinx.coroutines.launch

class ViewModelProfile(private val profileUseCase: ProfileUseCase) : ViewModel() {

    val user: LiveData<UserEntity?>? = profileUseCase.user
    val logout: LiveData<Boolean> = profileUseCase.logout
    val delete: LiveData<Boolean> = profileUseCase.delete
    val failure: LiveData<Boolean> = profileUseCase.failure

    private fun fetchDataForUser() {
        viewModelScope.launch {
            profileUseCase.getUser()
        }
    }

    fun buttonLogoutTapped() {
        profileUseCase.logout()
    }

    fun onResume() {
        fetchDataForUser()
    }

    fun buttonDeleteTapped() {
        profileUseCase.deleteAccount()
    }

}