package com.github.overpass.gather.screen.profile

import android.os.Bundle
import com.github.overpass.gather.App.Companion.appComponentManager
import com.github.overpass.gather.R
import com.github.overpass.gather.di.profile.ProfileComponent
import com.github.overpass.gather.di.profile.ProfileComponentManager
import com.github.overpass.gather.model.commons.FragmentUtils
import com.github.overpass.gather.screen.base.BackPressActivity
import com.github.overpass.gather.screen.dialog.PickImageDialogFragment

class ProfileActivity : BackPressActivity<GeneralProfileViewModel, ProfileComponent>(),
        PickImageDialogFragment.OnClickListener {

    override val componentManager: ProfileComponentManager =
            appComponentManager.getProfileComponentManager()

    override val layoutRes: Int = R.layout.activity_profile

    override fun createComponent(): ProfileComponent = componentManager.getOrCreate(Unit)

    override fun onComponentCreated(component: ProfileComponent) {
        component.inject(this)
    }

    override fun createViewModel(): GeneralProfileViewModel {
        return viewModelProvider.get(GeneralProfileViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            FragmentUtils.replace(supportFragmentManager, R.id.flProfileContainer,
                    ProfileFragment.newInstance(), false)
        }
    }

    override fun onGallery() {
        viewModel.onGallery()
    }

    override fun onCamera() {
        viewModel.onCamera()
    }
}
