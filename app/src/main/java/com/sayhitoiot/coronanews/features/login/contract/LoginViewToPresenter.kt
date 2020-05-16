package com.sayhitoiot.coronanews.features.login.contract

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface LoginViewToPresenter {

    val activity: Activity?
    var mAuth: FirebaseAuth?
    val email: String?
    val password: String?
    val currentUser: FirebaseUser?
    fun initializeViews()
    fun showErrorInEmail(messageError: String)
    fun showErrorInPassword(messageError: String)
    fun loginSuccess()
    fun loginFail(messageError: String)
    fun renderViewForLogin()
    fun startSignUpActivity()

}