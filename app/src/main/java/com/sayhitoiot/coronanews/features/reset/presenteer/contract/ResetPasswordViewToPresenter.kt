package com.sayhitoiot.coronanews.features.reset.presenteer.contract

interface ResetPasswordViewToPresenter {
    var email: String?
    fun initializeViews()
    fun renderMessageOnComplete(message: String)
    fun renderMessageOnError(fail: String)
    fun renderPreviousView()
}