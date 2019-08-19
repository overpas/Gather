package com.github.overpass.gather.screen.meeting.chat.users.list.viewholder

import android.graphics.ColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.github.overpass.gather.R
import com.github.overpass.gather.screen.meeting.chat.users.list.UsersAdapter
import kotlinx.android.synthetic.main.item_progress.view.*

class ProgressViewHolder(itemView: View) : BaseViewHolder<Item>(itemView) {

    private val lavProgress: LottieAnimationView = itemView.lavProgress
    private val colorPrimary: Int = ContextCompat.getColor(itemView.context, R.color.colorPrimary)

    override fun bind(item: Item, listener: UsersAdapter.OnItemClickListener) {
        lavProgress.addValueCallback(
                KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                LottieValueCallback<ColorFilter>(SimpleColorFilter(colorPrimary))
        )
    }
}