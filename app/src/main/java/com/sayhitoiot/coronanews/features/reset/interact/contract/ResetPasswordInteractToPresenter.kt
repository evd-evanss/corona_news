package com.sayhitoiot.coronanews.features.reset.interact.contract

interface ResetPasswordInteractToPresenter {
    fun requestForgetPassword(email: String)
}