package by.overpass.gather.ui.auth.register

interface RegistrationController {

    fun getInitialStep(): Int

    fun moveToNextStep()
}