package com.github.overpass.gather.screen.meeting.chat.users.list.viewholder

import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.github.overpass.gather.screen.meeting.chat.users.list.UsersAdapter
import com.github.overpass.gather.screen.meeting.chat.users.model.UserModel
import kotlinx.android.synthetic.main.item_pending_user.view.*

class PendingUserViewHolder(itemView: View) : UserViewHolder(itemView) {

    private val flMenuContainer: FrameLayout = itemView.flMenuContainer
    private val tvAccept: TextView = itemView.tvAccept

    override fun bind(item: UserModel, listener: UsersAdapter.OnItemClickListener) {
        super.bind(item, listener)
        tvAccept.setOnClickListener { view -> listener.onItemClick(item.id) }
    }

    fun updateMenu() {
        val params = flMenuContainer.layoutParams
        params.height = itemView.height
        flMenuContainer.layoutParams = params
    }
}
