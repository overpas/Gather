package by.overpass.gather.data.repo.user;


import androidx.annotation.Nullable;

public class UserData {

    private String id;
    private String name;
    @Nullable
    private String photoUrl;

    public UserData(String id, String name, @Nullable String photoUrl) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getPhotoUrl() {
        return photoUrl;
    }
}
