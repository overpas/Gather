package by.overpass.gather.model.validator

import android.util.Patterns
import javax.inject.Inject

class EmailValidator @Inject constructor() : Validator<String> {

    override fun isValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()
    }
}