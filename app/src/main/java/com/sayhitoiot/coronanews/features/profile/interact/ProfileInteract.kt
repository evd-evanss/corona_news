package com.sayhitoiot.coronanews.features.profile.interact

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import com.sayhitoiot.coronanews.commom.firebase.model.User
import com.sayhitoiot.coronanews.features.profile.interact.contract.ProfileInteractToPresenter
import com.sayhitoiot.coronanews.features.profile.interact.contract.ProfilePresenterToInteract


class ProfileInteract(private val presenter: ProfilePresenterToInteract) : ProfileInteractToPresenter {

    companion object {
        const val TAG = "profile-interact"
    }

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    override fun fetchUser() {
        if(UserEntity.getUser() == null) {
            fetchUserOnFirebase()
        } else {
            fetchUserOnDB()
        }
    }

    private fun fetchUserOnFirebase() {
        val uid = mAuth.uid ?: return
        val userReference = firebaseDatabase.getReference(uid).child("User")

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firebaseUser: User? = dataSnapshot.getValue(
                    User::class.java)
                firebaseUser ?: return
                UserEntity.create(
                    id = RealmDB.DEFAULT_INTEGER,
                    name = firebaseUser.name,
                    email = firebaseUser.email,
                    birth = firebaseUser.birth,
                    token = firebaseUser.token
                )
                Log.d(
                    TAG,
                    "id:${firebaseUser.id}\n"+
                    "name:${firebaseUser.name}\n"+
                    "email:${firebaseUser.email}\n"+
                    "birthdate:${firebaseUser.birth}\n"+
                    "token:${firebaseUser.token}"
                )
                fetchUserOnDB()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchUserOnDB() {
        val user = UserEntity.getUser()
        if(user != null){
            presenter.didFetchUserOnDB(user)
            Log.d(TAG, user.name)
        } else {
            Log.d(TAG, "Não há usuário")
        }

    }

    override fun requestUpdateUserOnFirebase(
        name: String?,
        day: String?,
        month: String?,
        year: String?
    ) {
        val user = User()
        user.name = "$name"
        user.birth = "${day}/${month}/${year}"
        updateUserOnDB(user)
    }

    private fun updateUserOnDB(user: User) {
        UserEntity.update(
            name = user.name,
            birthdate = user.birth
        )
        fetchUserOnDB()
    }

    override fun requestLogout() {
        mAuth.signOut()
        RealmDB.clearDatabase()
        presenter.didFinishSignOut()
    }
}