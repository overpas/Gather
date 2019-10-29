package com.github.overpass.gather.di.validator

import com.github.overpass.gather.di.EMAIL_VALIDATOR
import com.github.overpass.gather.di.PASSWORD_VALIDATOR
import com.github.overpass.gather.di.ViewScope
import com.github.overpass.gather.model.data.validator.EmailValidator
import com.github.overpass.gather.model.data.validator.PasswordValidator
import com.github.overpass.gather.model.data.validator.Validator
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