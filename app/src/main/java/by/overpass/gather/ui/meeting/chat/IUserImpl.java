package by.overpass.gather.ui.meeting.chat;

import com.stfalcon.chatkit.commons.models.IUser;

public class IUserImpl implements IUser {

    private String id;
    private String username;
    private String photoUrl;

    public IUserImpl(String id, String username, String photoUrl) {
        this.id = id;
        this.username = username;
        this.photoUrl = photoUrl;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    public String getAvatar() {
        return photoUrl;
    }
}
