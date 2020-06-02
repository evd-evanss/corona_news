package com.sayhitoiot.coronanews.features.profile.presenter.contract

interface ProfilePresenterToView {
    fun onViewCreated()
    fun buttonLogoutTapped()
    fun buttonEditTapped()
    fun buttonChangeTapped()
    fun buttonBackTapped()
}