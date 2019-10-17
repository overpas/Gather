package com.github.overpass.gather.di.meeting.chat.attachments.detail

import com.github.overpass.gather.di.ComponentManager

class AttachmentDetailsComponentManager(
        attachmentsDetailsComponent: AttachmentsDetailsComponent
) : ComponentManager<Unit, AttachmentsDetailsComponent>({ attachmentsDetailsComponent })