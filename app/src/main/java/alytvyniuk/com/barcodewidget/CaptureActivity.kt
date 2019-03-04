package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.AsyncConverter
import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.db.BarcodeDao
import alytvyniuk.com.barcodewidget.model.Barcode
import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_barcode_capture.*
import javax.inject.Inject


private const val TAG = "BarcodeCaptureActivity"
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_GALLERY = 2
private const val REQUEST_CAMERA_PERMISSION_PHOTO = 10

class BarcodeCaptureActivity : AppCompatActivity(), View.OnClickListener {

    @Inject lateinit var imageToCodeConverter: ImageToCodeConverter
    @Inject lateinit var barcodeDao: BarcodeDao
    @Inject lateinit var fileStorage: FileStorage
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_capture)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        widgetId = getWidgetIdFromIntent()
        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            //TODO error
        }

        buttonFromPhoto.setOnClickListener(this)
        buttonFromGallery.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonFromPhoto -> dispatchTakePictureIntent()
            R.id.buttonFromGallery -> dispatchGalleryIntent()
        }
    }

    private fun dispatchTakePictureIntent() {
        if (!hasCameraPermission(this)) {
            requestCameraPermission(this, REQUEST_CAMERA_PERMISSION_PHOTO)
        } else {
            fileStorage.eraseImageTempFile()
            val uri = fileStorage.getImageTempFileUri()
            val requestIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, uri)
            startActivityForResult(requestIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun dispatchGalleryIntent() {
        val imagePickerIntent = Intent(Intent.ACTION_PICK).setType("image/*")
        startActivityForResult(imagePickerIntent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> handleImageCaptureResult(resultCode)
            REQUEST_GALLERY -> handleGalleryResult(resultCode, data)
        }
    }

    private fun handleImageCaptureResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            val bitmap = fileStorage.getScaledBitmap()
            //TODO refactor
            if (bitmap != null) {
                performImageToBarcodeConversion(bitmap)
            } else {
                // TODO error message
                Log.e(TAG, "Couldn't get bitmap from photo image")
            }
        } else {
            // TODO error message
            Log.e(TAG, "Couldn't get photo image")
        }
    }

    private fun handleGalleryResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                if (bitmap != null) {
                    performImageToBarcodeConversion(bitmap)
                } else {
                    // TODO error message
                    Log.e(TAG, "Couldn't get bitmap from gallery image")
                }
            } else {
                // TODO error message
                Log.e(TAG, "Couldn't get image from gallery null uri")
            }
        } else {
            // TODO error message
            Log.e(TAG, "Couldn't get image from gallery")
        }
    }

    private fun performImageToBarcodeConversion(bitmap: Bitmap) {
        imageToCodeConverter.convertAsync(bitmap, object : AsyncConverter.ConverterListener<Barcode> {
            override fun onError(exception: Exception) {
                Log.e(TAG, "Couldn't scan", exception)
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResult(to: Barcode) {
                Log.d(TAG, "Result of scan = $to")
                handleResult(to)
            }
        })
    }

    private fun handleResult(barcode: Barcode) {
        startActivity(EditActivity.intent(this, widgetId, barcode))
//        updateDb(barcode, widgetId)
//        val resultValue = Intent()
//        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
//        setResult(Activity.RESULT_OK, resultValue)
//        finish()
    }

    private fun updateDb(barcode: Barcode, widgetId : Int) {
        barcodeDao.insert(barcode, widgetId)
    }

    private fun getWidgetIdFromIntent() : Int {
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            return extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }
        return AppWidgetManager.INVALID_APPWIDGET_ID
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
