package com.sayhitoiot.coronanews.features.home.profile.presenter.contract

import android.app.Activity

interface ProfileViewToPresenter {
    val activity: Activity?
    fun initializeViews()
    fun renderViewWithDataUser(name: String, email: String, birth: String)
    fun logout()
}