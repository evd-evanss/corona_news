package com.sayhitoiot.coronanews.features.signup.presenter

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.sayhitoiot.coronanews.entity.Account
import com.sayhitoiot.coronanews.features.signup.contract.SignUpPresenterToView
import com.sayhitoiot.coronanews.features.signup.contract.SignUpViewToPresenter

class SignUpPresenter(private val view: SignUpViewToPresenter) : SignUpPresenterToView {

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
            val account = Account(
                name = view.name,
                email = view.email,
                nascimento = "${view.day}/${view.month}/${view.year}",
                token = null
            )

            account.email?.let {
                        email -> view.confirmPassword?.let {
                        password -> createAccount(email, password)
                }
            }
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

    private fun createAccount(email: String, password: String) {

        Log.d(TAG, "email: $email senha: $password")

        view.mAuth?.createUserWithEmailAndPassword(email,password)
            ?.addOnCompleteListener() { task ->
            run {
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user: FirebaseUser? = view.mAuth?.currentUser

                    val data = ArrayList<String>()
                    data.add(email)
                    data.add(password)
                    //user?.uid
                    view.sendDataForActivityResult(data)
                } else {
                    Log.d(TAG, "createUserWithEmail:failure", task.exception?.cause)
                    if(task.exception.toString().contains("The email address is already in use") ){
                        view.showMessageOnFail("Este email está sendo usado por outra conta")
                    } else {
                        view.showMessageOnFail("Tentar novamente mais tarde")
                    }
                }
            }
        }
    }


}