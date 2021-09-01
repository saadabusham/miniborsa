package com.technzone.miniborsa.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.fragment.app.Fragment
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import com.technzone.miniborsa.R

class ImagePickerUtil {

    companion object {

        const val TAKE_USER_IMAGE_REQUEST_CODE = 11111
        @SuppressLint("ResourceType")
        fun openImagePicker(
            activity: Activity? = null,
            fragment: Fragment? = null,
            selectedImagesLimit: Int = 1,
            cameraOnly: Boolean = false,
            selectMultipleImages: Boolean = false,
            showFolderMode: Boolean = true,
            showCameraButton: Boolean = true,
            showDoneButtonAlways: Boolean = true,
            requestCode: Int = Config.RC_PICK_IMAGES

        ) {
            if (activity == null && fragment == null) {
                return
            }

            val context: Context
            val imagePickerBuilder = if (activity != null) {
                context = activity
                ImagePicker.with(activity)
            } else {
                context = fragment?.requireContext()!!
                ImagePicker.with(fragment)
            } //  Initialize ImagePicker with activity or fragment context


            imagePickerBuilder
                .setToolbarColor(context.getString(R.color.colorPrimary))         //  Toolbar color
                .setStatusBarColor(context.getString(R.color.colorPrimaryDark))       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor(context.getString(R.color.title_toolbar_color))     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor(context.getString(R.color.title_toolbar_color))     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor(context.getString(R.color.colorAccent))     //  ProgressBar color
                .setBackgroundColor(context.getString(android.R.color.white))      //  Background color
                .setCameraOnly(cameraOnly)               //  Camera mode
                .setMultipleMode(selectMultipleImages)              //  Select multiple images or single image
                .setFolderMode(showFolderMode)                //  Folder mode
                .setShowCamera(showCameraButton)                //  Show camera button
                .setFolderTitle(context.getString(R.string.choose_image_folders_mode_title))           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle(context.getString(R.string.choose_image_done_button_txt))               //  Done button title
                .setLimitMessage(context.getString(R.string.choose_image_max_select_message))    // Selection limit message
                .setMaxSize(selectedImagesLimit)                     //  Max images can be selected
                .setAlwaysShowDoneButton(showDoneButtonAlways)      //  Set always show done button in multiple mode
                .setRequestCode(requestCode)                //  Set request code, default Config.RC_PICK_IMAGES
                .start()                           //  Start ImagePicker
        }

        fun getSelectedImagesPathsFromImagePicker(
            resultCode: Int,
            data: Intent?
        ): List<String> {
            val images: ArrayList<Image> = ArrayList()
            if (resultCode == RESULT_OK) {
                images.addAll(data?.getParcelableArrayListExtra(Config.EXTRA_IMAGES) ?: ArrayList())
            }
            return images.map { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) it.uri.toString() else it.path }
        }
    }


}

fun Fragment.pickImages(requestCode: Int) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)

        .start(requestCode)
}

fun Fragment.captureImage(requestCode: Int) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .cameraOnly()
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start(requestCode)
}

fun Activity.pickImages(requestCode: Int) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)

        .start(requestCode)
}

fun Activity.captureImage(requestCode: Int) {
    com.github.dhaval2404.imagepicker.ImagePicker.with(this)
        .crop() //Crop image(Optional), Check Customization for more option
        .compress(1024)            //Final image size will be less than 1 MB(Optional)
        .maxResultSize(
            1080,
            1080
        )    //Final image resolution will be less than 1080 x 1080(Optional)
        .start(requestCode)
}
