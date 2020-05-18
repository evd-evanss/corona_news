package com.sayhitoiot.coronanews.features.login.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sayhitoiot.coronanews.features.login.interact.contract.LoginInteractToPresenter
import com.sayhitoiot.coronanews.features.login.interact.contract.LoginPresenterToInteract

class LoginInteract(private val presenter: LoginPresenterToInteract) : LoginInteractToPresenter {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    companion object {
        const val TAG = "login-interact"
        const val PASSWORD_EXCEPTION = "The password is invalid or the user does not have a password"
        const val USER_EXCEPTION = "There is no user record corresponding to this identifier. The user may have been deleted"
        const val PASSWORD_FAIL = "A senha é inválida ou o usuário não tem uma senha"
        const val USER_FAIL = "Usuário não corresponde a nenhum registro, este usuário pode ter sido excluído"
        const val NETWORK_EXCEPTION = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        const val NETWORK_FAIL = "Ocorreu um erro de rede (como tempo limite, conexão interrompida ou host inacessível)"
    }

    override fun requestLoginOnFirebase(email: String, password: String) {
        loginWithUserAndPassword(email, password)
    }

    private fun loginWithUserAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "login success")
                    presenter.didFinishLoginWithSuccess()
                } else {
                    val exception = task.exception.toString()
                    Log.d(TAG, "login fail")
                    when {
                        exception.contains(PASSWORD_EXCEPTION) -> presenter.didFinishLoginWithFail(PASSWORD_FAIL)
                        exception.contains(USER_EXCEPTION) -> presenter.didFinishLoginWithFail(USER_FAIL)
                        exception.contains(NETWORK_EXCEPTION) -> presenter.didFinishLoginWithFail(NETWORK_FAIL)
                        else -> presenter.didFinishLoginWithFail(exception)
                    }

                }
            }
    }

}
