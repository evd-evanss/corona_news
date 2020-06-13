package com.sayhitoiot.coronanews.features.reset.cases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class ResetUseCase {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _messageOnComplete = MutableLiveData<String?>()

    companion object {
        const val TAG = "repository-reset"
        const val SUCCESS = "Enviamos instruções no seu email para redefinir sua senha!"
        const val FAIL = "Falha ao redefinir sua senha, verifique o e-mail digitado"
    }

    fun requestForgetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                when{
                    task.isSuccessful -> _messageOnComplete.postValue(SUCCESS)
                    else -> _messageOnComplete.postValue(FAIL)
                }
                }
            )
    }

    fun getSuccessMessage(): LiveData<String?>? {
        return _messageOnComplete
    }

}