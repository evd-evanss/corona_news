package com.sayhitoiot.coronanews.features.home.profile.interact.contract

interface ProfileInteractToPresenter {
    fun requestLogout()
    fun fetchUser()
    fun requestUpdateUserOnFirebase(name: String?, day: String?, month: String?, year: String?)
}