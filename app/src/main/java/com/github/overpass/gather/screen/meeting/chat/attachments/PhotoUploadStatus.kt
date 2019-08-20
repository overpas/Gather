package com.github.overpass.gather.screen.meeting.chat.attachments

sealed class PhotoUploadStatus {

    object Success : PhotoUploadStatus()

    data class Error(val throwable: Throwable) : PhotoUploadStatus()

    object Progress : PhotoUploadStatus()
}