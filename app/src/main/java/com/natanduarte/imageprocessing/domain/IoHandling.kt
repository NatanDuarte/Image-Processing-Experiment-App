package com.natanduarte.imageprocessing.domain

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.natanduarte.imageprocessing.R
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class IoHandling(private var context: Context) {

    fun writeImage(bitmap: Bitmap, path: String) {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val pathname = "$root/DCIM/$path"
        val file = File(pathname)

        file.mkdirs()

        val filename = "${getFileCreationPreciseDate()}.jpg"
        val imageFile = File(file, filename)

        if (imageFile.exists()) {
            imageFile.delete()
        }

        try {
            writeImageFile(imageFile, bitmap, file, context)
            Toast.makeText(
                context,
                "Image saved successfully",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Error: $e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun writeImageFile(
        imageFile: File,
        bitmap: Bitmap,
        file: File,
        context: Context
    ) {
        val fileOutputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
        notifyImageFileCreation(file.absolutePath, context)
    }

    private fun getFileCreationPreciseDate(): String {
        val dateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(
            context
                .getString(R.string.creationPreciseDatePattern)
        )

        return dateTime.format(formatter)
    }

    private fun notifyImageFileCreation(path: String, context: Context) {
        MediaScannerConnection
            .scanFile(
                context,
                arrayOf(path),
                null
            ) { path, _ ->
                Log.i("TAG", "Finished scanning $path")
            }
    }
}