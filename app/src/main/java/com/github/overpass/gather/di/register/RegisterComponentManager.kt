package com.github.overpass.gather.di.register

import android.util.Log
import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent

class RegisterComponentManager(
        private val registerComponentFactory: RegisterComponent.Factory
) : RegisterComponent.Factory {

    init {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Created")
    }

    private lateinit var registerComponent: RegisterComponent

    private var addPersonalDataComponent: AddPersonalDataComponent? = null
    private var confirmationComponent: ConfirmationComponent? = null
    private var signUpComponent: SignUpComponent? = null

    override fun create(initialStep: Integer): RegisterComponent {
        registerComponent = registerComponentFactory.create(initialStep)
        return registerComponent
    }

    fun getAddPersonalDataComponent(): AddPersonalDataComponent =
            addPersonalDataComponent ?: registerComponent.getAddPersonalDataComponent()
                    .also { addPersonalDataComponent = it }

    fun clearAddPersonalDataComponent() {
        addPersonalDataComponent = null
    }

    fun getConfirmationComponent(): ConfirmationComponent =
            confirmationComponent ?: registerComponent.getConfirmationComponent()
                    .also { confirmationComponent = it }

    fun clearConfirmationComponent() {
        confirmationComponent = null
    }

    fun getSignUpComponent(): SignUpComponent =
            signUpComponent ?: registerComponent.getSignUpComponent()
                    .also { signUpComponent = it }

    fun clearSignUpComponent() {
        signUpComponent = null
    }

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Destroyed")
    }
}