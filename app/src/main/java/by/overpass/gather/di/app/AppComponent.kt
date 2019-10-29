package by.overpass.gather.di.app

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import by.overpass.gather.di.MAPBOX_TOKEN
import by.overpass.gather.di.app.modules.DispatcherModule
import by.overpass.gather.di.app.modules.FirebaseModule
import by.overpass.gather.di.app.modules.NetworkModule
import by.overpass.gather.di.app.vm.ViewModelFactoryModule
import by.overpass.gather.di.closeup.CloseupComponent
import by.overpass.gather.di.enrolled.EnrolledComponent
import by.overpass.gather.di.forgot.ForgotComponent
import by.overpass.gather.di.login.SignInComponent
import by.overpass.gather.di.map.MapComponent
import by.overpass.gather.di.meeting.MeetingComponent
import by.overpass.gather.di.messaging.MessagingComponent
import by.overpass.gather.di.newmeeting.NewMeetingComponent
import by.overpass.gather.di.profile.ProfileComponent
import by.overpass.gather.di.register.RegisterComponent
import by.overpass.gather.di.search.SearchComponent
import by.overpass.gather.di.splash.SplashComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [
    FirebaseModule::class,
    ViewModelFactoryModule::class,
    DispatcherModule::class,
    NetworkModule::class
])
interface AppComponent {

    fun getSignInComponent(): SignInComponent

    fun getMapComponent(): MapComponent

    fun getProfileComponent(): ProfileComponent

    fun getRegisterComponentFactory(): RegisterComponent.Factory

    fun getSplashComponent(): SplashComponent

    fun getCloseupComponent(): CloseupComponent

    fun getEnrolledComponent(): EnrolledComponent

    fun getMeetingComponentFactory(): MeetingComponent.Factory

    fun getSearchComponent(): SearchComponent

    fun getForgotComponent(): ForgotComponent

    fun getNewMeetingComponent(): NewMeetingComponent

    fun getMessagingComponent(): MessagingComponent

    @Component.Builder
    interface Builder {

        fun withContext(@BindsInstance context: Context): Builder

        fun withApp(@BindsInstance app: Application): Builder

        fun withMapboxToken(@BindsInstance @Named(MAPBOX_TOKEN) mapboxToken: String): Builder

        fun withContentResolver(@BindsInstance contentResolver: ContentResolver): Builder

        fun build(): AppComponent
    }
}