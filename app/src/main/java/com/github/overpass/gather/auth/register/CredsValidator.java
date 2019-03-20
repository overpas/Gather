package com.github.overpass.gather.auth.register;

import com.github.overpass.gather.auth.BaseValidator;

import java.util.regex.Pattern;

public class CredsValidator extends BaseValidator {

    public boolean isUsernameValid(String username) {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches();
    }
}
