package com.github.overpass.gather.model.data.validator;

import android.util.Patterns;

import java.util.regex.Pattern;

public class BaseValidator {

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
}
