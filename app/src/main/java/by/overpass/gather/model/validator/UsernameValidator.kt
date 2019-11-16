package by.overpass.gather.model.validator

import java.util.regex.Pattern
import javax.inject.Inject

class UsernameValidator @Inject constructor() : Validator<String> {

    override fun isValid(username: String): Boolean {
        return Pattern.compile("[A-Za-z0-9_]+")
                .matcher(username)
                .matches()
    }
}
