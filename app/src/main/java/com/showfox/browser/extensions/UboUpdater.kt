package com.showfox.browser.extensions

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object UboUpdater {

    private const val TAG = "UboUpdater"
    private const val WORK_NAME = "ubo_filter_update"

    fun schedule(context: Context) {
        val request = PeriodicWorkRequestBuilder<UboUpdateWorker>(1, TimeUnit.DAYS)
            .addTag(WORK_NAME)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
        Log.d(TAG, "Scheduled uBO filter list updates (daily)")
    }

    fun cancel(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        Log.d(TAG, "Cancelled uBO filter list updates")
    }
}
