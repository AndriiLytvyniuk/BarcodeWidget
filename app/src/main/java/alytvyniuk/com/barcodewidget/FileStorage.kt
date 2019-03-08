package alytvyniuk.com.barcodewidget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

private const val FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider"
private const val TAG = "FileStorage"

class FileStorage(private val context: Context) {

    fun eraseImageTempFile(): File {
        val tempFile = getImageTempFile()
        tempFile.delete()
        return tempFile
    }

    fun getImageTempFile() : File {
        val storageDir: File = context.filesDir
        val fileName = "temp_image"
        val fileExtension = ".jpg"
        return File("$storageDir", "$fileName.$fileExtension")
    }

    fun getImageTempFileUri() : Uri {
        val file = getImageTempFile()
        return FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, file)
    }

    //TODO refactor
    fun getScaledBitmap() : Bitmap {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        val filePath = getImageTempFile().absolutePath
        BitmapFactory.decodeFile(filePath, options)
        val imageHeight: Int = options.outHeight
        val imageWidth: Int = options.outWidth
        Log.d(TAG, "initial size: $imageWidth * $imageHeight")
        options.inSampleSize = calculateInSampleSize(options, 300, 400)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath)
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }
}