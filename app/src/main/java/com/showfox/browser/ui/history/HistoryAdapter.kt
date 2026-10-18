package com.showfox.browser.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R
import com.showfox.browser.data.model.HistoryEntry
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (HistoryEntry) -> Unit
) : ListAdapter<HistoryEntry, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val dateFormat = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(view, onItemClick, dateFormat)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        itemView: View,
        private val onItemClick: (HistoryEntry) -> Unit,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.history_title)
        private val urlView: TextView = itemView.findViewById(R.id.history_url)
        private val dateView: TextView = itemView.findViewById(R.id.history_date)

        fun bind(entry: HistoryEntry) {
            titleView.text = entry.title.ifEmpty { entry.url }
            urlView.text = entry.url
            dateView.text = dateFormat.format(Date(entry.timestamp))
            itemView.setOnClickListener { onItemClick(entry) }
            itemView.contentDescription = entry.title
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryEntry>() {
            override fun areItemsTheSame(oldItem: HistoryEntry, newItem: HistoryEntry): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: HistoryEntry, newItem: HistoryEntry): Boolean =
                oldItem == newItem
        }
    }
}
