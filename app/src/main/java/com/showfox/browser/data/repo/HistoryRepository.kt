package com.showfox.browser.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.showfox.browser.data.db.AppDatabase
import com.showfox.browser.data.model.HistoryEntry

class HistoryRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).historyDao()

    fun getAllHistory(): LiveData<List<HistoryEntry>> = dao.getAllHistory()
    fun searchHistory(query: String): LiveData<List<HistoryEntry>> = dao.searchHistory(query)

    suspend fun addEntry(title: String, url: String, favicon: String? = null) {
        dao.deleteByUrl(url)
        dao.insert(HistoryEntry(title = title, url = url, favicon = favicon))
    }

    suspend fun removeEntry(entry: HistoryEntry) = dao.deleteByUrl(entry.url)
    suspend fun clearAll() = dao.deleteAll()

    suspend fun cleanupOldEntries(maxAgeMs: Long = 30L * 24 * 60 * 60 * 1000) {
        val cutoff = System.currentTimeMillis() - maxAgeMs
        dao.deleteOlderThan(cutoff)
    }
}
