package by.overpass.gather.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.overpass.gather.R
import by.overpass.gather.commons.date.meetingDateFormat
import by.overpass.gather.data.repo.meeting.MeetingWithId
import by.overpass.gather.ui.create.MeetingType
import kotlinx.android.synthetic.main.item_meeting.view.*
import java.util.*

class MeetingsAdapter(private val listener: (String) -> Unit) : RecyclerView.Adapter<MeetingsAdapter.MeetingViewHolder>() {

    private val meetings = ArrayList<MeetingWithId>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        return LayoutInflater.from(parent.context)
                .run { inflate(R.layout.item_meeting, parent, false) }
                .let { MeetingViewHolder(it, listener) }
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        holder.bind(meetings[position])
    }

    override fun getItemCount(): Int {
        return meetings.size
    }

    fun setItems(meetingWithIds: List<MeetingWithId>?) {
        meetings.clear()
        meetings.addAll(meetingWithIds ?: ArrayList())
        notifyDataSetChanged()
    }

    class MeetingViewHolder(
            itemView: View,
            private val listener: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivMeetingType = itemView.ivMeetingType
        private val tvName = itemView.tvName
        private val tvDate = itemView.tvDate

        fun bind(meetingWithId: MeetingWithId) {
            if (MeetingType.isEntertainment(meetingWithId.meeting.type)) {
                ivMeetingType.setImageResource(R.drawable.ic_beer_large)
            } else if (MeetingType.isProtest(meetingWithId.meeting.type)) {
                ivMeetingType.setImageResource(R.drawable.ic_bund_large)
            } else {
                ivMeetingType.setImageResource(R.drawable.ic_case_large)
            }
            tvName.text = meetingWithId.meeting.name
            val date = meetingDateFormat().format(
                    meetingWithId.meeting.date)
            tvDate.text = date
            itemView.setOnClickListener { view -> listener(meetingWithId.id) }
        }
    }
}
