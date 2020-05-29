package com.sayhitoiot.coronanews.features.sign.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpInteractToPresenter
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpPresenterToInteract
import com.sayhitoiot.coronanews.features.sign.presenter.SignUpPresenter
import com.sayhitoiot.coronanews.services.SyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SignUpInteract(private var presenter: SignUpPresenterToInteract) : SignUpInteractToPresenter,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = IO + Job()
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun requestCreateUserOnFirebase(
        name: String,
        email: String,
        password: String,
        birthdate: String
    ) {
        Log.d(SignUpPresenter.TAG, "email: $email senha: $password")
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener() { task ->
                run {
                    if (task.isSuccessful) {
                        Log.d(SignUpPresenter.TAG, "createUserWithEmail:success")
                        val user: FirebaseUser? = mAuth.currentUser
                        val data = ArrayList<String>()
                        data.add(email)
                        data.add(password)
                        val token = user?.uid
                        createUserOnDB(name, email, birthdate, token)
                    } else {
                        Log.d(SignUpPresenter.TAG, "createUserWithEmail:failure", task.exception?.cause)
                        if(task.exception.toString().contains("The email address is already in use") ){
                            presenter.didCreateUserOnFirebaseFail("Este email está sendo usado por outra conta")
                        } else {
                            presenter.didCreateUserOnFirebaseFail("Tentar novamente mais tarde")
                        }
                    }
                }
            }
    }

    private fun createUserOnFirebase() {
        val user = UserEntity.getUser() ?: return
        val uid = mAuth.uid ?: return
        val userReference = firebaseDatabase.reference.child(uid).child("User")
        userReference.setValue(user).addOnSuccessListener {
            createFeedOnFirebase()
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
        createUserOnFirebase()
    }

    private fun createFeedOnFirebase() {
        val feedList = FeedEntity.getAll()
        val uid = mAuth.uid ?: return
        val feedReference = firebaseDatabase.reference.child(uid).child(SyncService.FEEDS)

        launch {
            feedList.forEach {
                feedReference.child(it.country).setValue(it).addOnSuccessListener {
                    presenter.didCreateUserOnFirebaseSuccess("Volte a tela de login e insira suas credenciais")
                }
            }
        }


    }

}