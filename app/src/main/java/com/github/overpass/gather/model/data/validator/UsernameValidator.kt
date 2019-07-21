package com.github.overpass.gather.model.data.validator

import java.util.regex.Pattern

class UsernameValidator : Validator<String> {

    override fun isValid(username: String): Boolean {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches()
    }
}
