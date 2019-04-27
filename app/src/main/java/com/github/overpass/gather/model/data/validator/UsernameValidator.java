package com.github.overpass.gather.model.data.validator;

import java.util.regex.Pattern;

public class UsernameValidator {

    public boolean isUsernameValid(String username) {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches();
    }
}
