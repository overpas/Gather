package com.github.overpass.gather.screen.meeting.chat.users.list.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.overpass.gather.screen.meeting.chat.users.list.UsersAdapter

abstract class BaseViewHolder<I : Item>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(item: I, listener: UsersAdapter.OnItemClickListener) {}
}