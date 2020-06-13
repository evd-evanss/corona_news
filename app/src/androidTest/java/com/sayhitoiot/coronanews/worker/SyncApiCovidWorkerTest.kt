package com.sayhitoiot.coronanews.worker

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.work.ListenableWorker
import androidx.work.testing.TestListenableWorkerBuilder
import org.hamcrest.core.Is
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class SyncApiCovidWorkerTest {
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }
    @Test
    fun testMyWork() {
        // Get the ListenableWorker
        val worker =
            TestListenableWorkerBuilder <SyncApiCovidWorker>(context).build()
        // Run the worker synchronously
        val result = worker.startWork().get()
        assertThat(result, `is`(ListenableWorker.Result.success()))
    }
}