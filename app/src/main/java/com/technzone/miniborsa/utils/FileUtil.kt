package com.technzone.miniborsa.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream


object FileUtil {

    fun getImageToByte(filePath: String?): String? {
        val bmp: Bitmap?
        val bos: ByteArrayOutputStream?
        val bt: ByteArray?
        var encodeString: String? = null
        try {
            bmp = BitmapFactory.decodeFile(filePath)
            bos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bt = bos.toByteArray()
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return encodeString
    }
}