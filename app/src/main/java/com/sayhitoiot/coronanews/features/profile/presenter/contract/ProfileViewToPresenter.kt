package com.sayhitoiot.coronanews.features.profile.presenter.contract

import android.app.Activity

interface ProfileViewToPresenter {
    val name: String?
    val day: String?
    val month: String?
    val year: String?
    val activity: Activity?
    fun initializeViews()
    fun renderViewWithDataUser(name: String, email: String, birth: String)
    fun logout()
    fun renderSecondContainer()
    fun showErrorInDay(messageError: String)
    fun showErrorInMonth(messageError: String)
    fun showErrorInYear(messageError: String)
    fun renderFirstContainer()
}