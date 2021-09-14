package com.technzone.miniborsa.utils

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import java.io.*


object ContentUriUtils {

    private const val BUFFER_SIZE = 1024 * 2
    private const val IMAGE_DIRECTORY = "/miniborsa_temp"

    fun Uri.getFilePathFromURI(context: Context): String? {
        val fileName = getFileName(this)
        val wallpaperDirectory = File(
            context.getExternalFilesDir(null)?.absolutePath + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(wallpaperDirectory.path + File.separator.toString() + fileName)
            // create folder if not exists
            copy(context, this, copyFile)
            return copyFile.absolutePath
        }
        return null
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!)
                ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copystream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @Throws(java.lang.Exception::class, IOException::class)
    fun copystream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also({ n = it }) != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
//                Log.e(e.getMessage(), java.lang.String.valueOf(e))
                e
            }
            try {
                `in`.close()
            } catch (e: IOException) {
//                Log.e(e.getMessage(), java.lang.String.valueOf(e))
                e
            }
        }
        return count
    }
}