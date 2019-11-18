package by.overpass.gather.ui.meeting.chat.users.list.viewholder

import android.graphics.ColorFilter
import android.view.View
import androidx.core.content.ContextCompat
import by.overpass.gather.R
import by.overpass.gather.ui.meeting.chat.users.list.UsersAdapter
import by.overpass.gather.ui.meeting.chat.users.model.UserModel
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import kotlinx.android.synthetic.main.item_progress.view.*

class ProgressViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val lavProgress: LottieAnimationView = itemView.lavProgress
    private val colorPrimary: Int = ContextCompat.getColor(itemView.context, R.color.colorPrimary)

    override fun bind(userModel: UserModel, listener: UsersAdapter.OnItemClickListener) {
        super.bind(userModel, listener)
        lavProgress.addValueCallback<ColorFilter>(
                KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                LottieValueCallback<ColorFilter>(SimpleColorFilter(colorPrimary))
        )
    }
}