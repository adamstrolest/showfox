package com.showfox.browser.ui.tabs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R
import org.mozilla.geckoview.GeckoSession

class TabsAdapter(
    private val sessions: List<GeckoSession>,
    private val onTabClick: (Int) -> Unit,
    private val onTabClose: (Int) -> Unit,
    private val onNewTab: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TAB = 0
        private const val TYPE_NEW_TAB = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < sessions.size) TYPE_TAB else TYPE_NEW_TAB
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_TAB) {
            TabViewHolder(
                inflater.inflate(R.layout.item_tab, parent, false),
                onTabClick,
                onTabClose
            )
        } else {
            NewTabViewHolder(
                inflater.inflate(R.layout.item_new_tab, parent, false),
                onNewTab
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TabViewHolder) {
            holder.bind(sessions[position], position)
        }
    }

    override fun getItemCount(): Int = sessions.size + 1

    class TabViewHolder(
        itemView: View,
        private val onTabClick: (Int) -> Unit,
        private val onTabClose: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val titleView: TextView = itemView.findViewById(R.id.tab_title)

        fun bind(session: GeckoSession, position: Int) {
            titleView.text = "Tab ${position + 1}"
            itemView.setOnClickListener { onTabClick(position) }
            itemView.contentDescription = "Tab ${position + 1}"
        }
    }

    class NewTabViewHolder(
        itemView: View,
        private val onNewTab: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener { onNewTab() }
            itemView.contentDescription = "New tab"
        }
    }
}
