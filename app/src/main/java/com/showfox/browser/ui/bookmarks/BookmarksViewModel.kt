package com.showfox.browser.ui.bookmarks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.showfox.browser.data.model.Bookmark
import com.showfox.browser.data.repo.BookmarkRepository

class BookmarksViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BookmarkRepository(application)

    val bookmarks: LiveData<List<Bookmark>> = repository.getAllBookmarks()

    fun addBookmark(title: String, url: String, favicon: String? = null) {
        repository.addBookmark(title, url, favicon)
    }

    fun removeBookmark(bookmark: Bookmark) {
        repository.removeBookmark(bookmark)
    }

    fun clearAll() {
        repository.clearAll()
    }
}
