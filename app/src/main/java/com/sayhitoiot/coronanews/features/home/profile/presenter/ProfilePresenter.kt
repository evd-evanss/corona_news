package com.sayhitoiot.coronanews.features.home.profile.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.commom.entity.UserEntity
import com.sayhitoiot.coronanews.features.home.profile.interact.ProfileInteract
import com.sayhitoiot.coronanews.features.home.profile.interact.contract.ProfileInteractToPresenter
import com.sayhitoiot.coronanews.features.home.profile.interact.contract.ProfilePresenterToInteract
import com.sayhitoiot.coronanews.features.home.profile.presenter.contract.ProfilePresenterToView
import com.sayhitoiot.coronanews.features.home.profile.presenter.contract.ProfileViewToPresenter

class ProfilePresenter(private val view: ProfileViewToPresenter)
    : ProfilePresenterToView, ProfilePresenterToInteract {

    companion object {
        const val TAG = "profile-presenter"
    }

    private val interact: ProfileInteractToPresenter by lazy {
        ProfileInteract(this)
    }

    override fun onViewCreated() {
        view.initializeViews()
        interact.fetchUser()
    }

    override fun buttonLogoutTapped() {
        interact.requestLogout()
    }

    override fun didFetchUserOnDB(user: UserEntity) {
        Log.d(TAG, user.token)
        view.renderViewWithDataUser(user.name, user.email, user.birth)
    }

    override fun didFinishSignOut() {
        view.logout()
    }

}