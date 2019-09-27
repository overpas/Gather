package com.github.overpass.gather.di.app

import androidx.lifecycle.Lifecycle
import com.github.overpass.gather.di.closeup.CloseupComponent
import com.github.overpass.gather.di.enrolled.EnrolledComponent
import com.github.overpass.gather.di.forgot.ForgotComponent
import com.github.overpass.gather.di.login.SignInComponent
import com.github.overpass.gather.di.map.MapComponent
import com.github.overpass.gather.di.map.detail.MapDetailComponent
import com.github.overpass.gather.di.meeting.MeetingComponent
import com.github.overpass.gather.di.meeting.MeetingComponentManager
import com.github.overpass.gather.di.meeting.chat.ChatComponent
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.di.newmeeting.NewMeetingComponent
import com.github.overpass.gather.di.profile.ProfileComponent
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponent
import com.github.overpass.gather.di.register.RegisterComponent
import com.github.overpass.gather.di.register.RegisterComponentManager
import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent
import com.github.overpass.gather.di.search.SearchComponent
import com.github.overpass.gather.di.splash.SplashComponent
import com.github.overpass.gather.model.commons.LifecycleDisposable

class ComponentManager(private val appComponent: AppComponent) : AppComponent {

    private lateinit var registerComponentManagerDisposable: LifecycleDisposable<RegisterComponentManager>
    private lateinit var meetingComponentManagerDisposable: LifecycleDisposable<MeetingComponentManager>
    private lateinit var mapComponentDisposable: LifecycleDisposable<MapComponent>
    private lateinit var profileComponentDisposable: LifecycleDisposable<ProfileComponent>

    override fun getSignInComponent(): SignInComponent = appComponent.getSignInComponent()

    override fun getMapComponent(): MapComponent {
        throw IllegalStateException(notInitializedMessage("getMapComponent"))
    }

    fun getMapComponent(lifecycle: Lifecycle): MapComponent {
        mapComponentDisposable = LifecycleDisposable(
                lifecycle,
                appComponent.getMapComponent()
        )
        return mapComponentDisposable.value!!
    }

    fun getMapDetailComponent(): MapDetailComponent =
            mapComponentDisposable.value!!.getDetailComponent()

    override fun getProfileComponent(): ProfileComponent {
        throw IllegalStateException(notInitializedMessage("getProfileComponent"))
    }

    fun getProfileComponent(lifecycle: Lifecycle): ProfileComponent {
        profileComponentDisposable = LifecycleDisposable(
                lifecycle,
                appComponent.getProfileComponent()
        )
        return profileComponentDisposable.value!!
    }

    fun getProfileDetailComponent(): ProfileDetailComponent =
            profileComponentDisposable.value!!.getDetailComponent()

    override fun getRegisterComponentFactory(): RegisterComponent.Factory {
        throw IllegalStateException(notInitializedMessage("getRegisterComponentFactory"))
    }

    fun getRegisterComponentFactory(lifecycle: Lifecycle): RegisterComponent.Factory {
        registerComponentManagerDisposable = LifecycleDisposable(lifecycle,
                RegisterComponentManager(appComponent.getRegisterComponentFactory())
        )
        return registerComponentManagerDisposable.value!!
    }

    fun getAddPersonalDataComponent(): AddPersonalDataComponent {
        return registerComponentManagerDisposable.value!!.getAddPersonalDataComponent()
    }

    fun getConfirmationComponent(): ConfirmationComponent {
        return registerComponentManagerDisposable.value!!.getConfirmationComponent()
    }

    fun getSignUpComponent(): SignUpComponent {
        return registerComponentManagerDisposable.value!!.getSignUpComponent()
    }

    override fun getSplashComponent(): SplashComponent = appComponent.getSplashComponent()

    override fun getCloseupComponent(): CloseupComponent = appComponent.getCloseupComponent()

    override fun getEnrolledComponent(): EnrolledComponent = appComponent.getEnrolledComponent()

    override fun getMeetingComponent(): MeetingComponent {
        throw IllegalStateException(notInitializedMessage("getMeetingComponent"))
    }

    fun getMeetingComponent(lifecycle: Lifecycle): MeetingComponent {
        meetingComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                MeetingComponentManager(appComponent.getMeetingComponent())
        )
        return meetingComponentManagerDisposable.value!!
    }

    fun getUsersComponent(): UsersComponent =
            meetingComponentManagerDisposable.value!!.getUsersComponent()

    override fun getSearchComponent(): SearchComponent = appComponent.getSearchComponent()

    override fun getForgotComponent(): ForgotComponent = appComponent.getForgotComponent()

    fun getChatComponent(lifecycle: Lifecycle): ChatComponent =
            meetingComponentManagerDisposable.value!!.getChatComponent(lifecycle)

    fun getMeetingDetailsComponent(): MeetingDetailComponent =
            meetingComponentManagerDisposable.value!!.getMeetingDetailComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            meetingComponentManagerDisposable.value!!.getDeleteMessageComponent()

    fun getAttachmentsComponent(lifecycle: Lifecycle): AttachmentsComponent =
            meetingComponentManagerDisposable.value!!.getAttachmentsComponent(lifecycle)

    fun getAttachmentsDetailsComponent(): AttachmentsDetailsComponent =
            meetingComponentManagerDisposable.value!!.getAttachmentDetailComponent()

    fun getJoinComponent(): JoinComponent =
            meetingComponentManagerDisposable.value!!.getJoinComponent()

    override fun getNewMeetingComponent(): NewMeetingComponent =
            appComponent.getNewMeetingComponent()

    private fun notInitializedMessage(methodName: String) = "The subcomponent hasn't" +
            " been initialized. Consider calling fun $methodName(lifecycle: Lifecycle) instead"
}