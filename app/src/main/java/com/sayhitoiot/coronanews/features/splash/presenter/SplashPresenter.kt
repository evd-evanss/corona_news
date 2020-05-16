package com.sayhitoiot.coronanews.features.splash.presenter

import com.sayhitoiot.coronanews.features.splash.contract.SplashPresenterToView
import com.sayhitoiot.coronanews.features.splash.contract.SplashViewToPresenter
import com.sayhitoiot.coronanews.features.splash.interactor.SplashInteract
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashInteractToPresenter
import com.sayhitoiot.coronanews.features.splash.interactor.contract.SplashPresenterToInteract

class SplashPresenter(private var view: SplashViewToPresenter)
    : SplashPresenterToView, SplashPresenterToInteract {

    private val interact: SplashInteractToPresenter by lazy {
        SplashInteract(this)
    }

    override fun onCreate() {
        view.configureDataBase()
        interact.fetchUser()
    }

    override fun didFinishFetchUserOnDB() {
        view.loginWithUserActive()
    }

    override fun requestLogin() {
        view.requestActivityLogin()
    }

}