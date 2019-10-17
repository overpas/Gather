package com.github.overpass.gather.di.app

import com.github.overpass.gather.di.closeup.CloseupComponentManager
import com.github.overpass.gather.di.enrolled.EnrolledComponentManager
import com.github.overpass.gather.di.forgot.ForgotComponentManager
import com.github.overpass.gather.di.login.SignInComponentManager
import com.github.overpass.gather.di.map.MapComponentManager
import com.github.overpass.gather.di.map.detail.MapDetailComponentManager
import com.github.overpass.gather.di.meeting.MeetingComponentManager
import com.github.overpass.gather.di.meeting.chat.ChatComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.AttachmentsComponentManager
import com.github.overpass.gather.di.meeting.chat.attachments.detail.AttachmentDetailsComponentManager
import com.github.overpass.gather.di.meeting.chat.delete.DeleteMessageComponentManager
import com.github.overpass.gather.di.meeting.chat.details.MeetingDetailComponentManager
import com.github.overpass.gather.di.meeting.chat.users.UsersComponentManager
import com.github.overpass.gather.di.meeting.join.JoinComponentManager
import com.github.overpass.gather.di.newmeeting.NewMeetingComponentManager
import com.github.overpass.gather.di.profile.ProfileComponentManager
import com.github.overpass.gather.di.profile.detail.ProfileDetailComponentManager
import com.github.overpass.gather.di.register.RegisterComponentManager
import com.github.overpass.gather.di.register.add.AddPersonalDataComponentManager
import com.github.overpass.gather.di.register.confirm.ConfirmationComponentManager
import com.github.overpass.gather.di.register.signup.SignUpComponentManager
import com.github.overpass.gather.di.search.SearchComponentManager
import com.github.overpass.gather.di.splash.SplashComponentManager

class AppComponentManager(private val appComponent: AppComponent) {

    private var signInComponentManager: SignInComponentManager? = null
    private var registerComponentManager: RegisterComponentManager? = null
    private var meetingComponentManager: MeetingComponentManager? = null
    private var newMeetingComponentManager: NewMeetingComponentManager? = null
    private var forgotComponentManager: ForgotComponentManager? = null
    private var searchComponentManager: SearchComponentManager? = null
    private var splashComponentManager: SplashComponentManager? = null
    private var closeupComponentManager: CloseupComponentManager? = null
    private var enrolledComponentManager: EnrolledComponentManager? = null
    private var mapComponentManager: MapComponentManager? = null
    private var profileComponentManager: ProfileComponentManager? = null

    fun getSignInComponentManager(): SignInComponentManager = signInComponentManager
            ?: SignInComponentManager(appComponent.getSignInComponent())
                    .also { signInComponentManager = it }

    fun getMapComponentManager(): MapComponentManager = mapComponentManager
            ?: MapComponentManager(appComponent.getMapComponent())
                    .also { mapComponentManager = it }

    fun getMapDetailComponentManager(): MapDetailComponentManager =
            getMapComponentManager().getDetailComponentManager()

    fun getProfileComponentManager(): ProfileComponentManager =
            profileComponentManager
                    ?: ProfileComponentManager(appComponent.getProfileComponent())
                            .also { profileComponentManager = it }

    fun getProfileDetailComponentManager(): ProfileDetailComponentManager =
            getProfileComponentManager().getDetailComponentManager()

    fun getRegisterComponentManager(): RegisterComponentManager = registerComponentManager
            ?: RegisterComponentManager { step ->
                appComponent.getRegisterComponentFactory().create(Integer(step))
            }.also { registerComponentManager = it }

    fun getSignUpComponentManager(): SignUpComponentManager =
            getRegisterComponentManager().getSignUpComponentManager()

    fun getConfirmationComponentManager(): ConfirmationComponentManager =
            getRegisterComponentManager().getConfirmationComponentManager()

    fun getAddPersonalDataComponentManager(): AddPersonalDataComponentManager =
            getRegisterComponentManager().getAddPersonalDataComponentManager()

    fun getSplashComponentManager(): SplashComponentManager = splashComponentManager
            ?: SplashComponentManager(appComponent.getSplashComponent())
                    .also { splashComponentManager = it }

    fun getCloseupComponentManager(): CloseupComponentManager = closeupComponentManager
            ?: CloseupComponentManager(appComponent.getCloseupComponent())
                    .also { closeupComponentManager = it }

    fun getEnrolledComponentManager(): EnrolledComponentManager = enrolledComponentManager
            ?: EnrolledComponentManager(appComponent.getEnrolledComponent())
                    .also { enrolledComponentManager = it }

    fun getForgotComponentManager(): ForgotComponentManager = forgotComponentManager
            ?: ForgotComponentManager(appComponent.getForgotComponent())
                    .also { forgotComponentManager = it }

    fun getSearchComponentManager(): SearchComponentManager = searchComponentManager
            ?: SearchComponentManager(appComponent.getSearchComponent())
                    .also { searchComponentManager = it }

    fun getMeetingComponentManager(): MeetingComponentManager = meetingComponentManager
            ?: MeetingComponentManager(appComponent.getMeetingComponent())
                    .also { meetingComponentManager = it }

    fun getUsersComponentManager(): UsersComponentManager =
            getMeetingComponentManager().getUsersComponentManager()

    fun getChatComponentManager(): ChatComponentManager =
            getMeetingComponentManager().getChatComponentManager()

    fun getMeetingDetailsComponentManager(): MeetingDetailComponentManager =
            getMeetingComponentManager().getMeetingDetailComponentManager()

    fun getDeleteMessageComponentManager(): DeleteMessageComponentManager =
            getMeetingComponentManager().getDeleteMessageComponentManager()

    fun getAttachmentsComponentManager(): AttachmentsComponentManager =
            getMeetingComponentManager().getAttachmentsComponentManager()

    fun getAttachmentsDetailsComponentManager(): AttachmentDetailsComponentManager =
            getMeetingComponentManager().getAttachmentDetailComponentManager()

    fun getJoinComponentManager(): JoinComponentManager =
            getMeetingComponentManager().getJoinComponentManager()

    fun getNewMeetingComponentManager(): NewMeetingComponentManager = newMeetingComponentManager
            ?: NewMeetingComponentManager(appComponent.getNewMeetingComponent())
                    .also { newMeetingComponentManager = it }
}