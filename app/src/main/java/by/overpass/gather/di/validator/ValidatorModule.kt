package by.overpass.gather.di.validator

import by.overpass.gather.di.EMAIL_VALIDATOR
import by.overpass.gather.di.PASSWORD_VALIDATOR
import by.overpass.gather.di.ViewScope
import by.overpass.gather.model.validator.EmailValidator
import by.overpass.gather.model.validator.PasswordValidator
import by.overpass.gather.model.validator.Validator
import dagger.Binds
import dagger.Module
import javax.inject.Named

@Module
abstract class ValidatorModule {

    @Binds
    @ViewScope
    @Named(EMAIL_VALIDATOR)
    abstract fun bindEmailValidator(emailValidator: EmailValidator): Validator<String>

    @Binds
    @ViewScope
    @Named(PASSWORD_VALIDATOR)
    abstract fun bindPasswordValidator(passwordValidator: PasswordValidator): Validator<String>
}