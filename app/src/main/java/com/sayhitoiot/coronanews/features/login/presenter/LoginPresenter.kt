package com.sayhitoiot.coronanews.features.login.presenter

import android.util.Log
import com.sayhitoiot.coronanews.features.login.contract.LoginPresenterToView
import com.sayhitoiot.coronanews.features.login.contract.LoginViewToPresenter

class LoginPresenter(private val view: LoginViewToPresenter) : LoginPresenterToView {

    companion object {
        const val TAG = "login-presenter"
        const val PASSWORD_EXCEPTION = "The password is invalid or the user does not have a password"
        const val USER_EXCEPTION = "There is no user record corresponding to this identifier. The user may have been deleted"
        const val PASSWORD_FAIL = "A senha é inválida ou o usuário não tem uma senha"
        const val USER_FAIL = "Usuário não corresponde a nenhum registro, este usuário pode ter sido excluído"
        const val NETWORK_EXCEPTION = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        const val NETWORK_FAIL = "Ocorreu um erro de rede (como tempo limite, conexão interrompida ou host inacessível)"
    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun btnLoginTapped() {
        if(checkEmailValidity() && checkPasswordValidity()) {
            view.renderViewForLogin()
            view.email?.let { email ->
                view.password?.let { password ->
                    loginWithUserAndPassword(email, password)
                }
            }
        }
    }

    override fun btnSignUpTapped() {
        view.startSignUpActivity()
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

    private fun loginWithUserAndPassword(email: String, password: String) {
        view.mAuth?.signInWithEmailAndPassword(email, password)
            ?.addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    view.loginSuccess()
                } else {
                    val exception = task.exception.toString()
                    when {
                        exception.contains(PASSWORD_EXCEPTION) -> view.loginFail(PASSWORD_FAIL)
                        exception.contains(USER_EXCEPTION) -> view.loginFail(USER_FAIL)
                        exception.contains(NETWORK_EXCEPTION) -> view.loginFail(NETWORK_FAIL)
                        else -> view.loginFail(exception)
                    }

                }
            }
    }

}