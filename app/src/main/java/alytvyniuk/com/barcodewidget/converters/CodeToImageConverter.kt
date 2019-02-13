package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.VisibleForTesting
import java.lang.IllegalStateException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CodeToImageConverter @Inject constructor(
    private val barcodeToBitmap: BarcodeToBitmap
) {

    interface BitmapListener {
        fun onBitmapConverted(bitmap: Bitmap)
    }

    @VisibleForTesting
    var listener: BitmapListener? = null

    @Synchronized
    fun convert(barcode: Barcode, bitmapListener: BitmapListener) {
        if (this.listener != null) {
            throw IllegalStateException("BarcodeToBitmapConverter supports only one image at time")
        }
        this.listener = bitmapListener
        performConversion(barcode)
    }

    @VisibleForTesting
    fun performConversion(barcode: Barcode) {
        val bitmap = barcodeToBitmap.convert(barcode)
        listener?.onBitmapConverted(bitmap)
        listener = null
    }
}