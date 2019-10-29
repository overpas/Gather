package by.overpass.gather.di.register

import android.util.Log
import by.overpass.gather.di.register.add.AddPersonalDataComponent
import by.overpass.gather.di.register.confirm.ConfirmationComponent
import by.overpass.gather.di.register.signup.SignUpComponent

class RegisterComponentManager(
        private val registerComponentFactory: RegisterComponent.Factory
) : RegisterComponent.Factory {

    init {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Created")
    }

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

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Destroyed")
    }

    companion object {
        private const val UNINITIALIZED_PARENT_MESSAGE = "Parent component hasn't been initialized"
    }
}