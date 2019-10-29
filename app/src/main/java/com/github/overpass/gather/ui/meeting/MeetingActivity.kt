package com.github.overpass.gather.ui.meeting

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.github.overpass.gather.App.Companion.componentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.commons.android.fragment.addToBackStack
import com.github.overpass.gather.commons.android.fragment.transaction
import com.github.overpass.gather.commons.android.getStringExtra
import com.github.overpass.gather.commons.android.lifecycle.on
import com.github.overpass.gather.ui.base.BaseActivityKt
import com.github.overpass.gather.ui.meeting.chat.ChatFragment
import com.github.overpass.gather.ui.meeting.join.JoinFragment
import kotlinx.android.synthetic.main.activity_meeting.*

class MeetingActivity : BaseActivityKt<MeetingViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_meeting
    }

    override fun createViewModel(): MeetingViewModel {
        return viewModelProvider.get(MeetingViewModel::class.java)
    }

    override fun inject() {
        componentManager.getMeetingComponentFactory(lifecycle)
                .create(getStringExtra(MEETING_ID_KEY))
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            lavProgress.visibility = View.VISIBLE
            on(viewModel.isAllowed) {
                handleIsAllowed(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleIsAllowed(isAllowed: Boolean) {
        lavProgress.visibility = View.GONE
        if (!isAllowed) {
            transaction()
                    .replace(R.id.flMeetingContainer, JoinFragment.newInstance())
                    .addToBackStack(false)
                    .commit()
        } else {
            transaction()
                    .replace(R.id.flMeetingContainer, ChatFragment.newInstance())
                    .addToBackStack(false)
                    .commit()
        }
    }

    companion object {

        private const val MEETING_ID_KEY = "MEETING_ID_KEY"

        @JvmStatic
        fun start(context: Context, meetingId: String) {
            start(context, MeetingActivity::class.java, MEETING_ID_KEY, meetingId)
        }
    }
}
