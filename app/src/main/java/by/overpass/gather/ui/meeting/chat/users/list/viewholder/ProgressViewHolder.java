package by.overpass.gather.ui.meeting.chat.users.list.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.SimpleColorFilter;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieValueCallback;
import by.overpass.gather.R;
import by.overpass.gather.ui.meeting.chat.users.list.UsersAdapter;
import by.overpass.gather.ui.meeting.chat.users.model.UserModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProgressViewHolder extends BaseViewHolder {

    @BindView(R.id.lavProgress)
    LottieAnimationView lavProgress;
    private int colorPrimary;

    public ProgressViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        colorPrimary = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
    }

    @Override
    public void bind(UserModel userModel, UsersAdapter.OnItemClickListener listener) {
        super.bind(userModel, listener);
        lavProgress.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new LottieValueCallback<>(new SimpleColorFilter(colorPrimary))
        );
    }
}