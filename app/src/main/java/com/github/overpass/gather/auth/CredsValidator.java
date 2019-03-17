package com.github.overpass.gather.auth;

import android.util.Patterns;

import java.util.regex.Pattern;

public class CredsValidator {

    public boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches();
    }

    public boolean isPasswordValid(String password) {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$")
                .matcher(password)
                .matches();
    }

    public boolean isUsernameValid(String username) {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches();
    }
}
