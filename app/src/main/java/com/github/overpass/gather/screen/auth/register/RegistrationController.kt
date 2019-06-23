package com.github.overpass.gather.screen.auth.register

interface RegistrationController {

    fun getInitialStep(): Int

    fun moveToNextStep()
}