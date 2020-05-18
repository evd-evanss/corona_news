package com.sayhitoiot.coronanews.features.splash.interactor.contract

interface SplashPresenterToInteract {
    fun didFinishFetchUserOnDB()
    fun requestLogin()
    fun didFinishInitializeSyncService()
}