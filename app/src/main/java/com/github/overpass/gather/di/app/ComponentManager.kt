package com.github.overpass.gather.di.app

import com.github.overpass.gather.di.login.SignInComponent
import com.github.overpass.gather.di.map.MapComponent
import com.github.overpass.gather.di.map.detail.MapDetailComponent
import com.github.overpass.gather.di.profile.ProfileComponent
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponent
import com.github.overpass.gather.di.register.RegisterComponentManager
import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent
import com.github.overpass.gather.di.splash.SplashComponent
import com.github.overpass.gather.model.commons.weakCached

class ComponentManager(private val appComponent: AppComponent) : AppComponent {

    private var signInComp by weakCached { appComponent.getSignInComponent() }
    private var mapComp by weakCached { appComponent.getMapComponent() }
    private var profileComp by weakCached { appComponent.getProfileComponent() }
    private var splashComp by weakCached { appComponent.getSplashComponent() }
    private val registerComponentManager = RegisterComponentManager(appComponent.getRegisterComponentFactory())

    override fun getSignInComponent(): SignInComponent = signInComp

    override fun getMapComponent(): MapComponent = mapComp

    fun getMapDetailComponent(): MapDetailComponent = mapComp.getDetailComponent()

    override fun getProfileComponent(): ProfileComponent = profileComp

    fun getProfileDetailComponent(): ProfileDetailComponent = profileComp.getDetailComponent()

    override fun getRegisterComponentFactory(): RegisterComponentManager = registerComponentManager

    fun getAddPersonalDataComponent(): AddPersonalDataComponent {
        return registerComponentManager.getAddPersonalDataComponent()
    }

    fun getConfirmationComponent(): ConfirmationComponent {
        return registerComponentManager.getConfirmationComponent()
    }

    fun getSignUpComponent(): SignUpComponent {
        return registerComponentManager.getSignUpComponent()
    }

    override fun getSplashComponent(): SplashComponent = splashComp
}