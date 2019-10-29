package by.overpass.gather.di.register

import by.overpass.gather.di.ParentScope
import by.overpass.gather.di.image.ImageSourceModule
import by.overpass.gather.di.register.add.AddPersonalDataComponent
import by.overpass.gather.di.register.confirm.ConfirmationComponent
import by.overpass.gather.di.register.signup.SignUpComponent
import by.overpass.gather.ui.auth.register.RegisterActivity
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