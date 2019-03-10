package alytvyniuk.com.barcodewidget

import alytvyniuk.com.barcodewidget.converters.*
import alytvyniuk.com.barcodewidget.model.RawBarcode
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_test_capture.*
import java.lang.Exception
import javax.inject.Inject
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.RGBLuminanceSource




class TestCaptureActivity : AppCompatActivity(), View.OnClickListener {

    @Inject lateinit var codeToImageConverter : CodeToImageConverter
    @Inject lateinit var imageToCodeConverter: ImageToCodeConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_capture)
        setSupportActionBar(toolbar)
        App.component().inject(this)
        fab.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> convertImage()
        }
    }

    private fun convertImage() {
        val imageId = R.drawable.test_photo
        val options = BitmapFactory.Options().apply {
            inSampleSize = 4
        }
        val b = BitmapFactory.decodeResource(resources, imageId, options)
        sampleImageView.setImageBitmap(b)
        //performImageToBarcodeConversion(b)
        val result = syncDecodeQRCode(b)
        Log.d("Andrii", "convertImage: $result")
    }

    fun syncDecodeQRCode(bitmap: Bitmap): String? {
        try {
            val width = bitmap.width
            val height = bitmap.height
            val pixels = IntArray(width * height)
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
            val source = RGBLuminanceSource(width, height, pixels)
            val result = MultiFormatReader().decode(BinaryBitmap(HybridBinarizer(source)))
            return result.text
        } catch (e: Exception) {
            return null
        }

    }


    private fun performImageToBarcodeConversion(bitmap: Bitmap) {
        imageToCodeConverter.convertAsync(bitmap, object : AsyncConverter.ConverterListener<RawBarcode> {
            override fun onError(exception: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResult(to: RawBarcode) {
                performBarcodeToImageConversion(to)
            }
        })
    }

    private fun performBarcodeToImageConversion(rawBarcode: RawBarcode) {
        codeToImageConverter.convertAsync(rawBarcode, object : AsyncConverter.ConverterListener<Bitmap> {
            override fun onError(exception: Exception) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResult(to: Bitmap) {
                setImage(to)
            }
        })
    }

    private fun setImage(bitmap: Bitmap) {
        resultImageView.setImageBitmap(bitmap)
    }
}
