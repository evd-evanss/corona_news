package com.sayhitoiot.coronanews.support

import android.app.Application
import androidx.work.Operation
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.FirebaseApp
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.commom.workmanager.WorkManagerBackgroundSync
import com.sayhitoiot.coronanews.services.SyncService
import java.util.concurrent.TimeUnit

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        configureDataBase()
        FirebaseApp.initializeApp(applicationContext)
        SyncService()
    }

    private fun configureDataBase() {
        RealmDB.configureRealm(applicationContext)
    }



}