package alytvyniuk.com.barcodewidget.converters

import alytvyniuk.com.barcodewidget.model.Barcode
import android.graphics.Bitmap
import androidx.annotation.VisibleForTesting
import java.lang.IllegalStateException

class BitmapToBarcodeConverter {

    interface BarcodeListener {
        fun onBarcodeConverted(barcode : Barcode)
    }

    @VisibleForTesting
    var listener : BarcodeListener? = null

    @Synchronized
    fun convert(bitmap: Bitmap, barcodeListener: BarcodeListener) {
        if (this.listener != null) {
            throw IllegalStateException("BitmapToBarcodeConverter supports only one image at time")
        }
        this.listener = barcodeListener
    }

    @VisibleForTesting
    fun sendResult(barcode: Barcode) {
        val barcodeListener = this.listener
        this.listener = null
        barcodeListener!!.onBarcodeConverted(barcode)
    }
}

