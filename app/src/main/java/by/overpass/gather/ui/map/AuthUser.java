package by.overpass.gather.ui.map;

public class AuthUser {

    private int role;
    private String id;

    public AuthUser() {
    }

    public AuthUser(int role, String id) {
        this.role = role;
        this.id = id;
    }

    public int getRole() {
        return role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public enum Role {

        ADMIN(0), MODER(1), USER(2), NOBODY(3);

        private final int role;

        Role(int role) {
            this.role = role;
        }

        public int getRole() {
            return role;
        }
    }
}
