package com.sayhitoiot.coronanews.support

import android.app.Application
import com.sayhitoiot.coronanews.commom.realm.RealmDB
import com.sayhitoiot.coronanews.services.SyncService

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        configureDataBase()
        SyncService()
    }

    private fun configureDataBase() {
        RealmDB.configureRealm(applicationContext)
    }



}