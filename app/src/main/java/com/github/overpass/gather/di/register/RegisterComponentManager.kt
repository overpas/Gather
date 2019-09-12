package com.github.overpass.gather.di.register

import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent

class RegisterComponentManager(
        private val registerComponentFactory: RegisterComponent.Factory
) : RegisterComponent.Factory {

    private lateinit var registerComponent: RegisterComponent

    override fun create(initialStep: Integer): RegisterComponent {
        registerComponent = registerComponentFactory.create(initialStep)
        return registerComponent
    }

    fun getAddPersonalDataComponent(): AddPersonalDataComponent {
        return registerComponent.getAddPersonalDataComponent()
    }

    fun getConfirmationComponent(): ConfirmationComponent {
        return registerComponent.getConfirmationComponent()
    }

    fun getSignUpComponent(): SignUpComponent {
        return registerComponent.getSignUpComponent()
    }
}