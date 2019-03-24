package com.github.overpass.gather.auth.register;

import java.util.regex.Pattern;

public class UsernameValidator {

    public boolean isUsernameValid(String username) {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches();
    }
}
