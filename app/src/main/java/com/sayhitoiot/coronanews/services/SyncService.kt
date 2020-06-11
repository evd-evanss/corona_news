package com.sayhitoiot.coronanews.services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsByStatesCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.OnGetStatisticsCoronaCallback
import com.sayhitoiot.coronanews.commom.apicovid.model.Response
import com.sayhitoiot.coronanews.commom.apicovid.model.ResultData
import com.sayhitoiot.coronanews.commom.apicovid.model_states.ResultOfStates
import com.sayhitoiot.coronanews.commom.apicovid.repository.ApiDataManager
import com.sayhitoiot.coronanews.commom.apicovid.repository.InteractToApi
import com.sayhitoiot.coronanews.commom.extensions.toLocale
import com.sayhitoiot.coronanews.commom.firebase.model.Feed
import com.sayhitoiot.coronanews.commom.firebase.model.User
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.realm.entity.FeedEntity
import com.sayhitoiot.coronanews.commom.realm.entity.FilterEntity
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
        fetchUser()
        fetchFilters()
    }

    private fun fetchUser() {
        if(UserEntity.getUser() == null) {
            fetchUserOnFirebase()
        }
    }

    private fun fetchFilters() {
        if(FilterEntity.getFilter() == null) {
            createFilterOnDB()
        }
    }

    private fun createFilterOnDB() {
        FilterEntity.create(0)
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

    fun syncApiCovid() {

        repository.getStatistics(object : OnGetStatisticsCoronaCallback {

            override fun onSuccess(coronaResult: ResultData) {
                Log.d(TAG, coronaResult.results.toString())
                response.addAll(coronaResult.response)
                saveFeedOnFirebase(response)
            }

            override fun onError() {
                Log.e(TAG, "Error on fetch data")
            }
        })

        repository.getStatisticsByStates(object : OnGetStatisticsByStatesCoronaCallback {
            override fun onSuccess(coronaResult: ResultOfStates) {

            }

            override fun onError() {

            }

        })

    }

    private fun saveFeedOnFirebase(response: MutableList<Response>) {
        val uid = mAuth.uid

        val feedFirebaseModel: MutableList<Feed> = mutableListOf()

        response.forEach {
            feedFirebaseModel.add(
                Feed (
                    cases = it.cases.total,
                    country = it.country,
                    day = it.day.toLocale(),
                    deaths = it.deaths.total,
                    newCases = it.cases.new,
                    recoveries = it.cases.recovered,
                    favorite = false
                )
            )
        }

        feedFirebaseModel.forEach {
            uid?.let { it1 ->
                firebaseDatabase.reference.child(it1).child(FEEDS).child(it.country)
                    .setValue(it)
            }
        }

        saveFeedOnDB(feedFirebaseModel)

    }

    private fun saveFeedOnDB(results: MutableList<Feed>) {

        if(FeedEntity.getAll().isEmpty()) {
            for(feed in results) {
                Log.d(TAG, feed.country)
                FeedEntity.create (
                    country = feed.country,
                    day = feed.day ?: "",
                    cases = feed.cases,
                    recoveries = feed.recoveries,
                    deaths = feed.deaths,
                    newCases = feed.newCases,
                    favorite = feed.favorite
                )
            }
        } else {
            for(feed in results) {
                FeedEntity.update(
                    country = feed.country,
                    day = feed.day
                )
            }
        }
    }


}

