package com.sayhitoiot.coronanews.features.sign.presenter.contract

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth

interface SignUpViewToPresenter {
    val activity: Activity?
    var mAuth: FirebaseAuth?
    var name: String?
    var day: String?
    var month: String?
    var year: String?
    var email: String?
    var password: String?
    var confirmPassword: String?
    fun initializeViews()
    fun callPreviousActivity()
    fun showErrorInEmail(messageError: String)
    fun showErrorInName(messageError: String)
    fun showErrorInDay(messageError: String)
    fun showErrorInMonth(messageError: String)
    fun showErrorInYear(messageError: String)
    fun showErrorInConfirmPassword(messageError: String)
    fun showErrorInPassword(messageError: String)
    fun showMessageOnSuccess(messageSuccess: String)
    fun showMessageOnFail(messageError: String)
}