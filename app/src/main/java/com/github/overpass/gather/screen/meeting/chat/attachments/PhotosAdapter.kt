package com.github.overpass.gather.screen.meeting.chat.attachments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.get
import coil.api.load
import com.github.overpass.gather.R
import com.github.overpass.gather.model.commons.scope
import kotlinx.android.synthetic.main.item_photo.view.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class PhotosAdapter(private val listener: OnClickListener) : RecyclerView.Adapter<PhotosAdapter.AttachmentViewHolder>() {

    private val photos = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return AttachmentViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_photo, parent, false))
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(photos[position], listener)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setItems(newPhotos: List<String>?) {
        if (newPhotos != null && newPhotos.isNotEmpty()) {
            photos.clear()
            photos.addAll(newPhotos)
            notifyDataSetChanged()
        }
    }

    class AttachmentViewHolder internal constructor(itemView: View)
        : RecyclerView.ViewHolder(itemView), CoroutineScope by scope(Dispatchers.Main) {

        private val ivPhotoAttachment: ImageView = itemView.ivPhotoAttachment

        fun bind(photoUrl: String, listener: OnClickListener) {
            launch {
                val photoDrawable = Coil.get(photoUrl)
                ivPhotoAttachment.setImageDrawable(photoDrawable)
            }
            itemView.setOnClickListener { view -> listener.onItemClick(photoUrl) }
        }
    }

    interface OnClickListener {
        fun onItemClick(photoUrl: String)
    }
}
