package com.sayhitoiot.coronanews.features.sign.presenter

import android.util.Log
import com.sayhitoiot.coronanews.features.sign.interact.SignUpInteract
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpInteractToPresenter
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpPresenterToInteract
import com.sayhitoiot.coronanews.features.sign.presenter.contract.SignUpPresenterToView
import com.sayhitoiot.coronanews.features.sign.presenter.contract.SignUpViewToPresenter

class SignUpPresenter(private val view: SignUpViewToPresenter)
    : SignUpPresenterToView, SignUpPresenterToInteract {

    private val interact: SignUpInteractToPresenter by lazy {
        SignUpInteract(this)
    }

    companion object {
        const val TAG = "sign-presenter"
    }

    override fun onCreate() {
        view.initializeViews()
    }

    override fun buttonBackTapped() {
        view.callPreviousActivity()
    }

    override fun buttonSignTapped() {

        if(checkNameValidity() && checkDayValidity() &&
            checkMonthValidity() && checkYearValidity() && checkEmailValidity() &&
            checkPasswordValidity() && checkConfirmPasswordValidity()) {
            val birthdate = "${view.day}/${view.month}/${view.year}"
            interact.requestCreateUserOnFirebase(
                view.name!!,
                view.email!!,
                view.password!!,
                birthdate
            )
        }

    }

    private fun checkNameValidity() : Boolean {
        val nameInput = view.name
        return if(nameInput.isNullOrEmpty()){
            view.showErrorInName("O campo nome está vazio!")
            false
        } else {
            true
        }
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

    private fun checkConfirmPasswordValidity() : Boolean {
        val inputConfirm = view.confirmPassword
        val inputPassword = view.password

        return when {
            inputConfirm.isNullOrEmpty() -> {
                view.showErrorInConfirmPassword("O campo de confirmação está vazio!")
                false
            }
            inputConfirm != inputPassword -> {
                view.showErrorInConfirmPassword("As senhas não conferem")
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

    // respostas do interact
    override fun didCreateUserOnFirebaseSuccess(messageSuccess: String) {
        view.showMessageOnSuccess(messageSuccess)
    }

    override fun didCreateUserOnFirebaseFail(messageError: String) {
        view.showMessageOnFail(messageError)
    }

}