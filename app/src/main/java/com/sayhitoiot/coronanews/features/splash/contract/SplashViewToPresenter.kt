package com.sayhitoiot.coronanews.features.splash.contract

import android.app.Activity

interface SplashViewToPresenter {
    fun loginWithUserActive()
    fun requestActivityLogin()
    val activity: Activity?
}