package com.example.gaurichugh

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream

object CacheUtils {

    fun saveBitmapToCache(context: Context, bitmap: Bitmap, url: String) {
        val fileName = url.hashCode().toString()
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    }

    fun getBitmapFromCache(context: Context, url: String): Bitmap? {
        val fileName = url.hashCode().toString()
        val file = File(context.cacheDir, fileName)
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }
}
