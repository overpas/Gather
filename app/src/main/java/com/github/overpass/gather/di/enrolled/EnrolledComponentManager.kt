package com.github.overpass.gather.di.enrolled

import com.github.overpass.gather.di.ComponentManager

class EnrolledComponentManager(
        enrolledComponent: EnrolledComponent
) : ComponentManager<Unit, EnrolledComponent>({ enrolledComponent })