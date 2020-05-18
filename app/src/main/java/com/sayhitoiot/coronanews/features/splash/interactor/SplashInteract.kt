package com.sayhitoiot.coronanews.features.splash.interactor

import android.util.Log
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashInteractToPresenter
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashPresenterToInteract
import com.sayhitoiot.coronanews.support.App

class SplashInteract(private val presenter: SplashPresenterToInteract)
    : SplashInteractToPresenter {

    companion object {
        const val TAG = "splash-interactor"
    }

    override fun initializeSyncService() {
        //App.runSyncService()
        presenter.didFinishInitializeSyncService()
    }

    override fun fetchUser() {
        fetchUserOnDB()
    }

    private fun fetchUserOnDB()  {
        val user = UserEntity.getUser()
        if(user != null) {
            //Se usuário existe entra na tela home
            presenter.didFinishFetchUserOnDB()
            Log.d(TAG,"Usuário ${user.name} online")
        } else {
            presenter.requestLogin()
        }

    }



}