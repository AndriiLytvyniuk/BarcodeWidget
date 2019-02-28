package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.CodeToImageConverter
import alytvyniuk.com.barcodewidget.converters.ImageToCodeConverter
import alytvyniuk.com.barcodewidget.converters.AsyncConverter
import alytvyniuk.com.barcodewidget.model.Barcode
import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_barcode_capture.*
import android.content.Intent
import android.provider.MediaStore
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
import android.widget.RemoteViews
import java.lang.Exception


private const val TAG = "BarcodeCaptureActivity"
private const val REQUEST_IMAGE_CAPTURE = 1
private const val REQUEST_GALLERY = 2
private const val REQUEST_CAMERA_PERMISSION_PHOTO = 10

class BarcodeCaptureActivity : AppCompatActivity(), View.OnClickListener {

    @Inject lateinit var codeToImageConverter : CodeToImageConverter
    @Inject lateinit var imageToCodeConverter: ImageToCodeConverter
    //TODO Inject
    private lateinit var fileStorage: FileStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_capture)
        setSupportActionBar(toolbar)

        //DaggerConverterComponent.create().inject(this)
        buttonFromPhoto.setOnClickListener(this)
        buttonFromGallery.setOnClickListener(this)
        fileStorage = FileStorage(applicationContext)
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
            val bitmap = BitmapFactory.decodeFile(fileStorage.getImageTempFile().absolutePath)
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
        imageToCodeConverter.convert(bitmap, object : AsyncConverter.ConverterListener<Barcode> {
            override fun onError(exception: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResult(to: Barcode) {
                performBarcodeToImageConversion(to)
            }
        })
    }

    private fun performBarcodeToImageConversion(barcode: Barcode) {
        codeToImageConverter.convert(barcode, object : AsyncConverter.ConverterListener<Bitmap> {
            override fun onError(exception: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

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

            val appWidgetManager = AppWidgetManager.getInstance(this)

            val remoteViews = RemoteViews(packageName, R.layout.widget_layout)

            val b = BitmapFactory.decodeResource(resources, R.drawable.sample)
            remoteViews.setBitmap(R.id.widget_root_view, "setImageBitmap", b)
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

            val resultValue = Intent()
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
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
