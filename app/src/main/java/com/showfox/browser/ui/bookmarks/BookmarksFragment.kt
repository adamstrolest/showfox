package com.showfox.browser.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.showfox.browser.R

class BookmarksFragment : Fragment() {

    private lateinit var viewModel: BookmarksViewModel
    private lateinit var adapter: BookmarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(BookmarksViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.bookmarks_grid)
        recyclerView.layoutManager = GridLayoutManager(context, 4)
        adapter = BookmarkAdapter { bookmark ->
            (activity as? com.showfox.browser.ui.MainActivity)?.navigateToUrl(bookmark.url)
        }
        recyclerView.adapter = adapter

        viewModel.bookmarks.observe(viewLifecycleOwner) { bookmarks ->
            adapter.submitList(bookmarks)
        }
    }
}
