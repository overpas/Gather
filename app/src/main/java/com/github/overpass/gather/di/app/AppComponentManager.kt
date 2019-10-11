package com.github.overpass.gather.di.app

import com.github.overpass.gather.di.ComponentManager
import com.github.overpass.gather.di.closeup.CloseupComponent
import com.github.overpass.gather.di.enrolled.EnrolledComponent
import com.github.overpass.gather.di.forgot.ForgotComponent
import com.github.overpass.gather.di.login.SignInComponent
import com.github.overpass.gather.di.login.SignInComponentManager
import com.github.overpass.gather.di.map.MapComponentManager
import com.github.overpass.gather.di.map.detail.MapDetailComponentManager
import com.github.overpass.gather.di.meeting.MeetingComponentManager
import com.github.overpass.gather.di.meeting.chat.ChatComponent
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponent
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentsDetailsComponent
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponent
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponent
import com.github.overpass.gather.di.meeting.chat.users.UsersComponent
import com.github.overpass.gather.di.meeting.join.JoinComponent
import com.github.overpass.gather.di.newmeeting.NewMeetingComponentManager
import com.github.overpass.gather.di.profile.ProfileComponentManager
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponent
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponentManager
import com.github.overpass.gather.di.register.RegisterComponentManager
import com.github.overpass.gather.di.register.add.AddPersonalDataComponent
import com.github.overpass.gather.di.register.confirm.ConfirmationComponent
import com.github.overpass.gather.di.register.signup.SignUpComponent
import com.github.overpass.gather.di.search.SearchComponent
import com.github.overpass.gather.di.splash.SplashComponent

class AppComponentManager(private val appComponent: AppComponent) {

    private var signInComponentManager: SignInComponentManager? = null
    private var registerComponentManager: RegisterComponentManager? = null
    private var meetingComponentManager: MeetingComponentManager? = null
    private var newMeetingComponentManager: NewMeetingComponentManager? = null
    private var forgotComponent: ForgotComponent? = null
    private var searchComponent: SearchComponent? = null
    private var splashComponent: SplashComponent? = null
    private var closeupComponent: CloseupComponent? = null
    private var enrolledComponent: EnrolledComponent? = null
    private var mapComponentManager: MapComponentManager? = null
    private var profileComponentManager: ProfileComponentManager? = null

    fun getSignInComponentManager(): SignInComponentManager =
            signInComponentManager ?: SignInComponentManager { appComponent.getSignInComponent() }
                    .also { signInComponentManager = it }

    fun getMapComponentManager(): MapComponentManager =
            mapComponentManager ?: appComponent.getMapComponent()
                    .also { mapComponentManager = MapComponentManager { it } }
                    .let { mapComponentManager!! }

    fun getMapDetailComponentManager(): MapDetailComponentManager =
            getMapComponentManager().getDetailComponentManager()

    fun getProfileComponentManager(): ProfileComponentManager =
            profileComponentManager ?: ProfileComponentManager { appComponent.getProfileComponent() }
                    .also { profileComponentManager = it }

    fun getProfileDetailComponentManager(): ProfileDetailComponentManager =
            getProfileComponentManager().getDetailComponentManager()

    fun getRegisterComponentFactory(): RegisterComponentManager =
            registerComponentManager ?: appComponent.getRegisterComponentFactory()
                    .also { registerComponentManager = RegisterComponentManager(it) }
                    .let { registerComponentManager!! }

    fun clearRegisterComponentFactory() {
        registerComponentManager = null
    }

    fun getAddPersonalDataComponent(): AddPersonalDataComponent =
            getRegisterComponentFactory().getAddPersonalDataComponent()

    fun clearAddPersonalDataComponent() =
            getRegisterComponentFactory().clearAddPersonalDataComponent()

    fun getConfirmationComponent(): ConfirmationComponent =
            getRegisterComponentFactory().getConfirmationComponent()

    fun clearConfirmationComponent() =
            getRegisterComponentFactory().clearConfirmationComponent()

    fun getSignUpComponent(): SignUpComponent =
            getRegisterComponentFactory().getSignUpComponent()

    fun clearSignUpComponent() =
            getRegisterComponentFactory().clearSignUpComponent()

    fun getSplashComponent(): SplashComponent =
            splashComponent ?: appComponent.getSplashComponent()
                    .also { splashComponent = it }

    fun clearSplashComponent() {
        splashComponent = null
    }

    fun getCloseupComponent(): CloseupComponent =
            closeupComponent ?: appComponent.getCloseupComponent()
                    .also { closeupComponent = it }

    fun clearCloseupComponent() {
        closeupComponent = null
    }

    fun getEnrolledComponent(): EnrolledComponent =
            enrolledComponent ?: appComponent.getEnrolledComponent()
                    .also { enrolledComponent = it }

    fun clearEnrolledComponent() {
        enrolledComponent = null
    }

    fun getMeetingComponent(): MeetingComponentManager =
            meetingComponentManager ?: appComponent.getMeetingComponent()
                    .also { meetingComponentManager = MeetingComponentManager(it) }
                    .let { meetingComponentManager!! }

    fun clearMeetingComponent() {
        meetingComponentManager = null
    }

    fun getUsersComponent(): UsersComponent = getMeetingComponent().getUsersComponent()

    fun clearUsersComponent() = getMeetingComponent().clearUsersComponent()

    fun getSearchComponent(): SearchComponent =
            searchComponent ?: appComponent.getSearchComponent()
                    .also { searchComponent = it }

    fun clearSearchComponent() {
        searchComponent = null
    }

    fun getForgotComponent(): ForgotComponent =
            forgotComponent ?: appComponent.getForgotComponent()
                    .also { forgotComponent = it }

    fun clearForgotComponent() {
        forgotComponent = null
    }

    fun getChatComponent(): ChatComponent = getMeetingComponent().getChatComponent()

    fun clearChatComponent() {
        getMeetingComponent().clearChatComponent()
    }

    fun getMeetingDetailsComponent(): MeetingDetailComponent =
            getMeetingComponent().getMeetingDetailComponent()

    fun clearMeetingDetailsComponent() = getMeetingComponent().clearMeetingDetailsComponent()

    fun getDeleteMessageComponent(): DeleteMessageComponent =
            getMeetingComponent().getDeleteMessageComponent()

    fun clearDeleteMessageComponent() = getMeetingComponent().clearDeleteMessageComponent()

    fun getAttachmentsComponent(): AttachmentsComponent =
            getMeetingComponent().getAttachmentsComponent()

    fun clearAttachmentsComponent() = getMeetingComponent().clearAttachmentsComponent()

    fun getAttachmentsDetailsComponent(): AttachmentsDetailsComponent =
            getMeetingComponent().getAttachmentDetailComponent()

    fun clearAttachmentDetailsComponent() = getMeetingComponent().cleatAttachmentDetailComponent()

    fun getJoinComponent(): JoinComponent = getMeetingComponent().getJoinComponent()

    fun clearJoinComponent() = getMeetingComponent().clearJoinComponent()

    fun getNewMeetingComponentManager(): NewMeetingComponentManager =
            newMeetingComponentManager ?: appComponent.getNewMeetingComponent()
                    .also { newMeetingComponentManager = NewMeetingComponentManager { it } }
                    .let { newMeetingComponentManager!! }
}