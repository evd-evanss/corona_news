package com.sayhitoiot.coronanews.features.login.viewmodel

interface DelegateToLoginActivity {
    val password: String?
    val email: String?
    fun showErrorInEmail(error: String)
    fun showErrorInPassword(error: String)
    fun renderViewForLogin()
    fun renderViewForResetPassword()
    fun startSignUpActivity()
}