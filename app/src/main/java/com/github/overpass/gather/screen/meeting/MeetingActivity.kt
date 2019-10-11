package com.github.overpass.gather.screen.meeting

import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.Constants.MEETING_ID_KEY
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.screen.base.BaseActivityKt
import com.github.overpass.gather.screen.meeting.chat.ChatFragment
import com.github.overpass.gather.screen.meeting.join.JoinFragment
import kotlinx.android.synthetic.main.activity_meeting.*

class MeetingActivity : BaseActivityKt<MeetingViewModel>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_meeting
    }

    override fun createViewModel(): MeetingViewModel {
        return viewModelProvider.get(MeetingViewModel::class.java)
    }

    override fun inject() {
        appComponentManager.getMeetingComponent()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            lavProgress.visibility = View.VISIBLE
            viewModel.isAllowed(getMeetingId()).observe(this, Observer<Boolean> { this.handleIsAllowed(it) })
        }
    }

    override fun clearComponent() {
        super.clearComponent()
        appComponentManager.clearMeetingComponent()
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
            FragmentUtils.replace(supportFragmentManager, R.id.flMeetingContainer,
                    JoinFragment.newInstance(getMeetingId()), false)
        } else {
            FragmentUtils.replace(supportFragmentManager, R.id.flMeetingContainer,
                    ChatFragment.newInstance(getMeetingId()), false)
        }
    }

    private fun getMeetingId(): String = intent?.extras?.getString(MEETING_ID_KEY) ?: "-1"

    companion object {

        @JvmStatic
        fun start(context: Context, meetingId: String) {
            start(context, MeetingActivity::class.java, MEETING_ID_KEY, meetingId)
        }
    }
}
