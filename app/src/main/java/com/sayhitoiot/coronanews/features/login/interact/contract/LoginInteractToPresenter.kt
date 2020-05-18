package com.sayhitoiot.coronanews.features.login.interact.contract

interface LoginInteractToPresenter {
    fun requestLoginOnFirebase(email: String, password: String)
}