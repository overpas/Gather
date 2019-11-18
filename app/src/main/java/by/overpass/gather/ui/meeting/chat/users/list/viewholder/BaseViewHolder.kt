package by.overpass.gather.ui.meeting.chat.users.list.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import by.overpass.gather.ui.meeting.chat.users.list.UsersAdapter
import by.overpass.gather.ui.meeting.chat.users.model.UserModel

abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    open fun bind(userModel: UserModel, listener: UsersAdapter.OnItemClickListener) {}
}