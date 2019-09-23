package com.github.overpass.gather.screen.search

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.repo.meeting.MeetingWithId
import com.github.overpass.gather.screen.base.BaseBottomSheetDialogFragment
import com.github.overpass.gather.screen.meeting.MeetingActivity
import kotlinx.android.synthetic.main.fragment_bottom_search.*

class SearchBottomFragment : BaseBottomSheetDialogFragment<SearchViewModel>() {

    private lateinit var meetingsAdapter: MeetingsAdapter

    override fun getLayoutRes(): Int {
        return R.layout.fragment_bottom_search
    }

    override fun inject() {
        componentManager
                .getSearchComponent()
                .inject(this)
    }

    override fun createViewModel(): SearchViewModel {
        return viewModelProvider.get(SearchViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupList()
    }

    private fun setupList() {
        rvMeetings.layoutManager = LinearLayoutManager(context)
        meetingsAdapter = MeetingsAdapter { id -> MeetingActivity.start(requireContext(), id) }
        rvMeetings.adapter = meetingsAdapter
    }

    override fun onBind() {
        super.onBind()
        viewModel.queryResults().observe(viewLifecycleOwner, Observer<List<MeetingWithId>> {
            meetingsAdapter.setItems(it)
        })
        svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (!TextUtils.isEmpty(newText)) {
                    viewModel.setQueryText(newText)
                }
                return false
            }
        })
    }

    companion object {

        private const val TAG = "SearchBottomFragment"

        fun open(fragmentManager: FragmentManager) {
            val fragment = SearchBottomFragment()
            fragment.show(fragmentManager, TAG)
        }
    }
}
