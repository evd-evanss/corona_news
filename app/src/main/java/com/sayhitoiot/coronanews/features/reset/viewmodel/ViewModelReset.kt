package com.sayhitoiot.coronanews.features.reset.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.sayhitoiot.coronanews.features.reset.cases.ResetUseCase

class ViewModelReset(
    private val delegate: DelegateToResetActivity,
    private val resetUseCase: ResetUseCase) : ViewModel() {

    companion object {
        const val FAIL = "Insira o e-mail cadastrado"
    }

    val messageSuccess: LiveData<String?>? = resetUseCase.getSuccessMessage()

    fun resetButtonTapped() {
        resetEmail()
    }

    private fun resetEmail() {
        val email = delegate.email
        if(email.isNullOrEmpty()) {
            delegate.renderMessageOnError(FAIL)
        } else {
            resetUseCase.requestForgetPassword(email)
        }
    }

}