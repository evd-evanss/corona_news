package com.sayhitoiot.coronanews.features.reset.presenteer

import com.sayhitoiot.coronanews.features.reset.interact.ResetPasswordInteract
import com.sayhitoiot.coronanews.features.reset.interact.contract.ResetPasswordInteractToPresenter
import com.sayhitoiot.coronanews.features.reset.interact.contract.ResetPasswordPresenterToInteract
import com.sayhitoiot.coronanews.features.reset.presenteer.contract.ResetPasswordPresenterToView
import com.sayhitoiot.coronanews.features.reset.presenteer.contract.ResetPasswordViewToPresenter

class ResetPasswordPresenter(private val view: ResetPasswordViewToPresenter) : ResetPasswordPresenterToView,
    ResetPasswordPresenterToInteract {

    private val interact: ResetPasswordInteractToPresenter by lazy {
        ResetPasswordInteract(this)
    }

    companion object {
        const val TAG = "reset-presenter"
        const val FAIL = "Insira o e-mail cadastrado"
    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun resetButtonTapped() {
        resetEmail()
    }

    private fun resetEmail() {
        val email = view.email
        if(email.isNullOrEmpty()) {
            view.renderMessageOnError(FAIL)
        } else {
            interact.requestForgetPassword(email)
        }
    }

    override fun showMessage(message: String) {
        view.renderMessageOnComplete(message)
    }

    override fun buttonBackTapped() {
        view.renderPreviousView()
    }
}