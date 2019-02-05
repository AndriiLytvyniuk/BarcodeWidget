package alytvyniuk.com.barcodewidget

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import java.io.File


class BarcodeActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

//        val intent = intent
//        val extras = intent.extras
//        if (extras != null) {
//            val mAppWidgetId = extras.getInt(
//                AppWidgetManager.EXTRA_APPWIDGET_ID,
//                AppWidgetManager.INVALID_APPWIDGET_ID
//            )
//            val resultValue = Intent()
//            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId)
//            setResult(Activity.RESULT_OK, resultValue)
//        }

//        val options = FirebaseVisionBarcodeDetectorOptions.Builder()
//            .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
//            .build()
//
//        val detector = FirebaseVision.getInstance()
//            .getVisionBarcodeDetector(options)

        dispatchTakePictureIntent()
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        // Continue only if the File was successfully created
        val photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun createImageFile(): File {
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val tempFile = File.createTempFile("temp_image", ".jpg", storageDir)
        tempFile.delete()
        return tempFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

        }
    }

    private fun decodeImage() {

    }
}
