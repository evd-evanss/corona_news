package com.sayhitoiot.coronanews.features.sign.interact.contract

interface SignUpInteractToPresenter {
    fun requestCreateUserOnFirebase(
        name: String,
        email: String,
        password: String,
        birthdate: String
    )
}