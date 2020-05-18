package com.sayhitoiot.coronanews.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.firebase.model.User
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.UserEntity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import kotlin.coroutines.CoroutineContext

class SyncService : CoroutineScope{

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()

    companion object {

        const val TAG = "sync-service"
        const val USER = "User"
        const val FEEDS = "Feeds"

    }

    init {
        this.syncUser()
        //this.syncFeed()
    }

    private fun syncUser() {
        fetchUser()
    }

    private fun syncFeed() {
        GlobalScope.launch {
            fetchFeedOnFirebase()
        }
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

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to sync user.", error.toException())
            }
        })

    }

    private fun fetchFeedOnFirebase() {

        val uid = mAuth.uid
        val userReference = uid?.let { firebaseDatabase.getReference(it).child(FEEDS) }
        userReference?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val response =
                    dataSnapshot.children

                response.forEach {
                    val feed : Feed? = it.getValue(Feed::class.java)

                    if(feed != null) {
                        FeedEntity.create(
                            feed.country,
                            feed.day,
                            feed.cases,
                            feed.recovereds,
                            feed.deaths,
                            feed.newCases,
                            feed.favorite
                        )
                    }
                    Log.d(TAG, "Sync $feed success")
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to sync user.", error.toException())
            }
        })

    }

    override val coroutineContext: CoroutineContext
        get() = Default + Job()

}

