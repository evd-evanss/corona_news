package com.sayhitoiot.coronanews.features.home.profile.presenter

import android.util.Log
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
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

    override fun buttonEditTapped() {
        view.renderSecondContainer()
    }

    override fun buttonChangeTapped() {
        if(checkDayValidity() && checkMonthValidity() && checkYearValidity()) {
            interact.requestUpdateUserOnFirebase(view.name, view.day, view.month, view.year)
        }
    }

    override fun buttonBackTapped() {
        view.renderFirstContainer()
    }

    override fun didFetchUserOnDB(user: UserEntity) {
        Log.d(TAG, user.token)
        view.renderViewWithDataUser(user.name, user.email, user.birth)
    }

    override fun didFinishSignOut() {
        view.logout()
    }

    private fun checkDayValidity() : Boolean{
        val dayInput = view.day?.let { applyRegex(it) }

        return when {
            dayInput.isNullOrEmpty() -> {
                view.showErrorInDay("O campo dia está vazio!")
                false
            }
            dayInput.toInt() > 31 || dayInput.toInt() == 0 -> {
                view.showErrorInMonth("Informe um dia válido!")
                false
            }
            dayInput.length < 2 -> {
                view.showErrorInDay("Digite o dia com 2 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun checkMonthValidity() : Boolean{
        val monthInput = view.month?.let { applyRegex(it) }

        Log.d("teste-input", monthInput )

        return when {
            monthInput.isNullOrEmpty() -> {
                view.showErrorInMonth("O campo mês está vazio!")
                false
            }
            monthInput.toInt() > 12 || monthInput.toInt() == 0 -> {
                view.showErrorInMonth("Informe o mês de 1 a 12")
                false
            }
            monthInput.length < 2 -> {
                view.showErrorInMonth("Digite o ano com 2 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun checkYearValidity() : Boolean{
        val yearInput = view.year?.let { applyRegex(it) }

        return when {
            yearInput.isNullOrEmpty() -> {
                view.showErrorInYear("O campo ano está vazio!")
                false
            }
            yearInput.length < 4 -> {
                view.showErrorInYear("Digite o ano com 4 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun applyRegex(s: String): String {
        return s.replace(regex = "[^0-9]".toRegex(), replacement = "")
    }

}