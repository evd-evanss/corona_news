package com.sayhitoiot.coronanews.commom

import android.content.Context
import com.sayhitoiot.coronanews.commom.entity.UserEntity
import com.sayhitoiot.coronanews.commom.migration.RealmDatabaseMigration
import io.realm.Realm
import io.realm.RealmConfiguration


object RealmDB {

    const val DEFAULT_INTEGER = 0

    fun configureRealm(context: Context) {
        Realm.init(context)

        val mRealmConfiguration = RealmConfiguration.Builder()
            .name("smnow-teste-database.realm")
            .schemaVersion(0L)
            .build()

        Realm.getInstance(mRealmConfiguration)
        Realm.setDefaultConfiguration(mRealmConfiguration)

    }

    fun clearDatabase() {
        UserEntity.delete()
    }

}