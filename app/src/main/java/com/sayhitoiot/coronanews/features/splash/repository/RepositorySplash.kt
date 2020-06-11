package com.sayhitoiot.coronanews.features.splash.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity

class RepositorySplash {

    private val _login = MutableLiveData<Boolean?>()

    val login : LiveData<Boolean?> get() = _login

    companion object {
        const val TAG = "getDataForUser"
    }

    init {
        _login.postValue(false)
    }

    fun fetchUser() {
        fetchUserOnDB()
    }

    private fun fetchUserOnDB()  {
        val user = UserEntity.getUser()
        if(user != null) {
            _login.postValue(true)
            Log.d(TAG,"Usuário ${user.name} online")
        } else {
            _login.postValue(false)
        }
    }


}