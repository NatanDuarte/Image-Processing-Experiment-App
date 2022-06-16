package com.natanduarte.imageprocessing.domain

import android.content.Context
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader

class OpenCvStatusChecker(var context: Context) {

    fun check() {
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader
                .initAsync(
                    OpenCVLoader.OPENCV_VERSION_3_0_0,
                    context, customLoaderCallback
                )
        } else {
            customLoaderCallback
                .onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    private val customLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(
        context
    ) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                SUCCESS -> {}
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }
}