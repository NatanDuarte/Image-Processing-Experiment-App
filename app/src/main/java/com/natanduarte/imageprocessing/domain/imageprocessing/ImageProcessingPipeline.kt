package com.natanduarte.imageprocessing.domain.imageprocessing

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class ImageProcessingPipeline(
    private var context: Context,
    private var imageView: ImageView
) {

    fun run() {
        val bitmapOfImage = imageView.drawable.toBitmap()

        val resultGrayImage = convertIntoGrayscale(bitmapOfImage)

        sendResultToImageView(resultGrayImage)
    }

    private fun convertIntoGrayscale(bitmap: Bitmap): Bitmap? {
        val image = Mat()
        val grayImage = Mat()

        Utils.bitmapToMat(bitmap, image)
        Imgproc.cvtColor(image, grayImage, Imgproc.COLOR_RGB2GRAY)
        val result = Bitmap
            .createBitmap(grayImage.cols(), grayImage.rows(), Bitmap.Config.ARGB_8888)

        Utils.matToBitmap(grayImage, result)

        return result
    }

    private fun sendResultToImageView(resultGrayImage: Bitmap?) {
        Glide
            .with(context)
            .load(resultGrayImage)
            .into(imageView)
    }
}