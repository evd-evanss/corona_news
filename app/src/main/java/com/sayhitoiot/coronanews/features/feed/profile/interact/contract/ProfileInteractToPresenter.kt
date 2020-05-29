package com.sayhitoiot.coronanews.features.feed.profile.interact.contract

interface ProfileInteractToPresenter {
    fun requestLogout()
    fun fetchUser()
    fun requestUpdateUserOnFirebase(name: String?, day: String?, month: String?, year: String?)
}