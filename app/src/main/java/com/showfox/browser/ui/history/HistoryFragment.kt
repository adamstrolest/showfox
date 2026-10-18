package com.showfox.browser.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R

class HistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.history_list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = HistoryAdapter { entry ->
            (activity as? com.showfox.browser.ui.MainActivity)?.navigateToUrl(entry.url)
        }
        recyclerView.adapter = adapter

        viewModel.history.observe(viewLifecycleOwner) { entries ->
            adapter.submitList(entries)
        }
    }
}
