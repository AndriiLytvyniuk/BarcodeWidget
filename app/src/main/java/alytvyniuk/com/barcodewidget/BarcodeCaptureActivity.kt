package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.dagger.AsyncConverter
import alytvyniuk.com.barcodewidget.dagger.DaggerConverterComponent
import alytvyniuk.com.barcodewidget.model.Barcode
import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_barcode_capture.*
import android.content.Intent
import android.provider.MediaStore
import androidx.core.content.FileProvider
import java.io.File
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import javax.inject.Inject
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class BarcodeCaptureActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "BarcodeCaptureActivity"
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_CAMERA_PERMISSION_PHOTO = 10
        private const val FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider"
    }

    @Inject lateinit var codeToImageConverter : CodeToImageConverter
    @Inject lateinit var imageToCodeConverter: ImageToCodeConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_capture)
        setSupportActionBar(toolbar)

        DaggerConverterComponent.create().inject(this)
        buttonFromPhoto.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonFromPhoto -> dispatchTakePictureIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        if (!hasCameraPermission(this)) {
            requestCameraPermission(this, REQUEST_CAMERA_PERMISSION_PHOTO)
        } else {
            val photoFile = createImageFile()
            val photoURI = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, photoFile)
            val requestIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(requestIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageFile(): File {
        val tempFile = getImageFile()
        tempFile.delete()
        return tempFile
    }

    private fun getImageFile() : File {
        val storageDir: File = filesDir
        val fileName = "temp_image"
        val fileExtension = ".jpg"
        return File.createTempFile(fileName, fileExtension, storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(getImageFile().absolutePath)
                if (bitmap != null) {
                    performImageToBarcodeConversion(bitmap)
                } else {
                    // TODO error message
                    Log.e(TAG, "Couldn't get bitmap from image")
                }
            } else {
                // TODO error message
                Log.e(TAG, "Couldn't get photo image")
            }
        }
    }

    private fun performImageToBarcodeConversion(bitmap: Bitmap) {
        imageToCodeConverter.convert(bitmap, object : AsyncConverter.ConverterListener<Barcode> {
            override fun onResult(to: Barcode) {
                performBarcodeToImageConversion(to)
            }
        })
    }

    private fun performBarcodeToImageConversion(barcode: Barcode) {
        codeToImageConverter.convert(barcode, object : AsyncConverter.ConverterListener<Bitmap> {
            override fun onResult(to: Bitmap) {
                initWidget()
            }
        })
    }

    private fun initWidget() {
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            val appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            resultValue.putExtra("Andrii", "AndriiValue")
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }

    private fun requestCameraPermission(activity: Activity, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            //TODO Show Rationale
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), requestCode)
        }
    }

    private fun hasCameraPermission(context: Context) = ContextCompat
        .checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
            permissions.isNotEmpty() && permissions[0] == Manifest.permission.CAMERA) {
            when (requestCode) {
                REQUEST_CAMERA_PERMISSION_PHOTO -> dispatchTakePictureIntent()
            }
        }
    }
}
