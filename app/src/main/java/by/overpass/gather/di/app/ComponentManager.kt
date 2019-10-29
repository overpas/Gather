package by.overpass.gather.di.app

import androidx.lifecycle.Lifecycle
import by.overpass.gather.di.closeup.CloseupComponent
import by.overpass.gather.di.enrolled.EnrolledComponent
import by.overpass.gather.di.forgot.ForgotComponent
import by.overpass.gather.di.login.SignInComponent
import by.overpass.gather.di.map.MapComponent
import by.overpass.gather.di.map.detail.MapDetailComponent
import by.overpass.gather.di.meeting.MeetingComponent
import by.overpass.gather.di.meeting.MeetingComponentManager
import by.overpass.gather.di.meeting.chat.ChatComponentManager
import by.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import by.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import by.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import by.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import by.overpass.gather.di.meeting.chat.users.UsersComponent
import by.overpass.gather.di.meeting.join.JoinComponent
import by.overpass.gather.di.newmeeting.NewMeetingComponent
import by.overpass.gather.di.profile.ProfileComponent
import by.overpass.gather.di.profile.detail.ProfileDetailComponent
import by.overpass.gather.di.register.RegisterComponent
import by.overpass.gather.di.register.RegisterComponentManager
import by.overpass.gather.di.register.add.AddPersonalDataComponent
import by.overpass.gather.di.register.confirm.ConfirmationComponent
import by.overpass.gather.di.register.signup.SignUpComponent
import by.overpass.gather.di.search.SearchComponent
import by.overpass.gather.di.splash.SplashComponent
import by.overpass.gather.commons.android.lifecycle.LifecycleDisposable
import by.overpass.gather.di.messaging.MessagingComponent

class ComponentManager(private val appComponent: AppComponent) {

    private lateinit var registerComponentManagerDisposable: LifecycleDisposable<RegisterComponentManager>
    private lateinit var meetingComponentManagerDisposable: LifecycleDisposable<MeetingComponentManager>
    private lateinit var mapComponentDisposable: LifecycleDisposable<MapComponent>
    private lateinit var profileComponentDisposable: LifecycleDisposable<ProfileComponent>

    fun getSignInComponent(): SignInComponent = appComponent.getSignInComponent()

    fun getMapComponent(lifecycle: Lifecycle): MapComponent {
        mapComponentDisposable = LifecycleDisposable(
                lifecycle,
                appComponent.getMapComponent()
        )
        return mapComponentDisposable.value!!
    }

    fun getMapDetailComponent(): MapDetailComponent =
            mapComponentDisposable.value!!.getDetailComponent()

    fun getProfileComponent(lifecycle: Lifecycle): ProfileComponent {
        profileComponentDisposable = LifecycleDisposable(
                lifecycle,
                appComponent.getProfileComponent()
        )
        return profileComponentDisposable.value!!
    }

    fun getProfileDetailComponent(): ProfileDetailComponent =
            profileComponentDisposable.value!!.getDetailComponent()

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

    fun getSplashComponent(): SplashComponent = appComponent.getSplashComponent()

    fun getCloseupComponent(): CloseupComponent = appComponent.getCloseupComponent()

    fun getEnrolledComponent(): EnrolledComponent = appComponent.getEnrolledComponent()

    fun getMeetingComponentFactory(lifecycle: Lifecycle): MeetingComponent.Factory {
        meetingComponentManagerDisposable = LifecycleDisposable(
                lifecycle,
                MeetingComponentManager(appComponent.getMeetingComponentFactory())
        )
        return meetingComponentManagerDisposable.value!!
    }

    fun getUsersComponent(): UsersComponent =
            meetingComponentManagerDisposable.value!!.getUsersComponent()

    fun getSearchComponent(): SearchComponent = appComponent.getSearchComponent()

    fun getForgotComponent(): ForgotComponent = appComponent.getForgotComponent()

    fun getChatComponent(lifecycle: Lifecycle): ChatComponentManager =
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

    fun getNewMeetingComponent(): NewMeetingComponent =
            appComponent.getNewMeetingComponent()

    fun getMessagingComponent(): MessagingComponent = appComponent.getMessagingComponent()
}