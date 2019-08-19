package com.github.overpass.gather.screen.meeting.chat.users.list

import android.os.Handler
import android.view.ViewGroup
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.BaseViewHolder
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.Item
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.PendingUserViewHolder

class PendingUsersAdapter(listener: OnItemClickListener) : UsersAdapter(listener) {

    private val viewBinderHelper = ViewBinderHelper()

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        super.onBindViewHolder(holder, position)
        bindIfNeeded(holder, position)
    }

    override fun createUserViewHolder(parent: ViewGroup): BaseViewHolder<Item> {
        return createViewHolder(parent, R.layout.item_pending_user) {
            PendingUserViewHolder(it) as BaseViewHolder<Item>
        }
    }

    fun setSwipeLocked(locked: Boolean) {
        val ids = users.map { it.id }
                .toTypedArray()
        if (locked) {
            viewBinderHelper.lockSwipe(*ids)
        } else {
            viewBinderHelper.unlockSwipe(*ids)
        }
    }

    private fun bindIfNeeded(holder: BaseViewHolder<*>, position: Int) {
        if (holder is PendingUserViewHolder) {
            if (holder.itemView is SwipeRevealLayout) {
                val id = users[position].id
                viewBinderHelper.bind(holder.itemView as SwipeRevealLayout, id)
            }
            Handler().postDelayed({ holder.updateMenu() }, 500)
        }
    }
}
