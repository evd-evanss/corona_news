package com.sayhitoiot.coronanews.features.login.presenter

import com.sayhitoiot.coronanews.features.login.contract.LoginPresenterToView
import com.sayhitoiot.coronanews.features.login.contract.LoginViewToPresenter
import com.sayhitoiot.coronanews.features.login.interact.LoginInteract
import com.sayhitoiot.coronanews.features.login.interact.contract.LoginInteractToPresenter
import com.sayhitoiot.coronanews.features.login.interact.contract.LoginPresenterToInteract

class LoginPresenter(private val view: LoginViewToPresenter)
    : LoginPresenterToView, LoginPresenterToInteract {

    private val interact: LoginInteractToPresenter by lazy {
        LoginInteract(this)
    }

    companion object {

        const val TAG = "login-presenter"

    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun btnLoginTapped() {
        if(checkEmailValidity() && checkPasswordValidity()) {
            view.renderViewForLogin()
            view.email?.let { email->
                view.password?.let { password->
                    interact.requestLoginOnFirebase(
                        email,
                        password
                    )
                }
            }
        }
    }

    override fun btnSignUpTapped() {
        view.startSignUpActivity()
    }

    override fun forgetPasswordTapped() {
        view.renderViewForResetPassword()
    }

    private fun checkEmailValidity() : Boolean {
        val emailInput = view.email

        if(emailInput.isNullOrEmpty()) {
            view.showErrorInEmail("O campo e-mail está vazio!")
        } else {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                return true
            } else {
                view.showErrorInEmail("E-mail inválido!")
            }
        }
        return false
    }

    private fun checkPasswordValidity() : Boolean {
        val inputPassword = view.password

        return when {
            inputPassword.isNullOrEmpty() -> {
                view.showErrorInPassword("O campo senha está vazio!")
                false
            }
            inputPassword.length < 5 -> {
                view.showErrorInPassword("A senha precisa ter no mínimo 5 dígitos")
                false
            }
            else -> {
                true
            }
        }

    }

    override fun didFinishLoginWithSuccess() {
        view.loginSuccess()
    }

    override fun didFinishLoginWithFail(passwordFail: String) {
        view.loginFail(passwordFail)
    }

}