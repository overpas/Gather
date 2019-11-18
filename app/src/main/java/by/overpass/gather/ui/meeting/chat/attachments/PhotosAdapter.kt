package by.overpass.gather.ui.meeting.chat.attachments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.overpass.gather.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo.view.*
import java.util.*

class PhotosAdapter(
        private val listener: (String) -> Unit
) : RecyclerView.Adapter<PhotosAdapter.AttachmentViewHolder>() {

    private val photos = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        return LayoutInflater.from(parent.context)
                .run { inflate(R.layout.item_photo, parent, false) }
                .let { AttachmentViewHolder(it, listener) }
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) {
        holder.bind(photos[position])
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

    class AttachmentViewHolder(
            itemView: View,
            private val listener: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivPhotoAttachment: ImageView = itemView.ivPhotoAttachment

        fun bind(photoUrl: String) {
            Glide.with(itemView)
                    .load(photoUrl)
                    .into(ivPhotoAttachment)
            itemView.setOnClickListener { view -> listener(photoUrl) }
        }
    }
}
