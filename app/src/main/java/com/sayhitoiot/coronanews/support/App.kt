package com.sayhitoiot.coronanews.support

import android.app.Application
import androidx.work.*
import com.google.firebase.FirebaseApp
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.services.SyncUser
import com.sayhitoiot.coronanews.worker.SyncApiCovidWorker
import java.util.concurrent.TimeUnit

class App: Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        configureDataBase()
        FirebaseApp.initializeApp(applicationContext)
        SyncUser()
        setupWorker()
    }

    private fun configureDataBase() {
        RealmDB.configureRealm(applicationContext)
    }

    private fun setupWorker() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val work = PeriodicWorkRequestBuilder<SyncApiCovidWorker>(
            15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(work)
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

}