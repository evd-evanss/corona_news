package com.sayhitoiot.coronanews.features.splash.interactor

import android.util.Log
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashInteractToPresenter
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashPresenterToInteract

class SplashInteract(private val presenter: SplashPresenterToInteract)
    : SplashInteractToPresenter {

    companion object {
        const val TAG = "splash-interactor"
    }

    override fun fetchUser() {
        fetchUserOnDB()
    }

    private fun fetchUserOnDB()  {
        val user = UserEntity.getUser()
        if(user != null) {
            presenter.didFinishFetchUserOnDB()
            Log.d(TAG,"Usuário ${user.name} online")
        } else {
            presenter.requestLogin()
        }

    }

}