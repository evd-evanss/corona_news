package com.sayhitoiot.coronanews.features.sign.interact.contract

interface SignUpPresenterToInteract {
    fun didCreateUserOnFirebaseSuccess(messageSuccess: String)
    fun didCreateUserOnFirebaseFail(messageError: String)
}