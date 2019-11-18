package by.overpass.gather.ui.meeting.chat.users.list.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import by.overpass.gather.ui.meeting.chat.users.list.UsersAdapter
import by.overpass.gather.ui.meeting.chat.users.model.UserModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

open class UserViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val tvUsername: TextView = itemView.tvUsername
    private val tvEmail: TextView = itemView.tvEmail
    private val ivUserPhoto: ImageView = itemView.ivUserPhoto

    override fun bind(user: UserModel, listener: UsersAdapter.OnItemClickListener) {
        tvUsername.text = user.username
        tvEmail.text = user.email
        Glide.with(itemView)
                .load(user.uri)
                .into(ivUserPhoto)
        itemView.setOnClickListener { view -> listener.onItemClick(user.id) }
    }
}