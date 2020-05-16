package com.sayhitoiot.coronanews.features.sign.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.sayhitoiot.coronanews.commom.RealmDB
import com.sayhitoiot.coronanews.commom.entity.UserEntity
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpInteractToPresenter
import com.sayhitoiot.coronanews.features.sign.interact.contract.SignUpPresenterToInteract
import com.sayhitoiot.coronanews.features.sign.presenter.SignUpPresenter

class SignUpInteract(private var presenter: SignUpPresenterToInteract) : SignUpInteractToPresenter {

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
                        presenter.didCreateUserOnFirebaseSuccess("Volte a tela de login e insira suas credenciais")
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
        userReference.setValue(user)
    }

    private fun createUserOnDB(
        name: String,
        email: String,
        birthdate: String,
        token: String?
    ) {
        UserEntity.create(
            id = RealmDB.DEFAULT_INTEGER,
            name = name,
            email = email,
            birth = birthdate,
            token = token ?: ""
        )
        createUserOnFirebase()
    }

}