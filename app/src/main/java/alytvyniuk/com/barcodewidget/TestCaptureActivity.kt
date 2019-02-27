package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.*
import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_capture.*
import java.lang.Exception


class TestCaptureActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var codeToImageConverter : CodeToImageConverter
    lateinit var imageToCodeConverter: ImageToCodeConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_capture)
        setSupportActionBar(toolbar)
        fab.setOnClickListener(this)
        codeToImageConverter = ZXingCodeToImageConverter(Looper.getMainLooper())
        imageToCodeConverter = MLKitImageToCodeConverter(Looper.getMainLooper())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> convertImage()
        }
    }

    private fun convertImage() {
        val b = BitmapFactory.decodeResource(resources, R.drawable.widget_preview)
        performImageToBarcodeConversion(b)
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
                setImage(to)
            }
        })
    }

    private fun setImage(bitmap: Bitmap) {
        testImageView.setImageBitmap(bitmap)
    }
}
