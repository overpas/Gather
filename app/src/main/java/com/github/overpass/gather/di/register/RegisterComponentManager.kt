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
        return getComponentOrThrowError { getAddPersonalDataComponent() }
    }

    fun getConfirmationComponent(): ConfirmationComponent {
        return getComponentOrThrowError { getConfirmationComponent() }
    }

    fun getSignUpComponent(): SignUpComponent {
        return getComponentOrThrowError { getSignUpComponent() }
    }

    private fun <C> getComponentOrThrowError(getComponent: RegisterComponent.() -> C): C {
        return registerComponent.getComponent()
                ?: throw IllegalStateException(UNINITIALIZED_PARENT_MESSAGE)
    }

    companion object {
        private const val UNINITIALIZED_PARENT_MESSAGE = "Parent component hasn't been initialized"
    }
}