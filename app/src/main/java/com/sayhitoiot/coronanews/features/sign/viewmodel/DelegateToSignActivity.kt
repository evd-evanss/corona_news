package com.sayhitoiot.coronanews.features.sign.viewmodel

interface DelegateToSignActivity {
    val confirmPassword: String?
    val password: String?
    val email: String?
    val name: String?
    val year: String?
    val month: String?
    val day: String?

    fun renderViewsForProgress()
    fun renderViewsForProgressDefault()
    fun showErrorInName(error: String)
    fun showErrorInDay(error: String)
    fun showErrorInMonth(error: String)
    fun showErrorInYear(error: String)
    fun showErrorInEmail(error: String)
    fun showErrorInPassword(error: String)
    fun showErrorInConfirmPassword(error: String)
    fun startActivityTerms()
    fun showAlertInTermsAnConditions()

}