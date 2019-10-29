package by.overpass.gather.ui.meeting.chat.users.model;

import java.util.Objects;

public class UserModel {

    public static final UserModel PROGRESS = new UserModel("-1");
    public static final UserModel NO_DATA = new UserModel("-2");
    private String id;
    private String username;
    private String email;
    private String uri;

    public UserModel() {
    }

    public UserModel(String id) {
        this.id = id;
    }

    public UserModel(String id, String username, String email, String uri) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        if (!Objects.equals(id, userModel.id)) return false;
        if (!Objects.equals(username, userModel.username))
            return false;
        if (!Objects.equals(email, userModel.email)) return false;
        return Objects.equals(uri, userModel.uri);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }
}
