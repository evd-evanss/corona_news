package com.sayhitoiot.coronanews.commom.workmanager

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.sayhitoiot.coronanews.services.SyncUser
import java.util.*

class WorkManagerBackgroundSync(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    companion object {

        const val TAG = "SYNC-WORKER"

    }

    override fun doWork(): Result {
        Log.d(TAG, "workmanager = ${Date()}")
        return try {
            SyncUser()
            Result.success()
        } catch (throwable: Throwable) {
            Result.retry()
        }
    }

}