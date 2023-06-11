package com.technzone.minibursa.utils

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import com.github.dhaval2404.imagepicker.ImagePicker

const val TAKE_USER_IMAGE_REQUEST_CODE = 11111

fun Fragment.pickImages(
    requestCode: Int? = null,
    resultLauncher: ActivityResultLauncher<Intent>? = null
) {
    (com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )).let {
            if (resultLauncher != null)
                it.createIntent { intent ->
                    resultLauncher.launch(intent)
                }
            else {
                requestCode?.let { it1 -> it.start(it1) }
            }
        }
}

fun Fragment.captureImage(
    requestCode: Int? = null,
    resultLauncher: ActivityResultLauncher<Intent>? = null
) {
    (com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .cameraOnly()
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )).let {
            if (resultLauncher != null)
                it.createIntent { intent ->
                    resultLauncher.launch(intent)
                }
            else {
                requestCode?.let { it1 -> it.start(it1) }
            }
        }

}

fun Activity.pickImages(
    requestCode: Int? = null,
    resultLauncher: ActivityResultLauncher<Intent>? = null
) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        ).let {
            if (resultLauncher != null)
                it.createIntent { intent ->
                    resultLauncher.launch(intent)
                }
            else {
                requestCode?.let { it1 -> it.start(it1) }
            }
        }
}

fun Activity.captureImage(
    requestCode: Int? = null,
    resultLauncher: ActivityResultLauncher<Intent>? = null
) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        ).let {
            if (resultLauncher != null)
                it.createIntent { intent ->
                    resultLauncher.launch(intent)
                }
            else {
                requestCode?.let { it1 -> it.start(it1) }
            }
        }

}
