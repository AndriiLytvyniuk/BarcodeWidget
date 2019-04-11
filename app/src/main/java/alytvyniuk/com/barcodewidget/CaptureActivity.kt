package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.EditActivity.Companion.REQUEST_EDIT_ACTIVITY
import alytvyniuk.com.barcodewidget.model.Barcode
import alytvyniuk.com.barcodewidget.model.RawBarcode
import alytvyniuk.com.barcodewidget.model.isValidWidgetId
import android.Manifest
import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_capture.*
import javax.inject.Inject

private const val TAG = "CaptureActivity"
private const val REQUEST_GALLERY = 2
private const val REQUEST_LIST_ACTIVITY = 3
private const val REQUEST_CAMERA_PERMISSION_ON_RESUME = 11

@SuppressWarnings("TooManyFunctions")
class CaptureActivity : AppCompatActivity(), View.OnClickListener, BarcodeResultHandler {

    @Inject
    lateinit var imageConvertModelFactory: ImageConvertModelFactory
    @VisibleForTesting
    var newWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var model : ImageConvertViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        App.component().inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        setSupportActionBar(toolbar)
        initViewModel()
        newWidgetId = getWidgetIdFromIntent(intent)
        updateWidgetTextView.visibility =
            if (newWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) View.GONE else View.VISIBLE
        galleryButton.setOnClickListener(this)
        savedButton.setOnClickListener(this)
        if (Intent.ACTION_VIEW == intent.action) {
            handleViewImage()
        }
    }

    private fun initViewModel() {
        model = ViewModelProviders.of(this, imageConvertModelFactory)
            .get(ImageConvertViewModel::class.java)
        model.observe(this, Observer { convertResponse ->
            hideProgress()
            when {
                convertResponse.barcode != null -> {
                    Log.d(TAG, "Result of conversion = $convertResponse.barcode")
                    onBarcodeResult(convertResponse.barcode)
                }
                convertResponse.exception != null -> Log.e(TAG, "Couldn't convert", convertResponse.exception)
                //TODO handle
                else -> throw IllegalStateException("Both barcode and exception can't be null")
            }
        })
    }

    override fun onClick(v: View) {
        if (!isProgressShown()) {
            when (v.id) {
                R.id.galleryButton -> dispatchGalleryIntent()
                R.id.savedButton -> startSavedBarcodesActivity()
            }
        }
    }

    private fun startSavedBarcodesActivity() {
        startActivityForResult(ListActivity.intent(this, newWidgetId), REQUEST_LIST_ACTIVITY)
    }

    private fun dispatchGalleryIntent() {
        val imagePickerIntent = Intent(Intent.ACTION_PICK).setType("image/*")
        startActivityForResult(imagePickerIntent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult: $requestCode, $resultCode")
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            REQUEST_GALLERY -> handleGalleryResult(resultCode, data)
            REQUEST_EDIT_ACTIVITY, REQUEST_LIST_ACTIVITY -> handleEditActivityResult(resultCode)
        }
    }

    private fun handleViewImage() {
        if (intent.data != null) {
            performImageToBarcodeConversion(intent.data)
        } else {
            // TODO error message
            Log.e(TAG, "Couldn't get image from ACTION_VIEW null uri")
        }
    }

    private fun handleGalleryResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.data != null) {
                performImageToBarcodeConversion(data.data)
            } else {
                // TODO error message
                Log.e(TAG, "Couldn't get image from gallery null uri")
            }
        } else {
            // TODO error message
            Log.e(TAG, "Couldn't get image from gallery")
        }
    }

    private fun handleEditActivityResult(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK && newWidgetId.isValidWidgetId()) {
            Log.d(TAG, "Widget for id $newWidgetId was set up successfully")
            val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, newWidgetId)
            setResult(Activity.RESULT_OK, resultValue)
            finish()
        }
    }

    private fun performImageToBarcodeConversion(uri: Uri) {
        showProgress()
        model.performConversion(uri)
    }

    override fun onBarcodeResult(rawBarcode: RawBarcode) {
        if (!isProgressShown()) {
            val barcode = Barcode(rawBarcode)
            startActivityForResult(EditActivity.intent(this, barcode, newWidgetId), REQUEST_EDIT_ACTIVITY)
        }
    }

    @VisibleForTesting
    fun getWidgetIdFromIntent(intent: Intent) : Int {
        return intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )
    }

    private fun requestCameraPermission(activity: Activity, requestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            //TODO Show Rationale
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), requestCode)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!hasCameraPermission()) {
            requestCameraPermission(this, REQUEST_CAMERA_PERMISSION_ON_RESUME)
        }
    }

    private fun showProgress() {
        progressCircle.visibility = View.VISIBLE
        invalidateOptionsMenu()
    }

    private fun hideProgress() {
        progressCircle.visibility = View.GONE
        invalidateOptionsMenu()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_about, menu)
        if (isProgressShown()) {
            menu.findItem(R.id.about).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.about -> {
                startActivity(AboutActivity.intent(this))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun isProgressShown() = progressCircle.visibility == View.VISIBLE
}

interface BarcodeResultHandler {

    fun onBarcodeResult(rawBarcode: RawBarcode)
}

fun Context.hasCameraPermission() = ContextCompat
    .checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

fun Context.getBitmapForImageUri(uri: Uri): Bitmap? =
    MediaStore.Images.Media.getBitmap(contentResolver, uri)
