package com.sayhitoiot.coronanews.features.login.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import kotlinx.coroutines.delay

class RepositoryLogin {

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _message = MutableLiveData<String?>()
    private val _login = MutableLiveData<Boolean>()

    val login : LiveData<Boolean> get() = _login

    companion object {
        const val TAG = "getDataForUser"
        const val PASSWORD_EXCEPTION = "The password is invalid or the user does not have a password"
        const val USER_EXCEPTION = "There is no user record corresponding to this identifier. The user may have been deleted"
        const val PASSWORD_FAIL = "A senha é inválida ou o usuário não tem uma senha"
        const val USER_FAIL = "Usuário não corresponde a nenhum registro, este usuário pode ter sido excluído"
        const val NETWORK_EXCEPTION = "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
        const val NETWORK_FAIL = "Ocorreu um erro de rede (como tempo limite, conexão interrompida ou host inacessível)"
    }

    fun login(email: String, password: String) {
        loginWithUserAndPassword(email, password)
    }

    private fun loginWithUserAndPassword(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "login success")
                    _login.postValue(true)
                    _message.postValue(null)
                } else {
                    _login.postValue(false)
                    val exception = task.exception.toString()
                    Log.d(TAG, "login fail")
                    when {
                        exception.contains(PASSWORD_EXCEPTION) -> _message.postValue(PASSWORD_FAIL)
                        exception.contains(USER_EXCEPTION) -> _message.postValue(USER_FAIL)
                        exception.contains(NETWORK_EXCEPTION) -> _message.postValue(NETWORK_FAIL)
                        else -> _message.postValue(null)
                    }
                }
            }
    }

    fun getFail(): LiveData<String?>? {
        return _message
    }


}