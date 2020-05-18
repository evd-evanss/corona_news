package com.sayhitoiot.coronanews.features.login.interact.contract

interface LoginPresenterToInteract {
    fun didFinishLoginWithSuccess()
    fun didFinishLoginWithFail(passwordFail: String)
}