package com.showfox.browser.ui.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.showfox.browser.data.model.Bookmark
import com.showfox.browser.data.repo.BookmarkRepository
import kotlinx.coroutines.launch

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkRepository(application)

    val bookmarks: LiveData<List<Bookmark>> = repository.getAllBookmarks()

    fun addBookmark(title: String, url: String, favicon: String? = null) {
        viewModelScope.launch { repository.addBookmark(title, url, favicon) }
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch { repository.removeBookmark(bookmark) }
    }

    fun clearAll() {
        viewModelScope.launch { repository.clearAll() }
    }
}
