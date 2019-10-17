package com.github.overpass.gather.di.register

import android.util.Log
import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.register.add.AddPersonalDataComponentManager
import com.github.overpass.gather.di.register.confirm.ConfirmationComponentManager
import com.github.overpass.gather.di.register.signup.SignUpComponentManager

class RegisterComponentManager(
        creator: (Int) -> RegisterComponent
) : ComponentManager<Int, RegisterComponent>(creator) {

    init {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Created")
    }

    private var addPersonalDataComponentManager: AddPersonalDataComponentManager? = null
    private var confirmationComponentManager: ConfirmationComponentManager? = null
    private var signUpComponentManager: SignUpComponentManager? = null

    fun getAddPersonalDataComponentManager(): AddPersonalDataComponentManager = addPersonalDataComponentManager
            ?: AddPersonalDataComponentManager(component!!.getAddPersonalDataComponent())
                    .also { addPersonalDataComponentManager = it }

    fun getConfirmationComponentManager(): ConfirmationComponentManager = confirmationComponentManager
            ?: ConfirmationComponentManager(component!!.getConfirmationComponent())
                    .also { confirmationComponentManager = it }

    fun getSignUpComponentManager(): SignUpComponentManager = signUpComponentManager
            ?: SignUpComponentManager(component!!.getSignUpComponent())
                    .also { signUpComponentManager = it }

    protected fun finalize() {
        Log.w(this::class.java.simpleName, "RegisterComponentManager Destroyed")
    }
}