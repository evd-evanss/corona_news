package com.sayhitoiot.coronanews.commom.realm.migration

import android.util.Log
import io.realm.DynamicRealm
import io.realm.RealmMigration
import java.lang.Exception

class RealmDatabaseMigration : RealmMigration {

    companion object {

        const val TAG = "REALM-MIGRATION"

    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {

        try { } catch (e: Exception) {
            Log.e(TAG, "Database migration with old version $oldVersion failed")
        }

    }
}