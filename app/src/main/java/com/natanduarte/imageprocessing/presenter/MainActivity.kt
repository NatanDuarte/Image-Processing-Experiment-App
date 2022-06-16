package com.natanduarte.imageprocessing.presenter

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.natanduarte.imageprocessing.R
import com.natanduarte.imageprocessing.domain.IoHandling
import com.natanduarte.imageprocessing.domain.OpenCvStatusChecker
import com.natanduarte.imageprocessing.domain.PermissionHandler
import com.natanduarte.imageprocessing.domain.imageprocessing.ImageProcessingPipeline


class MainActivity : AppCompatActivity() {
    private lateinit var imagePreview: ImageView
    private lateinit var processImageButton: Button
    private lateinit var resetImageButton: Button
    private lateinit var saveImageButton: Button
    private lateinit var contentGetter: ActivityResultLauncher<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = getString(R.string.app_title)

        PermissionHandler(this@MainActivity).writePermissions()

        initializeContentGetter(this)
        initializeViewComponents()
        initializeListeners()
    }

    override fun onResume() {
        super.onResume()
        OpenCvStatusChecker(this).check()
    }

    private fun initializeListeners() {
        imagePreview.setOnClickListener { handleImageChoice() }
        processImageButton.setOnClickListener { handleImageProcessing() }
        resetImageButton.setOnClickListener { imagePreview.setImageDrawable(null) }
        saveImageButton.setOnClickListener {
            if (imagePreview.drawable != null) {
                IoHandling(this)
                    .writeImage(imagePreview.drawable.toBitmap(), "imageProcessing")
            }
        }
    }

    private fun initializeViewComponents() {
        imagePreview = findViewById(R.id.image_preview)
        processImageButton = findViewById(R.id.button_process_image)
        saveImageButton = findViewById(R.id.button_save_image)
        resetImageButton = findViewById(R.id.button_reset_image)
    }


    private fun initializeContentGetter(context: Context) {
        contentGetter = registerForActivityResult(GetContent()) { result: Uri? ->
            Glide
                .with(context)
                .load(result)
                .into(imagePreview)
        }
    }

    private fun handleImageChoice() {
        val inputs = "image/*"
        contentGetter.launch(inputs)
    }

    private fun handleImageProcessing() {
        if (imagePreview.drawable != null) {
            ImageProcessingPipeline(this, imagePreview).run()
        }
    }
}