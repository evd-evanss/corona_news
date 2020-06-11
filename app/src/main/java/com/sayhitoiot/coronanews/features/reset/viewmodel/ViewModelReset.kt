package com.sayhitoiot.coronanews.features.reset.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sayhitoiot.coronanews.features.reset.repository.RepositoryReset

class ViewModelReset(
    private val delegate: DelegateToResetActivity,
    private val repositoryReset: RepositoryReset) : ViewModel() {

    companion object {
        const val FAIL = "Insira o e-mail cadastrado"
    }

    val messageSuccess: LiveData<String?>? = repositoryReset.getSuccessMessage()

    fun resetButtonTapped() {
        resetEmail()
    }

    private fun resetEmail() {
        val email = delegate.email
        if(email.isNullOrEmpty()) {
            delegate.renderMessageOnError(FAIL)
        } else {
            repositoryReset.requestForgetPassword(email)
        }
    }

}