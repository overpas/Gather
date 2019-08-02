package com.github.overpass.gather.model.commons.image

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.github.overpass.gather.model.commons.Runners
import com.github.overpass.gather.model.commons.exception.ImageConversionException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import java.io.ByteArrayOutputStream
import java.util.concurrent.Callable

private const val QUALITY = 100

class ImageConverter(private val contentResolver: ContentResolver) {

    fun getBytes(imageUri: Uri): Task<ByteArray> {
        return Tasks.call(Runners.intensive(), Callable {
            uriToBytes(contentResolver, imageUri)
        })
    }
}

@WorkerThread
fun uriToBytes(contentResolver: ContentResolver, imageUri: Uri): ByteArray {
    val bitmap = uriToBitmap(contentResolver, imageUri)
    val baos = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos)
    return baos.toByteArray()
}

@WorkerThread
fun uriToBitmap(contentResolver: ContentResolver, imageUri: Uri): Bitmap {
    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
    // Get the cursor
    val cursor = contentResolver.query(imageUri, filePathColumn, null, null, null)
    // Move to first row
    cursor?.moveToFirst() ?: throw ImageConversionException()
    //Get the column index of MediaStore.Images.Media.DATA
    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
    //Gets the String value in the column
    val imgDecodableString = cursor.getString(columnIndex)
    cursor.close()
    // Set the Image in ImageView after decoding the String
    return BitmapFactory.decodeFile(imgDecodableString)
}