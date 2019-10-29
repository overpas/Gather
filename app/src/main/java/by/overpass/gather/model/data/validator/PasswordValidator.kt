package by.overpass.gather.model.data.validator

import java.util.regex.Pattern
import javax.inject.Inject

class PasswordValidator @Inject constructor() : Validator<String> {

    override fun isValid(password: String): Boolean {
        return Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}$")
                .matcher(password)
                .matches()
    }
}