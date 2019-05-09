package com.github.overpass.gather.screen.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.overpass.gather.R;
import com.github.overpass.gather.model.commons.DateFormatting;
import com.github.overpass.gather.model.repo.meeting.MeetingWithId;
import com.github.overpass.gather.screen.create.MeetingType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsAdapter.MeetingViewHolder> {

    private final List<MeetingWithId> meetings = new ArrayList<>();
    private final OnItemClickListener listener;

    public MeetingsAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MeetingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MeetingViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_meeting, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingViewHolder holder, int position) {
        holder.bind(meetings.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }

    public void setItems(@Nullable List<MeetingWithId> meetingWithIds) {
        meetings.clear();
        meetings.addAll(meetingWithIds == null ? new ArrayList<>() : meetingWithIds);
        notifyDataSetChanged();
    }

    static class MeetingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivMeetingType)
        ImageView ivMeetingType;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public MeetingViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(MeetingWithId meetingWithId, OnItemClickListener listener) {
            if (MeetingType.isEntertainment(meetingWithId.getMeeting().getType())) {
                ivMeetingType.setImageResource(R.drawable.ic_beer_large);
            } else if (MeetingType.isProtest(meetingWithId.getMeeting().getType())) {
                ivMeetingType.setImageResource(R.drawable.ic_bund_large);
            } else {
                ivMeetingType.setImageResource(R.drawable.ic_case_large);
            }
            tvName.setText(meetingWithId.getMeeting().getName());
            String date = DateFormatting.getMeetingDateFormat().format(
                    meetingWithId.getMeeting().getDate());
            tvDate.setText(date);
            itemView.setOnClickListener(view -> listener.onItemClick(meetingWithId.getId()));
        }
    }

    interface OnItemClickListener {
        void onItemClick(String id);
    }
}
