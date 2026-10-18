package com.showfox.browser.ui.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R
import com.showfox.browser.ui.MainActivity

class TabsFragment : Fragment() {

    private lateinit var adapter: TabsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_tabs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.tabs_grid)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        val activity = requireActivity() as MainActivity
        adapter = TabsAdapter(
            sessions = activity.sessionManager.getSessions(),
            onTabClick = { index ->
                val sessions = activity.sessionManager.getSessions()
                if (index < sessions.size) {
                    activity.sessionManager.setActiveSession(sessions[index])
                }
            },
            onTabClose = { index ->
                val sessions = activity.sessionManager.getSessions()
                if (index < sessions.size) {
                    activity.sessionManager.closeSession(sessions[index])
                }
            },
            onNewTab = {
                activity.navigateToUrl("https://www.google.com")
            }
        )
        recyclerView.adapter = adapter
    }
}
