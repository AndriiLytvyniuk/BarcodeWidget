package alytvyniuk.com.barcodewidget

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

private const val FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider"

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
}