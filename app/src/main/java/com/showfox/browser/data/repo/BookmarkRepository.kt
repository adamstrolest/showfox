package com.showfox.browser.data.repo

import android.content.Context
import androidx.lifecycle.LiveData
import com.showfox.browser.data.db.AppDatabase
import com.showfox.browser.data.model.Bookmark

class BookmarkRepository(context: Context) {
    private val dao = AppDatabase.getDatabase(context).bookmarkDao()

    fun getAllBookmarks(): LiveData<List<Bookmark>> = dao.getAllBookmarks()
    fun searchBookmarks(query: String): LiveData<List<Bookmark>> = dao.searchBookmarks(query)

    suspend fun findByUrl(url: String): Bookmark? = dao.findByUrl(url)

    suspend fun addBookmark(title: String, url: String, favicon: String? = null) {
        dao.insert(Bookmark(title = title, url = url, favicon = favicon))
    }

    suspend fun removeBookmark(bookmark: Bookmark) = dao.delete(bookmark)
    suspend fun clearAll() = dao.deleteAll()
}
