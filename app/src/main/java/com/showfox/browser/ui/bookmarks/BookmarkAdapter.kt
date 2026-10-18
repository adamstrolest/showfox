package com.showfox.browser.ui.bookmarks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R
import com.showfox.browser.data.model.Bookmark

class BookmarkAdapter(
    private val onItemClick: (Bookmark) -> Unit
) : ListAdapter<Bookmark, BookmarkAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bookmark, parent, false)
        return ViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: android.view.View,
        private val onItemClick: (Bookmark) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.bookmark_title)
        private val urlView: TextView = itemView.findViewById(R.id.bookmark_url)

        fun bind(bookmark: Bookmark) {
            titleView.text = bookmark.title
            urlView.text = bookmark.url
            itemView.setOnClickListener { onItemClick(bookmark) }
            itemView.contentDescription = bookmark.title
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Bookmark>() {
            override fun areItemsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Bookmark, newItem: Bookmark): Boolean =
                oldItem == newItem
        }
    }
}
