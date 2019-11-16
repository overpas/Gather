package by.overpass.gather.ui.meeting.join

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import by.overpass.gather.App.Companion.componentManager
import by.overpass.gather.R
import by.overpass.gather.commons.android.fragment.transaction
import by.overpass.gather.commons.android.snackbar
import by.overpass.gather.commons.date.meetingDateFormat
import by.overpass.gather.ui.base.BaseFragmentKt
import by.overpass.gather.ui.create.MeetingType
import by.overpass.gather.ui.dialog.progress.indeterminate.ProgressDialogFragment
import by.overpass.gather.ui.meeting.base.LoadMeetingStatus
import by.overpass.gather.ui.meeting.chat.ChatFragment
import by.overpass.gather.ui.meeting.enrolled.EnrolledActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_join.*
import java.util.*

class JoinFragment : BaseFragmentKt<JoinViewModel>() {

    override fun createViewModel(): JoinViewModel {
        return viewModelProvider.get(JoinViewModel::class.java)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_join
    }

    override fun inject() {
        componentManager.getJoinComponent()
                .inject(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbarJoin.setNavigationOnClickListener { navIcon -> requireActivity().finish() }
        flProgress.visibility = View.VISIBLE
        viewModel.loadMeetingCheckEnrolled()
                .observe(viewLifecycleOwner, Observer<LoadPrivateMeetingStatus> { this.handleLoadStatus(it) })
        btnJoin.setOnClickListener {
            viewModel.join().observe(viewLifecycleOwner, Observer<JoinStatus> { this.handleJoin(it) })
        }
    }

    private fun handleJoin(status: JoinStatus) {
        when (status.tag()) {
            JoinStatus.ENROLLED -> handleEnrolled(status.`as`<JoinStatus.Enrolled>(JoinStatus.Enrolled::class.java))
            JoinStatus.ERROR -> handleJoinError(status.`as`<JoinStatus.Error>(JoinStatus.Error::class.java))
            JoinStatus.JOINED -> handleJoined(status.`as`<JoinStatus.Joined>(JoinStatus.Joined::class.java))
            JoinStatus.PROGRESS -> handleProgress(status.`as`<JoinStatus.Progress>(JoinStatus.Progress::class.java))
        }
    }

    private fun handleProgress(progress: JoinStatus.Progress) {
        ProgressDialogFragment.show(fragmentManager)
    }

    private fun handleJoined(joined: JoinStatus.Joined) {
        ProgressDialogFragment.hide(fragmentManager)
        transaction()
                .replace(R.id.flMeetingContainer, ChatFragment.newInstance())
                .commit()
    }

    private fun handleJoinError(error: JoinStatus.Error) {
        ProgressDialogFragment.hide(fragmentManager)
        ivMeetingType.snackbar(error.throwable.localizedMessage, Snackbar.LENGTH_SHORT)
    }

    private fun handleEnrolled(enrolled: JoinStatus.Enrolled) {
        ProgressDialogFragment.hide(fragmentManager)
        startActivity(Intent(context, EnrolledActivity::class.java))
        requireActivity().finish()
    }

    protected fun handleLoadStatus(loadMeetingStatus: LoadMeetingStatus) {
        when (loadMeetingStatus.tag()) {
            LoadPrivateMeetingStatus.ERROR -> handleLoadError(loadMeetingStatus.`as`<LoadPrivateMeetingStatus.Error>(LoadPrivateMeetingStatus.Error::class.java))
            LoadPrivateMeetingStatus.SUCCESS -> handleLoadSuccess(loadMeetingStatus.`as`<LoadPrivateMeetingStatus.Success>(LoadPrivateMeetingStatus.Success::class.java))
            LoadPrivateMeetingStatus.PROGRESS -> handlePrivateMeetingProgress()
        }
    }

    protected fun handleLoadSuccess(success: LoadPrivateMeetingStatus.Success) {
        flProgress.visibility = View.GONE
        tvName.text = success.meetingAndRatio.meeting.name
        tvPrivateMeeting.visibility = if (success.meetingAndRatio.meeting.isPrivate)
            View.VISIBLE
        else
            View.INVISIBLE
        if (success.isAlreadyEnrolled) {
            tvPrivateMeeting.setText(R.string.you_have_already_enrolled)
            btnJoin.visibility = View.GONE
        }
        val ratio = String.format(Locale.getDefault(), "%d / %d",
                success.meetingAndRatio.ratio.current,
                success.meetingAndRatio.ratio.max)
        tvRatio.text = ratio
        val format = meetingDateFormat()
        tvDate.text = format.format(success.meetingAndRatio.meeting.date)
        if (MeetingType.isBusiness(success.meetingAndRatio.meeting.type)) {
            ivMeetingType.setImageResource(R.drawable.ic_case_large)
        } else if (MeetingType.isProtest(success.meetingAndRatio.meeting.type)) {
            ivMeetingType.setImageResource(R.drawable.ic_bund_large)
        } else {
            ivMeetingType.setImageResource(R.drawable.ic_beer_large)
        }
        viewModel.getAddress(success.meetingAndRatio.meeting.longitude,
                success.meetingAndRatio.meeting.latitude)
                .observe(viewLifecycleOwner, Observer<String> { tvAddress.text = it })
    }

    protected fun handlePrivateMeetingProgress() {
        flProgress.visibility = View.VISIBLE
    }

    protected fun handleLoadError(error: LoadPrivateMeetingStatus.Error) {
        flProgress.visibility = View.GONE
    }

    companion object {

        fun newInstance(): JoinFragment {
            return JoinFragment()
        }
    }
}
