package com.sayhitoiot.coronanews.features.reset.viewmodel

interface DelegateToResetActivity {
    val email: String?
    fun renderMessageOnError(fail: String)
}