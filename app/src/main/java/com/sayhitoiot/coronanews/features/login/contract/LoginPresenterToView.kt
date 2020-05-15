package com.sayhitoiot.coronanews.features.login.contract

interface LoginPresenterToView {
    fun onCreate()
    fun btnLoginTapped()
    fun loginByActivityResult(emailByActivityResult: String?,passwordByActivityResult: String?)
    fun btnSignUpTapped()
    fun onStart()
}