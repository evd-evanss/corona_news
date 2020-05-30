package com.sayhitoiot.coronanews.features.reset.interact

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.features.reset.interact.contract.ResetPasswordInteractToPresenter
import com.sayhitoiot.coronanews.features.reset.interact.contract.ResetPasswordPresenterToInteract

class ResetPasswordInteract(private val presenter: ResetPasswordPresenterToInteract)
    : ResetPasswordInteractToPresenter {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        const val TAG = "reset-interact"
        const val SUCCESS = "Enviamos instruções no seu email para redefinir sua senha!"
        const val FAIL = "Falha ao redefinir sua senha, verifique o e-mail digitado"
    }

    override fun requestForgetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                when{
                    task.isSuccessful -> presenter.showMessage(SUCCESS)
                    else -> presenter.showMessage(FAIL)
                }
            })
    }
}