package com.showfox.browser.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.showfox.browser.data.model.HistoryEntry
import com.showfox.browser.data.repo.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = HistoryRepository(application)

    val history: LiveData<List<HistoryEntry>> = repository.getAllHistory()

    fun clearAll() {
        viewModelScope.launch { repository.clearAll() }
    }

    fun cleanupOldEntries() {
        viewModelScope.launch { repository.cleanupOldEntries() }
    }
}
