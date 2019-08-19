package com.github.overpass.gather.screen.meeting.chat.users.list.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import coil.api.load
import com.github.overpass.gather.screen.meeting.chat.users.list.UsersAdapter
import com.github.overpass.gather.screen.meeting.chat.users.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

open class UserViewHolder(itemView: View) : BaseViewHolder<UserModel>(itemView) {

    private val tvUsername: TextView = itemView.tvUsername
    private val tvEmail: TextView = itemView.tvEmail
    private val ivUserPhoto: ImageView = itemView.ivUserPhoto

    override fun bind(item: UserModel, listener: UsersAdapter.OnItemClickListener) {
        tvUsername.text = item.username
        tvEmail.text = item.email
        ivUserPhoto.load(item.uri)
        itemView.setOnClickListener { view -> listener.onItemClick(item.id) }
    }
}