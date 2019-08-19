package com.github.overpass.gather.screen.meeting.chat.users.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.meeting.chat.users.list.viewholder.*
import com.github.overpass.gather.screen.meeting.chat.users.model.UserModel
import java.util.*

open class UsersAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<BaseViewHolder<Item>>() {

    protected val users: MutableList<UserModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<Item> {
        val viewHolder: BaseViewHolder<Item>
        when (viewType) {
            VIEW_TYPE_NO_DATA -> viewHolder = createViewHolder(parent, R.layout.item_no_data) {
                NoDataViewHolder(it)
            }
            VIEW_TYPE_PROGRESS -> viewHolder = createViewHolder(parent, R.layout.item_progress) {
                ProgressViewHolder(it)
            }
            else -> viewHolder = createUserViewHolder(parent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BaseViewHolder<Item>, position: Int) {
        holder.bind(users[position], listener)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun getItemViewType(position: Int): Int {
        var viewType = VIEW_TYPE_USER
        if (users[position] === UserModel.PROGRESS) {
            viewType = VIEW_TYPE_PROGRESS
        } else if (users[position] === UserModel.NO_DATA) {
            viewType = VIEW_TYPE_NO_DATA
        }
        return viewType
    }

    fun setUsers(newUsers: List<UserModel>) {
        val diff = Diff(users, newUsers)
        val diffResult = DiffUtil.calculateDiff(diff)
        users.clear()
        if (newUsers.isNotEmpty()) {
            users.addAll(newUsers)
        } else {
            users.add(UserModel.NO_DATA)
        }
        diffResult.dispatchUpdatesTo(this)
    }

    fun setProgress() {
        users.clear()
        users.add(UserModel.PROGRESS)
        notifyDataSetChanged()
    }

    protected fun <I : Item, VH : BaseViewHolder<I>> createViewHolder(
            parent: ViewGroup,
            @LayoutRes itemLayoutRes: Int,
            create: (View) -> VH
    ): VH {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(itemLayoutRes, parent, false)
        return create(view)
    }

    protected open fun createUserViewHolder(parent: ViewGroup): BaseViewHolder<Item> {
        return createViewHolder(parent, R.layout.item_user) {
            UserViewHolder(it) as BaseViewHolder<Item>
        }
    }

    private class Diff(
            private val oldList: List<UserModel>,
            private val newList: List<UserModel>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPos: Int, newItemPos: Int): Boolean {
            return oldList[oldItemPos].id == newList[newItemPos].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface OnItemClickListener {

        fun onItemClick(id: String)
    }

    companion object {

        private const val VIEW_TYPE_PROGRESS = 11
        private const val VIEW_TYPE_USER = 12
        private const val VIEW_TYPE_NO_DATA = 13
    }
}
