package com.sayhitoiot.coronanews.features.sign.cases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.services.SyncUser
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.CoroutineContext

class SignUseCase() : CoroutineScope {


    override val coroutineContext: CoroutineContext get() = IO + Job()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    private val _messageSuccess = MutableLiveData<String?>()
    private val _messageFail = MutableLiveData<String?>()

    companion object {
        const val TAG = "repository-sign"
    }

    fun requestCreateUserOnFirebase(
        name: String,
        email: String,
        password: String,
        birthdate: String
    ) {
        Log.d(TAG, "email: $email senha: $password")
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener() { task ->
                run {
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val user: FirebaseUser? = mAuth.currentUser
                        val data = ArrayList<String>()
                        data.add(email)
                        data.add(password)
                        val token = user?.uid
                        createUserOnDB(name, email, birthdate, token)
                    } else {
                        Log.d(TAG, "createUserWithEmail:failure", task.exception?.cause)
                        if(task.exception.toString().contains("The email address is already in use") ){
                            _messageFail.postValue("Este email está sendo usado por outra conta")
                        } else {
                            _messageFail.postValue("Tentar novamente mais tarde")
                        }
                    }
                }
            }
    }

    private fun createUserOnDB(
        name: String,
        email: String,
        birthdate: String,
        token: String?
    ) {
        UserEntity.create (
            id = RealmDB.DEFAULT_INTEGER,
            name = name,
            email = email,
            birth = birthdate,
            token = token ?: ""
        )
        createUserOnDatabaseFirebase()
    }

    private fun createUserOnDatabaseFirebase() {
        val user = UserEntity.getUser() ?: return
        val uid = mAuth.uid ?: return
        val userReference = firebaseDatabase.reference.child(uid).child("User")
        userReference.setValue(user).addOnSuccessListener {
            createFeedOnFirebase()
        }
    }

    private fun createFeedOnFirebase() {
        val feedList = FeedEntity.getAll()
        val uid = mAuth.uid ?: return
        val feedReference = firebaseDatabase.reference.child(uid).child(SyncUser.FEEDS)

        launch {
            feedList.forEach {
                feedReference.child(it.country).setValue(it)
            }
        }

        _messageSuccess.postValue("Volte a tela de login e insira suas credenciais")

    }

    fun getSuccessMessage(): LiveData<String?>? {
        return _messageSuccess
    }

    fun getFailMessage(): LiveData<String?>? {
        return _messageFail
    }
}