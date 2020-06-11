package com.sayhitoiot.coronanews.features.sign.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sayhitoiot.coronanews.features.sign.repository.RepositorySign

class ViewModelSign(
    private val delegate: DelegateToSignActivity,
    private val repositorySign: RepositorySign) : ViewModel() {

    var accept: Boolean = false
    val messageSuccess: LiveData<String?>? = repositorySign.getSuccessMessage()
    val messageFail: LiveData<String?>? = repositorySign.getFailMessage()

    fun buttonTermsTapped() {
        delegate.startActivityTerms()
    }

    fun buttonSignTapped() {

        if(checkNameValidity() && checkDayValidity() &&
            checkMonthValidity() && checkYearValidity() && checkEmailValidity() &&
            checkPasswordValidity() && checkConfirmPasswordValidity() && checkAcceptTerms()) {
            delegate.renderViewsForProgress()
            val birthdate = "${delegate.day}/${delegate.month}/${delegate.year}"
            repositorySign.requestCreateUserOnFirebase(
                delegate.name!!,
                delegate.email!!,
                delegate.password!!,
                birthdate
            )
        } else {
            delegate.renderViewsForProgressDefault()
        }

    }

    private fun checkAcceptTerms() : Boolean {
        if(!accept) {
            delegate.showAlertInTermsAnConditions()
        }
        return accept
    }

    private fun checkNameValidity() : Boolean {
        val nameInput = delegate.name
        return if(nameInput.isNullOrEmpty()){
            delegate.showErrorInName("O campo nome está vazio!")
            false
        } else {
            true
        }
    }

    private fun checkDayValidity() : Boolean{
        val dayInput = delegate.day?.let { applyRegex(it) }

        return when {
            dayInput.isNullOrEmpty() -> {
                delegate.showErrorInDay("O campo dia está vazio!")
                false
            }
            dayInput.toInt() > 31 || dayInput.toInt() == 0 -> {
                delegate.showErrorInMonth("Informe um dia válido!")
                false
            }
            dayInput.length < 2 -> {
                delegate.showErrorInDay("Digite o dia com 2 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun checkMonthValidity() : Boolean{
        val monthInput = delegate.month?.let { applyRegex(it) }

        Log.d("teste-input", "$monthInput" )

        return when {
            monthInput.isNullOrEmpty() -> {
                delegate.showErrorInMonth("O campo mês está vazio!")
                false
            }
            monthInput.toInt() > 12 || monthInput.toInt() == 0 -> {
                delegate.showErrorInMonth("Informe o mês de 1 a 12")
                false
            }
            monthInput.length < 2 -> {
                delegate.showErrorInMonth("Digite o ano com 2 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun checkYearValidity() : Boolean{
        val yearInput = delegate.year?.let { applyRegex(it) }

        return when {
            yearInput.isNullOrEmpty() -> {
                delegate.showErrorInYear("O campo ano está vazio!")
                false
            }
            yearInput.length < 4 -> {
                delegate.showErrorInYear("Digite o ano com 4 dígitos!")
                false
            }
            else -> {
                true
            }
        }

    }

    private fun checkEmailValidity() : Boolean {
        val emailInput = delegate.email

        if(emailInput.isNullOrEmpty()) {
            delegate.showErrorInEmail("O campo e-mail está vazio!")
        } else {
            if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()){
                return true
            } else {
                delegate.showErrorInEmail("E-mail inválido!")
            }
        }
        return false
    }

    private fun checkPasswordValidity() : Boolean {
        val inputPassword = delegate.password

        return when {
            inputPassword.isNullOrEmpty() -> {
                delegate.showErrorInPassword("O campo senha está vazio!")
                false
            }
            inputPassword.length < 5 -> {
                delegate.showErrorInPassword("A senha precisa ter no mínimo 5 dígitos")
                false
            }
            else -> {
                true
            }
        }


    }

    private fun checkConfirmPasswordValidity() : Boolean {
        val inputConfirm = delegate.confirmPassword
        val inputPassword = delegate.password

        return when {
            inputConfirm.isNullOrEmpty() -> {
                delegate.showErrorInConfirmPassword("O campo de confirmação está vazio!")
                false
            }
            inputConfirm != inputPassword -> {
                delegate.showErrorInConfirmPassword("As senhas não conferem")
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