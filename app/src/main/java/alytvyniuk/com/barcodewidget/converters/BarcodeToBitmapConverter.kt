package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.VisibleForTesting
import java.lang.IllegalStateException

class BarcodeToBitmapConverter {
    interface BitmapListener {
        fun onBitmapConverted(bitmap: Bitmap)
    }

    @VisibleForTesting
    var listener : BitmapListener? = null

    @Synchronized
    fun convert(barcode: Barcode, bitmapListener: BitmapListener) {
        if (this.listener != null) {
            throw IllegalStateException("BarcodeToBitmapConverter supports only one image at time")
        }
        this.listener = bitmapListener
    }

    @VisibleForTesting
    fun sendResult(bitmap: Bitmap) {
        val bitmapListener = this.listener
        this.listener = null
        bitmapListener!!.onBitmapConverted(bitmap)
    }
}