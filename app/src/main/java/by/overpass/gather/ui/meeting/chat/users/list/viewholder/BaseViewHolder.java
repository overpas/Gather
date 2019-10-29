package by.overpass.gather.ui.meeting.chat.users.list.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import by.overpass.gather.ui.meeting.chat.users.list.UsersAdapter;
import by.overpass.gather.ui.meeting.chat.users.model.UserModel;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(UserModel userModel, UsersAdapter.OnItemClickListener listener) {
    }
}