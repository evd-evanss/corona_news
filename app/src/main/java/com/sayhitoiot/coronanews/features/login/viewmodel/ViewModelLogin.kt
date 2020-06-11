package com.sayhitoiot.coronanews.features.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sayhitoiot.coronanews.features.login.repository.RepositoryLogin
import kotlinx.coroutines.launch

class ViewModelLogin(private val delegate: DelegateToLoginActivity, private val repositoryLogin: RepositoryLogin) : ViewModel() {

    val message: LiveData<String?>? = repositoryLogin.getFail()
    val login: LiveData<Boolean> = repositoryLogin.login

    fun btnLoginTapped() {
        viewModelScope.launch {
            if(checkEmailValidity() && checkPasswordValidity()) {
                delegate.renderViewForLogin()
                delegate.email?.let { email->
                    delegate.password?.let { password->
                        repositoryLogin.login(email, password)
                    }
                }
            }
        }
    }

    fun forgetPasswordTapped() {
        delegate.renderViewForResetPassword()
    }

    fun btnSignUpTapped() {
        delegate.startSignUpActivity()
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

}