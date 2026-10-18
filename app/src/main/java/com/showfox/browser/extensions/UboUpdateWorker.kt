package com.showfox.browser.extensions

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class UboUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Log.d("UboUpdateWorker", "Updating uBO filter lists...")
        return Result.success()
    }
}
