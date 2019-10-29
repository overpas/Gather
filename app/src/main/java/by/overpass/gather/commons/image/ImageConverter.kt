package by.overpass.gather.commons.image

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import by.overpass.gather.di.DEFAULT_DISPATCHER
import by.overpass.gather.commons.exception.ImageConversionException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Named

class ImageConverter @Inject constructor(
        private val contentResolver: ContentResolver,
        @Named(DEFAULT_DISPATCHER) private val defaultDispatcher: CoroutineDispatcher
) {

    suspend fun getImageBytes(imageUri: Uri): ByteArray = withContext(defaultDispatcher) {
        resolveBytesFromImageUri(imageUri)
    }

    @WorkerThread
    fun resolveBytesFromImageUri(imageUri: Uri): ByteArray {
        val cursor = contentResolver.query(imageUri, imageDataColumns, null, null, null)
        return getBitmapBytesFromCursor(cursor)
    }

    @WorkerThread
    private fun getBitmapBytesFromCursor(cursor: Cursor?): ByteArray {
        val bitmap = decodeBitmapFromCursor(cursor)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, MAX_QUALITY, baos)
        return baos.toByteArray()
    }

    @WorkerThread
    private fun decodeBitmapFromCursor(cursor: Cursor?): Bitmap {
        // Move to first row
        cursor?.moveToFirst() ?: throw ImageConversionException()
        //Get the column index of MediaStore.Images.Media.DATA
        val columnIndex = cursor.getColumnIndex(imageDataColumns[0])
        //Gets the String value in the column
        val imgDecodableString = cursor.getString(columnIndex)
        cursor.close()
        // Set the Image in ImageView after decoding the String
        return BitmapFactory.decodeFile(imgDecodableString)
    }

    companion object {
        private const val MAX_QUALITY = 100
        private val imageDataColumns = arrayOf(MediaStore.Images.Media.DATA)
    }
}