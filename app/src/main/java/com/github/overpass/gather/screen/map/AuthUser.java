package com.github.overpass.gather.screen.map;

public class AuthUser {

    private final int role;
    private final String id;

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

    public enum Role {

        ADMIN(0), MODER(1), USER(2);

        private final int role;

        Role(int role) {
            this.role = role;
        }

        public int getRole() {
            return role;
        }
    }
}
