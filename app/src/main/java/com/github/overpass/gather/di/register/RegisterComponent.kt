package com.github.overpass.gather.di.register

import com.github.overpass.gather.di.ParentScope
import com.github.overpass.gather.di.image.ImageSourceModule
import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent
import com.github.overpass.gather.ui.auth.register.RegisterActivity
import dagger.BindsInstance
import dagger.Subcomponent

@ParentScope
@Subcomponent(modules = [RegisterModule::class, ImageSourceModule::class])
interface RegisterComponent {

    fun getAddPersonalDataComponent(): AddPersonalDataComponent

    fun getConfirmationComponent(): ConfirmationComponent

    fun getSignUpComponent(): SignUpComponent

    fun inject(registerActivity: RegisterActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(
                @BindsInstance initialStep: Integer
        ): RegisterComponent
    }
}