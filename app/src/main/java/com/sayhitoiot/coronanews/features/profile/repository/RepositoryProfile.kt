package com.sayhitoiot.coronanews.features.profile.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.firebase.model.User
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RepositoryProfile(private val ioDispatcher: CoroutineDispatcher) : CoroutineScope {

    override val coroutineContext: CoroutineContext get() = ioDispatcher + Job()
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    private val _user = MutableLiveData<UserEntity?>()
    private val _logout = MutableLiveData<Boolean>()

    val user : LiveData<UserEntity?> get() = _user
    val logout : LiveData<Boolean> get() = _logout

    companion object {
        const val TAG = "getDataForUser"
    }

    init {
        _logout.postValue(false)
    }

    fun getUser() {
        val userEntity = UserEntity.getUser()
        if(userEntity == null) {
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
                fetchUserOnDB()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun fetchUserOnDB() {
        val userEntity = UserEntity.getUser()
        _user.postValue(userEntity)
    }

    fun logout() {
        mAuth.signOut()
        RealmDB.clearDatabase()
        _logout.postValue(true)
    }

}