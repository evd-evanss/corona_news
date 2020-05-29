package com.sayhitoiot.coronanews.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.apicovid.model.Response
import com.sayhitoiot.coronanews.commom.apicovid.repository.ApiDataManager
import com.sayhitoiot.coronanews.commom.apicovid.repository.InteractToApi
import com.sayhitoiot.coronanews.commom.firebase.model.User
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.coroutines.CoroutineContext

class SyncService : CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = IO + Job()

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    private val repository: InteractToApi = ApiDataManager()
    private val response: MutableList<Response> = mutableListOf()


    companion object {

        const val TAG = "sync-service"
        const val USER = "User"
        const val FEEDS = "Feeds"

    }

    init {
        this.syncUser()
    }

    private fun syncUser() {
        fetchUser()
    }

    private fun fetchUser() {
        if(UserEntity.getUser() == null) {
            fetchUserOnFirebase()
        }
    }

    private fun fetchUserOnFirebase() {
        val uid = mAuth.uid ?: return
        val userReference = firebaseDatabase.getReference(uid).child(USER)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val firebaseUser: User? = dataSnapshot.getValue(
                    User::class.java)
                firebaseUser ?: return

                launch {
                    UserEntity.create(
                        id = RealmDB.DEFAULT_INTEGER,
                        name = firebaseUser.name,
                        email = firebaseUser.email,
                        birth = firebaseUser.birth,
                        token = firebaseUser.token
                    )
                    Log.d(
                        TAG,
                        "Success to sync user:\n" +
                                "id:${firebaseUser.id}\n"+
                                "name:${firebaseUser.name}\n"+
                                "email:${firebaseUser.email}\n"+
                                "birthdate:${firebaseUser.birth}\n"+
                                "token:${firebaseUser.token}"
                    )
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to sync user.", error.toException())
            }
        })

    }


}

