package by.overpass.gather.ui.search

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.lifecycle.onMaybeNull
import by.overpass.gather.ui.base.BaseBottomSheetDialogFragment
import by.overpass.gather.ui.meeting.MeetingActivity
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
        onMaybeNull(viewModel.queryResults()) {
            meetingsAdapter.setItems(it)
        }
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
